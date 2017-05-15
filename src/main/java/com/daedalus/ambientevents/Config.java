package com.daedalus.ambientevents;

import org.apache.logging.log4j.Level;

import com.daedalus.ambientevents.proxy.CommonStartupProxy;

import net.minecraftforge.common.config.Configuration;

public class Config {
	
	private static final String CATEGORY_EVENTS = "events";
	
	public static String eventsRaw = "null";
	
	public static void readConfig() {
		
		Configuration cfg = CommonStartupProxy.config;
		
		try {
			
			cfg.load();
			initEventsConfig(cfg);
			
		} catch (Exception e1) {
			
			AmbientEvents.logger.log(Level.ERROR, "Problem loading config file!", e1);
			
		} finally {
			
			if (cfg.hasChanged()) {
				cfg.save();
			}
		}
	}
	
	private static void initEventsConfig(Configuration cfg) {
		cfg.addCustomCategoryComment(CATEGORY_EVENTS, "Events Configuration");
		eventsRaw = cfg.getString("Events", CATEGORY_EVENTS, eventsRaw, "Enter list of conditions and events. Must be valid JSON on a single line or \"null\".");
	}
}