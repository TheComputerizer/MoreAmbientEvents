package com.daedalus.ambientevents.common;

import com.daedalus.ambientevents.AmbientEvents;
import com.daedalus.ambientevents.AmbientEventsRef;
import com.daedalus.ambientevents.capability.player.AmbientEventsPlayerDataProvider;
import com.daedalus.ambientevents.capability.CapabilityHandler;
import com.daedalus.ambientevents.client.ClientEventManager;
import com.daedalus.ambientevents.network.PacketSyncConfigData;
import com.daedalus.ambientevents.parsing.GenericEvent;
import com.daedalus.ambientevents.parsing.ParsingUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.mutable.MutableInt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = AmbientEventsRef.MODID)
public class EventManager {

    private static final MutableInt TICK_TIMER = new MutableInt();

    public static final List<GenericEvent> EVENTS = Collections.synchronizedList(new ArrayList<>());

    public static JsonObject parsedConfig;

    public static void loadConfig() {
        synchronized(EVENTS) {
            EVENTS.clear();
            File jsonFile = AmbientEvents.getConfigFile("events.json", false);
            if (jsonFile.exists()) {
                try {
                    final FileReader reader = new FileReader(jsonFile);
                    ParsingUtils.tryCloseable(reader, c -> {
                        parsedConfig = AmbientEventsRef.GSON.fromJson(reader, JsonObject.class);
                        JsonArray eventElements = ParsingUtils.getAsArray(parsedConfig, "events");
                        if (Objects.nonNull(eventElements)) {
                            for (JsonElement element : eventElements) {
                                GenericEvent event = ParsingUtils.parseElement(element, GenericEvent::new);
                                if (Objects.nonNull(event)) EVENTS.add(event);
                            }
                        }
                    }, ex -> AmbientEvents.logError("Could not parse JSON from file {}!", jsonFile, ex));
                } catch (FileNotFoundException ex) {
                    AmbientEvents.logError("Could not read JSON from file {}!", jsonFile, ex);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof EntityPlayerMP && !(event.getObject() instanceof FakePlayer))
            event.addCapability(CapabilityHandler.DATA_CAPABILITY,new AmbientEventsPlayerDataProvider());
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerLoggedInEvent ev) {
        if(ev.player instanceof EntityPlayerMP && Objects.nonNull(parsedConfig) && !parsedConfig.entrySet().isEmpty())
            new PacketSyncConfigData(parsedConfig).addPlayers((EntityPlayerMP)ev.player).send();
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent ev) {
        if(ev.phase==TickEvent.Phase.START) {
            if(TICK_TIMER.getAndAdd(1)>AmbientEventsRef.EVENT_TICK_RATE) {
                if(AmbientEvents.getSide(true).isServer()) {
                    synchronized(EVENTS) {
                        for(GenericEvent event : EVENTS)
                            event.process(ev.player);
                    }
                } else ClientEventManager.processClientEvents(ev.player);
                TICK_TIMER.setValue(0);
            }
        }
    }

    @SubscribeEvent
    public static void onWakeUp(PlayerWakeUpEvent ev) {
        EntityPlayer player = ev.getEntityPlayer();
        if(player instanceof EntityPlayerMP) CapabilityHandler.setLastSleep(player,player.world.getTotalWorldTime());
    }
}
