package com.daedalus.ambientevents.client;

import com.daedalus.ambientevents.AmbientEvents;
import com.daedalus.ambientevents.AmbientEventsRef;
import com.daedalus.ambientevents.GenericEvent;
import com.google.gson.*;
import mods.thecomputerizer.theimpossiblelibrary.util.file.FileUtil;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@EventBusSubscriber(modid = AmbientEventsRef.MODID, value = Side.CLIENT)
public class AmbientEventsClient {

    private static final List<GenericEvent> EVENTS = new ArrayList<>();
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().setLenient().setPrettyPrinting().create();
    private final static ResourceLocation MANIFEST_LOCATION = AmbientEvents.getResource("manifest.json");
    private static final MutableInt TICK_TIMER = new MutableInt();
    private static File configDir;
    private static boolean active = false;
    private static boolean configGood = false;
    private static int maxTick = 20;
    public static long lastSleep = 0;

    public static void setConfigDir(File dir) {
        configDir = dir;
    }

    public static void init() {
        if(Objects.isNull(configDir)) {
            AmbientEventsRef.LOGGER.fatal("Could not locate config directory?");
            return;
        }
        File jsonFile = FileUtil.generateNestedFile(new File(configDir,AmbientEventsRef.NAME+"/events.json"),false);
        if(jsonFile.exists()) {
            try {
                FileReader reader = new FileReader(jsonFile);
                try {
                    JsonObject fileJson = GSON.fromJson(reader,JsonObject.class);
                    JsonArray events = fileJson.getAsJsonArray("events");
                    for(JsonElement event : events) {
                        EVENTS.add(new GenericEvent((JsonObject)event));
                    }
                } catch(JsonIOException ex) {
                    AmbientEventsRef.LOGGER.error("Could not parse JSON from file {}!",jsonFile,ex);
                } finally {
                    try {
                        reader.close();
                    } catch(IOException ex) {
                        AmbientEventsRef.LOGGER.fatal("Could not close JSON reader for file {}! Things may leak!",jsonFile,ex);
                    }
                }
            } catch (FileNotFoundException ex) {
                AmbientEventsRef.LOGGER.error("Could not read JSON from file {}!",jsonFile,ex);
            }
        }
        if(EVENTS.size()>maxTick) maxTick = EVENTS.size();
        configGood = true;
    }

    public static JsonObject readManifest(IResourceManager manager) {
        IResource resource = null;
        try {
            resource = manager.getResource(MANIFEST_LOCATION);
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
