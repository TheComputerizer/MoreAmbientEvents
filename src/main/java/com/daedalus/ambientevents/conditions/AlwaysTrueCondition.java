package com.daedalus.ambientevents.conditions;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class AlwaysTrueCondition implements ICondition {

	public AlwaysTrueCondition() {}

	@Override
	public boolean isMet(EntityPlayer player) {
		return true;
	}

	@Override
	public void sync(ByteBuf buf) {}
}
