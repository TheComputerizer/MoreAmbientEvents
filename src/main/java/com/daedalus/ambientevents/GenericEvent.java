package com.daedalus.ambientevents;

import com.daedalus.ambientevents.actions.IAction;
import com.daedalus.ambientevents.actions.MasterAction;
import com.daedalus.ambientevents.conditions.ICondition;
import com.daedalus.ambientevents.conditions.MasterCondition;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.EntityPlayer;

public class GenericEvent {

	protected ICondition condition;
	protected IAction action;

	public GenericEvent(JsonObject args) throws JsonIOException {
		this.condition = new MasterCondition(args.getAsJsonArray("conditions"));
		this.action = new MasterAction(args.getAsJsonArray("actions"));
	}

	public void process(EntityPlayer player) {
		if (this.condition.isMet(player)) {
			this.action.execute(player);
		}
	}
}
