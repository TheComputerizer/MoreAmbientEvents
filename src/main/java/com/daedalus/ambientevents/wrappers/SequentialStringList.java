package com.daedalus.ambientevents.wrappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import com.daedalus.ambientevents.ParsingUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import org.apache.commons.lang3.mutable.MutableInt;

public class SequentialStringList implements IString {

	protected final List<IString> values;
	protected final MutableInt indexHolder;

	public SequentialStringList() {
		this.values = new ArrayList<>();
		this.indexHolder = new MutableInt();
	}

	@Override
	public boolean parse(JsonElement json) throws JsonIOException {
		JsonArray textElements = ParsingUtils.getAsArray(json,true);
		if(Objects.isNull(textElements)) return false;
		for(JsonElement element : textElements) {
			IString value = StringType.tryAutoParse(element);
			if(Objects.nonNull(value)) this.values.add(value);
		}
		return !this.values.isEmpty();
	}

	@Override
	public String getValue(Random rand) {
		String value = this.values.get(this.indexHolder.getAndAdd(1)).getValue(rand);
		if(this.indexHolder.getValue()>=this.values.size()) this.indexHolder.setValue(0);
		return value;
	}
}
