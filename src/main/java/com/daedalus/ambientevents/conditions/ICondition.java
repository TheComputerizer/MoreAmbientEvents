package com.daedalus.ambientevents.conditions;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public interface ICondition {

	boolean isMet(EntityPlayer player);

	void sync(ByteBuf buf);
}
