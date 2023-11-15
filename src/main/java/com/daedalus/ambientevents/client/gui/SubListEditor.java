package com.daedalus.ambientevents.client.gui;

import com.daedalus.ambientevents.ParsingUtils;
import com.daedalus.ambientevents.client.gui.widgets.*;
import com.daedalus.ambientevents.wrappers.JSONKeyValuePair;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class SubListEditor extends WWidget {
	
	protected Consumer<JSONKeyValuePair> selectedCallback;

	protected WPushButton newButton;
	protected WPushButton duplicateButton;
	protected WPushButton deleteButton;
	protected WDropDownMenu<JsonElement> dropMenu;
	
	protected WListView<JsonObject> listView;
	protected JsonArray array;
	protected String type;
	
	protected WListElement<JsonObject> selected;
	
	protected int padding = 2;
	
	public SubListEditor(WWidget parent, String type) {
		super(parent);
		this.type = type;
		this.listView = new WListView<>(this);
		this.listView.setOnElementSelectedAction(this::elementSelected);
		this.newButton = new WPushButton(this, "New");
		this.newButton.setOnClickAction(this::newElement);
		this.duplicateButton = new WPushButton(this, "Duplicate");
		this.duplicateButton.setOnClickAction(this::duplicateElement);
		this.deleteButton = new WPushButton(this, "Delete");
		this.deleteButton.setOnClickAction(this::deleteSelected);
		this.dropMenu = new WDropDownMenu<>(this);
		JsonObject events = ParsingUtils.getAsObject(ConfiguratorGUI.manifestJSON,"events",true);
		if(Objects.nonNull(events))
			for(Map.Entry<String, JsonElement> entry : events.entrySet())
				this.dropMenu.add(new WListElement<>(entry.getKey(),entry.getValue()));
	}
	
	@Override
	public void setSize(int width, int height) {
		super.setSize(width,height);
		int textHeight = this.fontRenderer.FONT_HEIGHT+this.padding*2;
		int sizeX = this.width-this.padding;
		this.newButton.setSize((this.width-this.padding)/3,textHeight);
		this.newButton.move(this.padding,0);
		this.duplicateButton.setSize(this.newButton.width,this.newButton.height);
		this.duplicateButton.move(this.newButton.width+this.newButton.offsetX,this.newButton.offsetY);
		this.deleteButton.setSize(sizeX-2*this.newButton.width,this.newButton.height);
		this.deleteButton.move(this.duplicateButton.width+this.duplicateButton.offsetX,this.newButton.offsetY);
		this.dropMenu.setSize(sizeX-this.listView.scrollBarWidth,textHeight);
		this.dropMenu.move(this.padding,this.newButton.height);
		this.listView.setSize(sizeX,this.height-this.dropMenu.height-this.dropMenu.offsetY);
		this.listView.move(this.padding,this.dropMenu.height+this.dropMenu.offsetY);
		this.dropMenu.setMaxMenuLength(this.listView.height);
	}
	
	public void setOnElementSelectedAction(Consumer<JSONKeyValuePair> onElementSelectedAction) {
		this.selectedCallback = onElementSelectedAction;
	}
	
	public void elementSelected(WListElement<JsonObject> element) {
		this.selected = element;
		if(Objects.nonNull(this.selectedCallback))
			this.selectedCallback.accept(new JSONKeyValuePair(this.type,element.item));
	}
	
	public void newElement(int mouseButton) {
		if(Objects.isNull(this.array)) return;
		JsonObject newJSON = new JsonObject();
		newJSON.add("type",this.dropMenu.getSelected().item);
		this.array.add(newJSON);
		this.listView.add(new WListElement<>(this.dropMenu.getSelected().text,newJSON));
	}
	
	public void duplicateElement(int mouseButton) {
		if(Objects.isNull(this.selected)) return;
		JsonObject newJSON = copyJSONObject(this.selected.item);
		this.array.add(newJSON);
		WListElement<JsonObject> newElement = new WListElement<>(ParsingUtils.getAsString(newJSON,"type",true),newJSON);
		this.listView.add(newElement);
	}
	
	public void deleteSelected(int mouseButton) {
		if(Objects.isNull(this.selected)) return;
		int index = this.listView.getIndex(this.selected);
		this.array.remove(index);
		this.listView.remove(this.selected);
		if(index>=this.listView.getSize()) index = this.listView.getSize()-1;
		this.listView.select(index);
	}
	
	public void deselect() {
		this.listView.deselect();
		this.selected = null;
	}
	
	public void clear() {
		this.deselect();
		this.listView.clear();
	}
	
	public void populate(JsonArray array) {
		this.array = array;
		this.listView.clear();
		for(JsonElement element : array) {
			String type = ParsingUtils.getAsString(element,"type",true);
			if(Objects.nonNull(type)) {
				this.listView.add(new WListElement<>(type,ParsingUtils.getAsObject(element)));
			}
		}
	}
}
