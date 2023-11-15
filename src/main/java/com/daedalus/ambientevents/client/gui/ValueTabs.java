package com.daedalus.ambientevents.client.gui;

import com.daedalus.ambientevents.AmbientEventsRef;
import com.daedalus.ambientevents.ParsingUtils;
import com.daedalus.ambientevents.client.gui.widgets.WTabView;
import com.daedalus.ambientevents.client.gui.widgets.WWidget;
import com.daedalus.ambientevents.wrappers.JSONKeyValuePair;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;
import java.util.Objects;

public class ValueTabs extends WTabView {

	public ValueTabs(WWidget parentIn) {
		super(parentIn);
	}
	
	public void populate(JSONKeyValuePair element) {
		this.clear();
		JsonObject events = ParsingUtils.getAsObject(ConfiguratorGUI.manifestJSON,"events",false);
		if(Objects.isNull(events)) {
			AmbientEventsRef.LOGGER.error("No events in manifest.json");
			return;
		}
		JsonObject sublist = ParsingUtils.getAsObject(events,element.getKey(),false);
		if(Objects.isNull(sublist)) {
			AmbientEventsRef.LOGGER.error(String.format("No %s in manifest.json", element.getKey()));
			return;
		}
		String subName = ParsingUtils.getAsString(element.getJSONObject(),"type",true);
		JsonObject subElement = ParsingUtils.getAsObject(sublist,subName,false);
		if(Objects.isNull(subElement)) {
			AmbientEventsRef.LOGGER.error(String.format("No %s in manifest.json",subName));
			return;
		}
		for(Map.Entry<String, JsonElement> entry : subElement.entrySet())
			this.addTab(new WWidget(this),entry.getKey());
		if(this.isVisible()) this.show();
	}
}
