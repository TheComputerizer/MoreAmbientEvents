package com.daedalus.ambientevents.wrappers;

import java.util.Random;

public class RawString implements IString {

	protected String value;

	public RawString(String valueIn) {
		this.value = valueIn;
	}

	@Override
	public String getValue(Random rand) {
		return this.value;
	}
}
