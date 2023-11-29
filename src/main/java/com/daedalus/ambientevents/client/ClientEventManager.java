package com.daedalus.ambientevents.client;

import com.daedalus.ambientevents.AmbientEvents;
import com.daedalus.ambientevents.AmbientEventsRef;
import com.daedalus.ambientevents.parsing.GenericEvent;
import com.daedalus.ambientevents.parsing.ParsingUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@EventBusSubscriber(modid = AmbientEventsRef.MODID, value = Side.CLIENT)
public class ClientEventManager {

    private final static ResourceLocation MANIFEST_LOCATION = AmbientEvents.getResource("manifest.json");
    private static final List<GenericEvent> EVENTS = new ArrayList<>();
    public static JsonObject parsedConfig;

    public static void init(JsonObject json) {
        parsedConfig = json;
        JsonArray eventElements = parsedConfig.getAsJsonArray("events");
        for(JsonElement element : eventElements) {
            GenericEvent event = ParsingUtils.parseElement(element,GenericEvent::new);
            if(Objects.nonNull(event)) EVENTS.add(event);
        }
    }

    public static JsonObject readManifest(IResourceManager manager) {
        IResource resource = null;
        try {
            resource = manager.getResource(MANIFEST_LOCATION);
            return ParsingUtils.returnCloseable(new InputStreamReader(resource.getInputStream(),StandardCharsets.UTF_8),
                    c -> ParsingUtils.getAsObject(AmbientEventsRef.GSON.fromJson((InputStreamReader)c,JsonElement.class)),
                    ex -> AmbientEvents.logError("Could not parse manifest JSON from resource!",ex));
        } catch (IOException ex) {
            AmbientEvents.logError("Error getting manifest file!",ex);
        } finally {
            if(Objects.nonNull(resource)) {
                try {
                    resource.close();
                } catch(IOException ex) {
                    AmbientEvents.logFatal("Could not close manifest resource! Things may leak!",ex);
                }
            }
        }
        return null;
    }

    @SubscribeEvent
    public static void onClientDisconnected(ClientDisconnectionFromServerEvent e) {
        EVENTS.clear();
    }

    public static void processClientEvents(EntityPlayer player) {
        if(player==Minecraft.getMinecraft().player)
            for(GenericEvent event : EVENTS)
                event.process(player);
    }
}
