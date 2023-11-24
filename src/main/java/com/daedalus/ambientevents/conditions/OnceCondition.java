package com.daedalus.ambientevents.conditions;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class OnceCondition implements ICondition {

	protected boolean fired = false;

	public OnceCondition() {}

	public OnceCondition(ByteBuf buf) {
		this.fired = buf.readBoolean();
	}

	@Override
	public boolean isMet(EntityPlayer player) {
		if(this.fired) return false;
		this.fired = true;
		return true;
	}

	@Override
	public void sync(ByteBuf buf) {
		buf.writeBoolean(this.fired);
	}
}
