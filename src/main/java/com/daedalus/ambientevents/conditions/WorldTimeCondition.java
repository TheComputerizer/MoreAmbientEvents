package com.daedalus.ambientevents.conditions;

import com.daedalus.ambientevents.comparisons.NumericComparison;
import com.daedalus.ambientevents.wrappers.INumber;

import com.daedalus.ambientevents.wrappers.NumberType;
import com.daedalus.ambientevents.wrappers.StringType;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Random;

public class WorldTimeCondition implements ICondition {

	protected NumericComparison comparison;
	protected INumber checkValue;

	public WorldTimeCondition(JsonObject json) throws JsonIOException {
		this.comparison = new NumericComparison(StringType.tryAutoParse(json,"comparison",true));
		this.checkValue = NumberType.tryAutoParse(json,"value",true);
	}

	@Override
	public boolean isMet(EntityPlayer player) {
		Random rand = player.world.rand;
        return this.comparison.compare(rand,player.world.getTotalWorldTime(),
				this.checkValue.getValue(rand).doubleValue()*24000);
    }

}
