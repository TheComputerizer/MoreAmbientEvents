package com.daedalus.ambientevents;

import com.daedalus.ambientevents.actions.IAction;
import com.daedalus.ambientevents.actions.MasterAction;
import com.daedalus.ambientevents.conditions.ICondition;
import com.daedalus.ambientevents.conditions.MasterCondition;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class GenericEvent {

	protected ICondition condition;
	protected IAction action;

	public GenericEvent(JsonElement json) throws JsonIOException {
		this.condition = new MasterCondition(ParsingUtils.getAsArray(json,"conditions"));
		this.action = new MasterAction(ParsingUtils.getAsArray(json,"actions"));
	}

	public GenericEvent(ByteBuf buf) {
		this.condition = new MasterCondition(buf);
		this.action = new MasterAction(buf);
	}

	public void process(EntityPlayer player) {
		if(this.condition.isMet(player)) this.action.execute(player);
	}

	public void sync(ByteBuf buf) {
		this.condition.sync(buf);
		this.action.sync(buf);
	}
}
