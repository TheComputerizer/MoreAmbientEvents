package com.daedalus.ambientevents.wrappers;

import java.util.Random;

public class RawNumber implements INumber {

	protected double value;

	public RawNumber(double valueIn) {
		this.value = valueIn;
	}

	@Override
	public double getValue(Random rand) {
		return this.value;
	}
}
