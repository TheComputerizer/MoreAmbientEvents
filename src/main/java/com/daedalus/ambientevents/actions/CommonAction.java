package com.daedalus.ambientevents.actions;

import com.daedalus.ambientevents.wrappers.INumber;
import com.daedalus.ambientevents.wrappers.NumberType;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.relauncher.Side;

public abstract class CommonAction implements IAction {

	protected INumber chance;

	protected CommonAction(JsonObject json) throws JsonIOException {
		this.chance = NumberType.tryAutoParse(json,"chance",true);
	}

	protected CommonAction(ByteBuf buf) {
		this.chance = NumberType.sync(buf);
	}

	@Override
	public boolean canExecute(Side side) {
		return side.isServer();
	}



	@Override
	public void sync(ByteBuf buf) {
		this.chance.sync(buf);
	}
}
