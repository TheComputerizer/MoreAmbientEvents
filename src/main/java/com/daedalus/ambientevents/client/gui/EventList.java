package com.daedalus.ambientevents.client.gui;

import java.util.*;
import java.util.function.Consumer;

import com.daedalus.ambientevents.ParsingUtils;
import com.daedalus.ambientevents.client.gui.widgets.*;
import com.daedalus.ambientevents.wrappers.JSONKeyValuePair;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class EventList extends WWidget {

	protected Consumer<JsonObject> selectedCallback;
	protected Consumer<Integer> refreshCallback;
	protected WListView<JSONKeyValuePair> listView;
	protected WVanillaTextField nameField;
	protected WPushButton newButton;
	protected WPushButton duplicateButton;
	protected WPushButton deleteButton;
	
	protected WListElement<JSONKeyValuePair> selected;
	protected int padding = 2;
	public static int eventCounter = 0;
	
	public EventList(WWidget parent) {
		super(parent);
		this.listView = new WListView<>(this);
		this.listView.setOnElementSelectedAction(this::eventSelected);
		this.nameField = new WVanillaTextField(this);
		this.nameField.setText("Event Name");
		this.nameField.setOnTextChangedAction(this::renameEvent);
		this.nameField.setValidRegex("[\\w ]*");
		this.newButton = new WPushButton(this, "New");
		this.newButton.setOnClickAction(this::newEvent);
		this.duplicateButton = new WPushButton(this, "Duplicate");
		this.duplicateButton.setOnClickAction(this::duplicateEvent);
		this.deleteButton = new WPushButton(this, "Delete");
		this.deleteButton.setOnClickAction(this::deleteSelected);
		for(Map.Entry<String,JsonElement> entry : ConfiguratorGUI.eventsJSON.entrySet()) {
			JSONKeyValuePair pair = new JSONKeyValuePair(entry.getKey(),ParsingUtils.getAsObject(entry.getValue()));
			this.listView.add(new WListElement<>(entry.getKey(),pair));
		}
	}
	
	@Override
	public void setSize(int width, int height) {
		super.setSize(width,height);
		int textHeight = this.fontRenderer.FONT_HEIGHT+this.padding*2;
		int sizeX = this.width-this.padding*2;
		this.nameField.setSize(width-this.padding*3,textHeight);
		this.nameField.move(this.padding+1,this.padding+1);
		this.newButton.setSize((this.width-this.padding*2)/3,textHeight);
		this.newButton.move(this.padding,this.padding*2+textHeight);
		this.duplicateButton.setSize(this.newButton.width,this.newButton.height);
		this.duplicateButton.move(this.newButton.width+this.newButton.offsetX,this.newButton.offsetY);
		this.deleteButton.setSize(sizeX-2*this.newButton.width,this.newButton.height);
		this.deleteButton.move(this.duplicateButton.width+this.duplicateButton.offsetX,this.newButton.offsetY);
		this.listView.setSize(this.width-this.padding*2,height-this.newButton.height-this.newButton.offsetY-this.padding);
		this.listView.move(this.padding,textHeight*2+this.padding*2);
	}
	
	@Override
	public void draw(int mouseX, int mouseY, float partialTicks) {
		drawRect(0,0,this.width,this.height,this.palette.edging);
		drawRect(this.padding,this.padding,this.width-this.padding,this.height-this.padding,this.palette.primary);
		super.draw(mouseX, mouseY, partialTicks);
	}
	
	public void newEvent(int mouseButton) {
		String newName = String.format("UnnamedEvent%d", eventCounter++);
		List<JSONKeyValuePair> pairs = new ArrayList<>();
		JsonObject events = ParsingUtils.getAsObject(ConfiguratorGUI.manifestJSON,"events",true);
		if(Objects.nonNull(events))
			for(Map.Entry<String,JsonElement> entry : events.entrySet())
				pairs.add(new JSONKeyValuePair(entry.getKey(),new JsonArray()));
		JsonObject newEvent = new JsonObject();
        for(JSONKeyValuePair pair : pairs) newEvent.add(pair.getKey(),pair.getJSONArray());
		ConfiguratorGUI.eventsJSON.add(newName,newEvent);
		this.listView.add(new WListElement<>(newName,new JSONKeyValuePair(newName,newEvent)));
	}
	
	public void duplicateEvent(int mouseButton) {
		if(Objects.isNull(this.selected)) return;
		String newName = String.format(this.selected.text + "%d", eventCounter++);
		JSONKeyValuePair newPair = new JSONKeyValuePair(newName, copyJSONObject(this.selected.item.getJSONObject()));
		WListElement<JSONKeyValuePair> newElement = new WListElement<>(newName, newPair);
		ConfiguratorGUI.eventsJSON.add(newName,newElement.item.getJSONObject());
		this.listView.add(newElement);
	}
	
	public void deleteSelected(int mouseButton) {
		if(Objects.isNull(this.selected)) return;
		int index = this.listView.getIndex(this.selected);
		this.listView.remove(this.selected);
		ConfiguratorGUI.eventsJSON.remove(this.selected.text);
		if(index>=this.listView.getSize()) index = this.listView.getSize()-1;
		this.listView.select(index);
		if(index<0) this.eventSelected(null);
	}
	
	public void renameEvent(String newName) {
		if(Objects.isNull(this.selected) || this.listView.contains(newName)) return;
		int index = this.listView.getIndex(this.selected);
		ConfiguratorGUI.eventsJSON.remove(this.selected.item.getKey());
		this.listView.remove(this.selected);
		this.selected.item.setKey(newName);
		WListElement<JSONKeyValuePair> newElement = new WListElement<>(newName,this.selected.item);
		ConfiguratorGUI.eventsJSON.add(newName,newElement.item.getJSONObject());
		this.listView.add(newElement,index);
		this.selected = newElement;
	}
	
	public void rePopulate() {
		int index = this.listView.getIndex(this.selected);
		this.listView.clear();
		for(Map.Entry<String,JsonElement> entry : ConfiguratorGUI.eventsJSON.entrySet()) {
			JSONKeyValuePair pair = new JSONKeyValuePair(entry.getKey(),ParsingUtils.getAsObject(entry.getValue()));
			this.listView.add(new WListElement<>(entry.getKey(),pair));
		}
		this.listView.select(index);
	}
	
	public void setOnEventSelectedAction(Consumer<JsonObject> onEventSelectedAction) {
		this.selectedCallback = onEventSelectedAction;
	}
	
	protected void eventSelected(WListElement<JSONKeyValuePair> selectedElement) {
		this.selected = selectedElement;
		if(Objects.nonNull(this.selected)) {
			this.nameField.setText(this.selected.item.getKey());
			this.onEventSelected(selectedElement.item.getJSONObject());
		} else {
			this.nameField.setText("Event Name");
			this.onEventSelected(null);
		}
	}

	public void onEventSelected(JsonObject selectedElement) {
		if(Objects.nonNull(this.selectedCallback)) this.selectedCallback.accept(selectedElement);
	}
}
