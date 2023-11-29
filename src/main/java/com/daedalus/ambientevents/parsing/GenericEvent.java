package com.daedalus.ambientevents.parsing;

import com.daedalus.ambientevents.parsing.actions.MasterAction;
import com.daedalus.ambientevents.parsing.conditions.MasterCondition;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

public class GenericEvent {

	protected MasterCondition conditionManager;
	protected MasterAction actionManager;

	public GenericEvent(JsonElement json) throws JsonIOException {
		this.conditionManager = new MasterCondition(ParsingUtils.getAsArray(json,"conditions"));
		this.actionManager = new MasterAction(ParsingUtils.getAsArray(json,"actions"));
	}

	public GenericEvent(ByteBuf buf) {
		this.conditionManager = new MasterCondition(buf);
		this.actionManager = new MasterAction(buf);
	}

	public boolean hasSideActions(Side side) {
		return this.actionManager.hasSidedActions(side);
	}

	public void process(EntityPlayer player) {
		if(this.conditionManager.isMet(player)) this.actionManager.execute(player);
	}

	public void sync(ByteBuf buf) {
		this.conditionManager.sync(buf);
		this.actionManager.sync(buf);
	}
}
