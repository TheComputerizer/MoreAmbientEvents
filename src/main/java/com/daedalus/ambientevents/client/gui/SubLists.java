package com.daedalus.ambientevents.client.gui;

import com.daedalus.ambientevents.parsing.ParsingUtils;
import com.daedalus.ambientevents.client.gui.widgets.WTabLabel;
import com.daedalus.ambientevents.client.gui.widgets.WTabView;
import com.daedalus.ambientevents.client.gui.widgets.WWidget;
import com.daedalus.ambientevents.parsing.JSONKeyValuePair;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class SubLists extends WTabView {
	
	protected Consumer<JSONKeyValuePair> selectedCallback;
	
	public SubLists(WWidget parent) {
		super(parent);
		JsonObject events = ParsingUtils.getAsObject(ConfiguratorGUI.manifestJSON,"events",true);
		if(Objects.nonNull(events)) {
			for(Map.Entry<String, JsonElement> entry : events.entrySet()) {
				SubListEditor subListEditor = new SubListEditor(this,entry.getKey());
				subListEditor.setOnElementSelectedAction(this::elementSelected);
				this.addTab(subListEditor,entry.getKey());
			}
		}
	}
	
	public void populate(JsonObject event) {
		if(Objects.isNull(event)) {
			for(WWidget subList : this.widgets)
				((SubListEditor)subList).clear();
			return;
		}
		for(WTabLabel label : this.labels) {
			String labelName = label.getLabel();
			if(event.has(labelName)) {
				JsonArray array = ParsingUtils.getAsArray(event,labelName);
				if(Objects.nonNull(array))
					((SubListEditor)this.widgets.get(this.labels.indexOf(label))).populate(array);
			}
		}
	}
	
	public void elementSelected(JSONKeyValuePair element) {
		for (int i = 0; i < this.labels.size(); i++)
			if (!labels.get(i).isSelected())
				((SubListEditor) this.widgets.get(i)).deselect();
		if(Objects.nonNull(this.selectedCallback)) this.selectedCallback.accept(element);
	}
	
	public void setOnElementSelectedAction(Consumer<JSONKeyValuePair> onElementSelectedAction) {
		this.selectedCallback = onElementSelectedAction;
	}
}
