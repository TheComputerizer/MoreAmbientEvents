package com.daedalus.ambientevents.wrappers;

import com.daedalus.ambientevents.ParsingUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import net.minecraft.util.WeightedRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class RandomNumberList implements INumber {

	protected final List<WeightedValue<INumber>> values;

	public RandomNumberList() {
		this.values = new ArrayList<>();
	}

	@Override
	public boolean parse(JsonElement json) throws JsonIOException {
		JsonArray textElements = ParsingUtils.getAsArray(json,true);
		if(Objects.isNull(textElements)) return false;
		for(JsonElement element : textElements) {
			INumber value = NumberType.tryAutoParse(ParsingUtils.getNextElement(element,"value",true));
			if(Objects.nonNull(value)) {
				Number weight = ParsingUtils.getAsNumber(element,"weight",true);
				if(Objects.nonNull(weight)) this.values.add(new WeightedValue<>(value,weight.intValue()));
			}
		}
		return !this.values.isEmpty();
	}

	@Override
	public Number getValue(Random rand) {
		return WeightedRandom.getRandomItem(rand,this.values).getValue().getValue(rand);
	}
}
