package com.daedalus.ambientevents.wrappers;

import com.daedalus.ambientevents.client.AmbientEventsClient;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Random;

public class MapNumber implements INumber {

	protected double inLow;
	protected double inHigh;
	protected double outLow;
	protected double outHigh;
	protected boolean clamp;
	protected String input;

	public MapNumber(JsonObject args) throws JsonIOException {
		if(args.has("input")) this.input = args.get("input").getAsString();
		else throw new JsonIOException("No input specified");
		if(args.has("inlow")) this.inLow = args.get("inlow").getAsDouble();
		else this.inLow = 0;
		if(args.has("inhigh")) this.inHigh = args.get("inhigh").getAsDouble();
		else throw new JsonIOException("No input high specified");
		if(args.has("outlow")) this.outLow = args.get("outlow").getAsDouble();
		else this.outLow = 0;
		if(args.has("outhigh")) this.outHigh = args.get("outhigh").getAsDouble();
		else throw new JsonIOException("No output high specified");
		if(args.has("clamp")) this.clamp = args.get("clamp").getAsString().equals("true");
		else this.clamp = false;
	}

	@Override
	public double getValue(Random rand) {
		double value = 0;
		EntityPlayer player = Minecraft.getMinecraft().player;
		switch (this.input) {
			case "playerposx":
				value = player.posX;
				break;
			case "playerposy":
				value = player.posY;
				break;
			case "playerposz":
				value = player.posZ;
				break;
			case "timeofday":
				value = player.world.getWorldTime();
				break;
			case "worldtime":
				value = player.world.getTotalWorldTime()/24000d;
				break;
			case "timesincesleep":
				value = (player.world.getTotalWorldTime() -AmbientEventsClient.lastSleep)/24000d;
		}

		double output = (value - this.inLow) / (this.inHigh - this.inLow) * (this.outHigh - this.outLow) + this.outLow;

		if(this.clamp) {
			if(this.inHigh > this.inLow) {
				if(value > this.inHigh) {
					output = this.outHigh;
				} else if(value < this.inLow) {
					output = this.outLow;
				}
			} else {
				if(value < this.inHigh) {
					output = this.outHigh;
				} else if(value > this.inLow) {
					output = this.outLow;
				}
			}
		}
		return output;
	}
}
