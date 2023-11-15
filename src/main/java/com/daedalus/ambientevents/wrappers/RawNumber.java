package com.daedalus.ambientevents.wrappers;

import com.daedalus.ambientevents.ParsingUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;

import java.util.Objects;
import java.util.Random;

public class RawNumber implements INumber {

	protected Number value;

	public RawNumber() {}

	public RawNumber(Number value) {
		this.value = value;
	}

	@Override
	public boolean parse(JsonElement json) throws JsonIOException {
		this.value = ParsingUtils.getAsNumber(json,true);
		return Objects.nonNull(this.value);
	}

	@Override
	public Number getValue(Random rand) {
		return this.value;
	}
}
