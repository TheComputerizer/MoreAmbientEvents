package com.daedalus.ambientevents.conditions;

import com.daedalus.ambientevents.wrappers.INumber;
import com.daedalus.ambientevents.wrappers.Wrapper;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Random;

public class ChanceCondition implements ICondition {

	protected INumber chance;

	public ChanceCondition(JsonObject args) throws JsonIOException {
		if(args.has("chance")) this.chance = Wrapper.newNumber(args.get("chance"));
		else throw new JsonIOException("No chance specified");
	}

	@Override
	public boolean isMet(EntityPlayer player) {
		Random rand = player.world.rand;
        return rand.nextDouble()*this.chance.getValue(rand)<1;
    }
}
