package com.daedalus.ambientevents;

import java.util.Random;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.daedalus.ambientevents.conditions.*;
import com.daedalus.ambientevents.actions.*;

import org.json.*;

public class EventHandler {
	
	// Client side event handler
	
	public EntityPlayer player;

	protected int tick = 0;
	protected Random random = new Random();
	protected boolean active = false;
	protected boolean configGood = false;
	
	protected ArrayList<GenericEvent> events;
	
	// Tracking Variables
	public long lastSleep = 0;
	
	public void init() {
		if (Config.eventsRaw == "null") {
			return;
		}
		
		JSONObject eventsJSON;
		
		try {
			eventsJSON = new JSONObject(Config.eventsRaw);
		} catch (Exception e) {
			AmbientEvents.logger.log(Level.ERROR, Config.eventsRaw);
			return;
		}
		
		events = new ArrayList<GenericEvent>();
		
		Iterator<String> eventIt = eventsJSON.keys();
		String eventName;
		
		while (eventIt.hasNext()) {
			eventName = eventIt.next();
			try {
				events.add(new GenericEvent(eventsJSON.getJSONObject(eventName)));
			} catch (Exception e) {
				AmbientEvents.logger.log(Level.ERROR, "Error parsing event: " + eventName, e);
			}
		}
		
		configGood = true;
	}
	
	@SubscribeEvent
	public void onWakeUp(PlayerWakeUpEvent e) {
		if (e.getEntityPlayer() == player) {
			lastSleep = player.world.getTotalWorldTime();
		}
	}
	
	@SubscribeEvent
	public void onLogon(PlayerLoggedInEvent e) {
		player = e.player;
		
		// TODO Change this to get data from world save
		lastSleep = player.world.getTotalWorldTime();
		
		if (configGood) {
			active = true;
		}
	}
	
	@SubscribeEvent
	public void onLogoff(PlayerLoggedOutEvent e) {
		active = false;
	}
	
	@SubscribeEvent
	public void onTick(ClientTickEvent e) {
		if (active) {
			for (int i = tick; i < events.size(); i+=20) {
				events.get(i).process(player);
			}
			tick++;
			if (tick == 20) {
				tick = 0;
			}
		}
	}
}
