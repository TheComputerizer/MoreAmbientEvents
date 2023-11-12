package com.daedalus.ambientevents.wrappers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomPickNumber implements INumber {

	protected final List<WeightedNumber> values;
	protected double total;

	public RandomPickNumber(JsonObject args) throws JsonIOException {
		this.values = new ArrayList<>();
		if(args.has("value")) {
			JsonElement value = args.get("value");
			if(value instanceof JsonArray) {
				for(JsonElement element : ((JsonArray)value)) {
					if(element instanceof JsonObject) {
						JsonObject number = (JsonObject)element;
						WeightedNumber wn = new WeightedNumber();
						if(number.has("string")) wn.number = number.get("number").getAsDouble();
						else throw new JsonIOException("No string specified");
						if(number.has("weight")) wn.weight = number.get("weight").getAsDouble();
						else wn.weight = 1;
						this.values.add(wn);
					} else this.values.add(new WeightedNumber(element.getAsDouble(),1d));
				}
			} else throw new JsonIOException("Unrecognized text value specified");
		} else throw new JsonIOException("No text specified");
        for(WeightedNumber value : this.values) this.total+=value.weight;
	}

	@Override
	public double getValue(Random rand) {
		double test = rand.nextDouble()*this.total;
		double subtotal = 0;
        for(WeightedNumber value : this.values) {
            subtotal+=value.weight;
            if(test<subtotal) return value.number;
        }

		return 0;
	}

	protected static class WeightedNumber {
		public double number;
		public double weight;

		public WeightedNumber() {}

		public WeightedNumber(double numberIn, double weightIn) {
			this.number = numberIn;
			this.weight = weightIn;
		}
	}
}
