package com.daedalus.ambientevents.actions;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import com.daedalus.ambientevents.wrappers.INumber;
import com.daedalus.ambientevents.wrappers.RandomNumber;
import com.daedalus.ambientevents.wrappers.Wrapper;

import net.minecraft.entity.player.EntityPlayer;

public class CommonAction implements IAction {

	protected INumber chance;

	public CommonAction(JsonObject args) throws JsonIOException {
		if(args.has("chance")) this.chance = new RandomNumber(0,args.get("chance").getAsDouble());
		else this.chance = Wrapper.newNumber(0);
	}

	@Override
	public void execute(EntityPlayer player) {}
}
