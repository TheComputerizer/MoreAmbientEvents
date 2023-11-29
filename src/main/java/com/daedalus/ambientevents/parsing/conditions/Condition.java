package com.daedalus.ambientevents.parsing.conditions;

import com.daedalus.ambientevents.parsing.WrapperHolder;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public abstract class Condition extends WrapperHolder {

	protected Condition() {
		super();
	}

	protected Condition(ByteBuf buf) {
		super(buf);
	}

	abstract boolean isMet(EntityPlayer player);
}
