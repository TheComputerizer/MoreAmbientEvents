package com.daedalus.ambientevents.parsing.numbers;

import com.daedalus.ambientevents.capability.CapabilityHandler;
import com.daedalus.ambientevents.client.ClientEventManager;
import com.daedalus.ambientevents.parsing.ParsingUtils;
import com.daedalus.ambientevents.parsing.strings.IString;
import com.daedalus.ambientevents.parsing.strings.StringType;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
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

	public MapNumber() {}

	public MapNumber(ByteBuf buf) {
		this.lowPair = new Tuple<>(NumberType.sync(buf),NumberType.sync(buf));
		this.highPair = new Tuple<>(NumberType.sync(buf),NumberType.sync(buf));
		this.shouldClamp = buf.readBoolean();
		this.inputType = StringType.sync(buf);
	}

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
			case "timesincesleep": return CapabilityHandler.getLastSleep(player)/24000d;
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

	@Override
	public void sync(ByteBuf buf) {
		syncTuple(buf,this.lowPair);
		syncTuple(buf,this.highPair);
		buf.writeBoolean(this.shouldClamp);
		this.inputType.sync(buf);
	}

	private void syncTuple(ByteBuf buf, Tuple<INumber,INumber> tuple) {
		tuple.getFirst().sync(buf);
		tuple.getSecond().sync(buf);
	}
}
