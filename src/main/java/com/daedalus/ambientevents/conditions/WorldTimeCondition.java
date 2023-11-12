package com.daedalus.ambientevents.conditions;

import com.daedalus.ambientevents.comparisons.NumericComparison;
import com.daedalus.ambientevents.wrappers.INumber;
import com.daedalus.ambientevents.wrappers.Wrapper;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Random;

public class WorldTimeCondition implements ICondition {

	protected NumericComparison comparison;
	protected INumber checkValue;

	public WorldTimeCondition(JsonObject args) throws JsonIOException {
		if(args.has("comparison"))
			this.comparison = new NumericComparison(Wrapper.newString(args.get("comparison")));
		else throw new JsonIOException("No comparison specified");
		if(args.has("value")) this.checkValue = Wrapper.newNumber(args.get("value"));
		else throw new JsonIOException("No value specified");
	}

	@Override
	public boolean isMet(EntityPlayer player) {
		Random rand = player.world.rand;
        return this.comparison.compare(rand,player.world.getTotalWorldTime(),this.checkValue.getValue(rand)*24000);
    }

}
