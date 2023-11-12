package com.daedalus.ambientevents.gui;

import java.util.Iterator;

import com.daedalus.ambientevents.AmbientEventsRef;
import com.google.gson.JsonObject;

import com.daedalus.ambientevents.gui.widgets.*;
import com.daedalus.ambientevents.wrappers.JSONKeyValuePair;

public class ValueTabs extends WTabView {

	public ValueTabs(WWidget parentIn) {
		super(parentIn);
	}
	
	public void populate(JSONKeyValuePair element) {
		this.clear();
		JsonObject events;
		if (ConfiguratorGUI.manifestJSON.has("events")) {
			events = ConfiguratorGUI.manifestJSON.getJSONObject("events");
		} else {
			AmbientEventsRef.LOGGER.error("No events in manifest.json");
			return;
		}

		JsonObject sublist;
		
		if (events.has(element.getKey())) {
			sublist = events.getJSONObject(element.getKey());
		} else {
			AmbientEventsRef.LOGGER.error(String.format("No %s in manifest.json", element.getKey()));
			return;
		}

		JsonObject subElement;
		
		if (sublist.has(element.getJSONObject().getString("type"))) {
			subElement = sublist.getJSONObject(element.getJSONObject().getString("type"));
		} else {
			AmbientEventsRef.LOGGER.error(String.format("No %s in manifest.json", element.getJSONObject().getString("type")));
			return;
		}
		
		Iterator<String> keys = subElement.keys();
		while(keys.hasNext()) {
			String key = keys.next();
			this.addTab(new WWidget(this), key);
		}
		
		if (this.isVisible()) {
			this.show();
		}
	}
}
