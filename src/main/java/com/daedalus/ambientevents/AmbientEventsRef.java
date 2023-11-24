package com.daedalus.ambientevents;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AmbientEventsRef {
	public static final String MODID = "ambientevents";
	public static final String NAME = "More Ambient Events";
	public static final String VERSION = "1.0.0";
	public static final Logger LOGGER = LogManager.getLogger(NAME);
	public static final Gson GSON = new GsonBuilder().disableHtmlEscaping().setLenient().setPrettyPrinting().create();
}
