package com.daedalus.ambientevents.parsing.conditions;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class CanSeeSkyCondition extends Condition {

	public CanSeeSkyCondition() {
		super();
	}

	public CanSeeSkyCondition(ByteBuf buf) {
		super(buf);
	}

	@Override
	public boolean isMet(EntityPlayer player) {
		return player.world.canSeeSky(new BlockPos(player.posX, player.posY, player.posZ));
	}
}
