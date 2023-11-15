package com.daedalus.ambientevents.actions;

import com.daedalus.ambientevents.wrappers.INumber;
import com.daedalus.ambientevents.wrappers.NumberType;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.EntityPlayer;

public class CommonAction implements IAction {

	protected INumber chance;

	public CommonAction(JsonObject json) throws JsonIOException {
		this.chance = NumberType.tryAutoParse(json,"chance",true);
	}

	@Override
	public void execute(EntityPlayer player) {}
}
