package com.daedalus.ambientevents.parsing.conditions;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class AlwaysTrueCondition extends Condition {

	public AlwaysTrueCondition() {
		super();
	}

	public AlwaysTrueCondition(ByteBuf buf) {
		super(buf);
	}

	@Override
	public boolean isMet(EntityPlayer player) {
		return true;
	}
}
