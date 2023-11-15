package com.daedalus.ambientevents.wrappers;

import com.daedalus.ambientevents.ParsingUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;

import java.util.Objects;
import java.util.Random;

public class RawString implements IString {

	protected String value;

	public RawString() {}

	public RawString(String value) {
		this.value = value;
	}

	@Override
	public boolean parse(JsonElement json) throws JsonIOException {
		this.value = ParsingUtils.getAsString(json);
		return Objects.nonNull(this.value);
	}

	@Override
	public String getValue(Random rand) {
		return this.value;
	}
}
