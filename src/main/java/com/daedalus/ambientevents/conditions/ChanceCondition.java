package com.daedalus.ambientevents.conditions;

import com.daedalus.ambientevents.wrappers.INumber;

import com.daedalus.ambientevents.wrappers.NumberType;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Random;

public class ChanceCondition implements ICondition {

	protected INumber chance;

	public ChanceCondition(JsonObject json) throws JsonIOException {
		this.chance = NumberType.tryAutoParse(json,"chance",true);
	}

	@Override
	public boolean isMet(EntityPlayer player) {
		Random rand = player.world.rand;
        return rand.nextDouble()*this.chance.getValue(rand).doubleValue()<1d;
    }
}
