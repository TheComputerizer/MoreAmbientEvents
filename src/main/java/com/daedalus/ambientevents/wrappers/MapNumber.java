package com.daedalus.ambientevents.wrappers;

import com.daedalus.ambientevents.ParsingUtils;
import com.daedalus.ambientevents.client.AmbientEventsClient;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Tuple;

import java.util.Objects;
import java.util.Random;

public class MapNumber implements INumber {

	protected Tuple<INumber,INumber> lowPair;
	protected Tuple<INumber,INumber> highPair;
	protected boolean shouldClamp;
	protected IString inputType;

	@Override
	public boolean parse(JsonElement json) throws JsonIOException {
		this.lowPair = new Tuple<>(getWithDefault(json,"inlow"),getWithDefault(json,"outlow"));
		this.highPair = new Tuple<>(getWithDefault(json,"inhigh"),getWithDefault(json,"outhigh"));
		JsonPrimitive primitive = ParsingUtils.getAsPrimitive(json,"clamp");
		this.shouldClamp = Objects.nonNull(primitive) && primitive.getAsBoolean();
		this.inputType = StringType.tryAutoParse(ParsingUtils.getNextElement(json,"type",true));
		return false;
	}

	protected INumber getWithDefault(JsonElement json, String key) {
		JsonElement numberElement = ParsingUtils.getNextElement(json,key);
		return Objects.nonNull(numberElement) ? NumberType.tryAutoParse(numberElement) : new RawNumber(0d);
	}

	@Override
	public Number getValue(Random rand) {
		return clamp(rand,mapOutput(rand,getInputValue(this.inputType.getValue(rand),Minecraft.getMinecraft().player)));
	}

	protected double getInputValue(String type, EntityPlayer player) {
		switch(type) {
			case "playerposx": return player.posX;
			case "playerposy": return player.posY;
			case "playerposz": return player.posZ;
			case "timeofday": return player.world.getWorldTime();
			case "worldtime": return player.world.getTotalWorldTime()/24000d;
			case "timesincesleep": return (player.world.getTotalWorldTime()-AmbientEventsClient.lastSleep)/24000d;
			default: return 0d;
		}
	}

	protected double mapOutput(Random rand, double value) {
		if(value==this.lowPair.getFirst().getValue(rand).doubleValue())
			value = this.lowPair.getSecond().getValue(rand).doubleValue();
		else if(value==this.highPair.getFirst().getValue(rand).doubleValue())
			value = this.lowPair.getSecond().getValue(rand).doubleValue();
		return value;
	}

	protected double clamp(Random rand, double value) {
		if(this.shouldClamp) {
			double lowOut = this.lowPair.getSecond().getValue(rand).doubleValue();
			double highOut = this.highPair.getSecond().getValue(rand).doubleValue();
			if(value<lowOut) value = lowOut;
			else if(value>highOut) value = highOut;
		}
		return value;
	}
}
