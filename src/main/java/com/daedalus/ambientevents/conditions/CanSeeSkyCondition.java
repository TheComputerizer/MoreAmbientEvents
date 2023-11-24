package com.daedalus.ambientevents.conditions;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class CanSeeSkyCondition implements ICondition {

	public CanSeeSkyCondition() {}

	@Override
	public boolean isMet(EntityPlayer player) {
		return player.world.canSeeSky(new BlockPos(player.posX, player.posY, player.posZ));
	}

	@Override
	public void sync(ByteBuf buf) {}
}
