package com.daedalus.ambientevents.wrappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

public class SequentialPickString implements IString {

	protected final List<String> values;
	protected int index;
	protected int max;

	public SequentialPickString(JsonObject args) throws JsonIOException {
		this.values = new ArrayList<>();
		if(args.has("text")) {
			JsonElement text = args.get("text");
			if(text instanceof JsonArray) {
				JsonArray array = (JsonArray)text;
				this.max = array.size();
				for(JsonElement element : array) this.values.add(element.getAsString());
			} else throw new JsonIOException("Unrecognized text value specified");
		} else throw new JsonIOException("No text specified");
	}

	@Override
	public String getValue(Random rand) {
		if(this.index == this.max) this.index = 0;
		return this.values.get(this.index++);
	}
}
