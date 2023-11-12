package com.daedalus.ambientevents.conditions;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import com.daedalus.ambientevents.comparisons.NumericComparison;
import com.daedalus.ambientevents.wrappers.INumber;
import com.daedalus.ambientevents.wrappers.IString;
import com.daedalus.ambientevents.wrappers.Wrapper;

import net.minecraft.entity.player.EntityPlayer;

import java.util.Random;

public class PlayerPosCondition implements ICondition {

	protected IString dimension;
	protected NumericComparison comparison;
	protected INumber compareValue;

	public PlayerPosCondition(JsonObject args) throws JsonIOException {
		if(args.has("dimension")) this.dimension = Wrapper.newString(args.get("dimension"));
		else throw new JsonIOException("No dimension specified");
		if(args.has("comparison")) this.comparison = new NumericComparison(Wrapper.newString(args.get("comparison")));
		else throw new JsonIOException("No comparison specified");
		if(args.has("value")) this.compareValue = Wrapper.newNumber(args.get("value"));
		else throw new JsonIOException("No value specified");
	}

	@Override
	public boolean isMet(EntityPlayer player) {
		double playerValue;
		Random rand = player.world.rand;
		switch(this.dimension.getValue(rand)) {
		case "x":
			playerValue = player.posX;
			break;
		case "y":
			playerValue = player.posY;
			break;
		case "z":
			playerValue = player.posZ;
			break;
		default: return false;
		}
		return this.comparison.compare(rand,playerValue,this.compareValue.getValue(rand));
	}
}
