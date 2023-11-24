package com.daedalus.ambientevents.client;

import com.daedalus.ambientevents.AmbientEvents;
import com.daedalus.ambientevents.AmbientEventsRef;
import com.daedalus.ambientevents.GenericEvent;
import com.daedalus.ambientevents.ParsingUtils;
import com.google.gson.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.lang3.mutable.MutableInt;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@EventBusSubscriber(modid = AmbientEventsRef.MODID, value = Side.CLIENT)
public class AmbientEventsClient {

    private static final List<GenericEvent> EVENTS = new ArrayList<>();
    private final static ResourceLocation MANIFEST_LOCATION = AmbientEvents.getResource("manifest.json");
    private static final MutableInt TICK_TIMER = new MutableInt();
    public static JsonObject parsedConfig;
    private static boolean active = false;
    private static boolean configGood = false;
    private static int maxTick = 20;
    public static long lastSleep = 0;
    public static void init() {
        File jsonFile = AmbientEvents.getConfigFile("events.json",false);
        if(jsonFile.exists()) {
            try {
                final FileReader reader = new FileReader(jsonFile);
                ParsingUtils.tryCloseable(reader,c -> {
                    parsedConfig = AmbientEventsRef.GSON.fromJson(reader,JsonObject.class);
                    JsonArray eventElements = parsedConfig.getAsJsonArray("events");
                    for(JsonElement element : eventElements) {
                        GenericEvent event = ParsingUtils.parseElement(element,GenericEvent::new);
                        if(Objects.nonNull(event)) EVENTS.add(event);
                    }
                },ex -> AmbientEventsRef.LOGGER.error("Could not parse JSON from file {}!",jsonFile,ex));
            } catch (FileNotFoundException ex) {
                AmbientEventsRef.LOGGER.error("Could not read JSON from file {}!",jsonFile,ex);
            }
        }
        if(EVENTS.size()>maxTick) maxTick = EVENTS.size();
        configGood = !EVENTS.isEmpty();
    }

    public static JsonObject readManifest(IResourceManager manager) {
        IResource resource = null;
        try {
            resource = manager.getResource(MANIFEST_LOCATION);
            return ParsingUtils.returnCloseable(new InputStreamReader(resource.getInputStream(),StandardCharsets.UTF_8),
                    c -> ParsingUtils.getAsObject(AmbientEventsRef.GSON.fromJson((InputStreamReader)c,JsonElement.class)),
                    ex -> AmbientEventsRef.LOGGER.error("Could not parse manifest JSON from resource!",ex));
        } catch (IOException ex) {
            AmbientEventsRef.LOGGER.error("Error getting manifest file!",ex);
        } finally {
            if(Objects.nonNull(resource)) {
                try {
                    resource.close();
                } catch(IOException ex) {
                    AmbientEventsRef.LOGGER.fatal("Could not close manifest resource! Things may leak!",ex);
                }
            }
        }
        return null;
    }

    @SubscribeEvent
    public static void onWakeUp(PlayerWakeUpEvent e) {
        EntityPlayer player = getPlayer();
        if(Objects.nonNull(player) && e.getEntityPlayer()==player) lastSleep = player.world.getTotalWorldTime();
    }

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent e) {
        EntityPlayer player = getPlayer();
        if(Objects.nonNull(player) && e.player==player) {
            if(lastSleep==0) lastSleep = player.world.getTotalWorldTime();
            if(configGood) active = true;
        }
    }

    @SubscribeEvent
    public static void onLogOut(PlayerEvent.PlayerLoggedOutEvent e) {
        active = false;
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent e) {
        if(e.phase==TickEvent.Phase.START && active) {
            EntityPlayer player = getPlayer();
            if(Objects.nonNull(player)) {
                int eventIndex = TICK_TIMER.getAndAdd(1);
                for(int i=eventIndex; i<EVENTS.size(); i+=20) EVENTS.get(i).process(player);
                if(TICK_TIMER.getValue()>=maxTick) TICK_TIMER.setValue(0);
            }
        }
    }

    private static @Nullable EntityPlayer getPlayer() {
        return Minecraft.getMinecraft().player;
    }
}
