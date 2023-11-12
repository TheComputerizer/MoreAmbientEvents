package com.daedalus.ambientevents.wrappers;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import java.util.Random;

public class RandomNumber implements INumber {

	private double upperBound = 0;
	private double lowerBound = 0;

	public RandomNumber(JsonObject args) throws JsonIOException {
		if(args.has("upperbound")) this.upperBound = args.get("upperbound").getAsDouble();
		if(args.has("lowerbound")) this.lowerBound = args.get("lowerbound").getAsDouble();
		if(this.upperBound==this.lowerBound) throw new JsonIOException("Invalid bounds for random number");
	}

	public RandomNumber(double lowerIn, double upperIn) {
		this.lowerBound = lowerIn;
		this.upperBound = upperIn;
	}

	@Override
	public double getValue(Random rand) {
		return rand.nextDouble()*(this.upperBound-this.lowerBound)+this.lowerBound;
	}

}
