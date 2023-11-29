package com.daedalus.ambientevents.parsing.conditions;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class OnceCondition extends Condition {

	protected boolean fired;

	public OnceCondition() {
		super();
		this.fired = false;
	}

	public OnceCondition(ByteBuf buf) {
		super(buf);
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
		super.sync(buf);
		buf.writeBoolean(this.fired);
	}
}
