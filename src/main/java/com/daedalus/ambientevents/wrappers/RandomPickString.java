package com.daedalus.ambientevents.wrappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

public class RandomPickString implements IString {

	protected final List<WeightedString> values;
	protected double total;

	public RandomPickString(JsonObject args) throws JsonIOException {
		this.values = new ArrayList<>();
		if(args.has("text")) {
			JsonElement text = args.get("text");
			if(text instanceof JsonArray) {
				for(JsonElement element : ((JsonArray)text)) {
					if(element instanceof JsonObject) {
						JsonObject string = (JsonObject)element;
						WeightedString ws = new WeightedString();
						if(string.has("string")) ws.string = string.get("string").getAsString();
						else throw new JsonIOException("No string specified");
						if(string.has("weight")) ws.weight = string.get("weight").getAsDouble();
						else ws.weight = 1;
						this.values.add(ws);
					} else this.values.add(new WeightedString(element.getAsString(),1d));
				}
			} else throw new JsonIOException("Unrecognized text value specified");
		} else throw new JsonIOException("No text specified");
        for(WeightedString value : this.values) this.total+=value.weight;
	}

	@Override
	public String getValue(Random rand) {
		double test = rand.nextDouble()*this.total;
		double subtotal = 0;
        for(WeightedString value : this.values) {
            subtotal+=value.weight;
            if(test<subtotal) return value.string;
        }

		return "";
	}

	protected static class WeightedString {

		public String string;
		public double weight;

		public WeightedString() {}

		public WeightedString(String stringIn, double weightIn) {
			this.string = stringIn;
			this.weight = weightIn;
		}
	}
}
