package com.daedalus.ambientevents.gui;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

import com.daedalus.ambientevents.gui.widgets.*;
import com.daedalus.ambientevents.wrappers.JSONKeyValuePair;
import com.google.gson.JsonObject;

public class SubLists extends WTabView {
	
	protected Consumer<JSONKeyValuePair> selectedCallback;
	
	public SubLists(WWidget parent) {
		super(parent);
		Iterator<String> keys = ConfiguratorGUI.manifestJSON.getJSONObject("events").keys();
		while(keys.hasNext()) {
			String key = keys.next();
			SubListEditor subListEditor = new SubListEditor(this, key);
			subListEditor.setOnElementSelectedAction(this::elementSelected);
			this.addTab(subListEditor, key);
		}
	}
	
	public void populate(JsonObject event) {
		if(Objects.isNull(event)) {
			for(WWidget subList : this.widgets)
				((SubListEditor)subList).clear();
			return;
		}
		for (int i = 0; i < this.labels.size(); i++)
			if (event.has(this.labels.get(i).getLabel()))
				((SubListEditor)this.widgets.get(i)).populate(event.getJSONArray(this.labels.get(i).getLabel()));
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
