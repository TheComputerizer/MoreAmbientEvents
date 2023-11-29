package com.daedalus.ambientevents.parsing.numbers;

import com.daedalus.ambientevents.AmbientEvents;
import com.daedalus.ambientevents.parsing.ParsingUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.theimpossiblelibrary.util.NetworkUtil;

import java.util.Objects;
import java.util.Random;

public class RandomNumber implements INumber {

	protected INumber max;
	protected INumber min;

	public RandomNumber() {}

	public RandomNumber(ByteBuf buf) {
		this.max = NumberType.sync(buf);
		this.min = NumberType.sync(buf);
	}

	@Override
	public boolean parse(JsonElement json) throws JsonIOException {
		this.max = NumberType.tryAutoParse(ParsingUtils.getNextElement(json,"max",true));
		if(Objects.nonNull(this.max)) {
			this.min = NumberType.tryAutoParse(ParsingUtils.getNextElement(json,"min",true));
			if(Objects.isNull(this.min)) this.min = new RawNumber(0d);
			Random rand = new Random();
			Number maxVal = this.max.getValue(rand);
			Number minVal = this.min.getValue(rand);
			int compare = ParsingUtils.compareGeneric(maxVal,minVal);
			if(compare<=0) {
				AmbientEvents.logError("Random number maximum of {} is not allowed to be less than or "+
						"equal to minimum {}!",maxVal,minVal);
				return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public Number getValue(Random rand) {
		double upperBound = this.max.getValue(rand).doubleValue();
		double lowerBound = this.min.getValue(rand).doubleValue();
		return rand.nextDouble()*(upperBound-lowerBound)+lowerBound;
	}

	@Override
	public void sync(ByteBuf buf) {
		NetworkUtil.writeString(buf,"random");
		this.max.sync(buf);
		this.min.sync(buf);
	}
}
