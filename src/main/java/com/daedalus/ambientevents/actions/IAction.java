package com.daedalus.ambientevents.actions;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

public interface IAction {

	void execute(EntityPlayer player);
	boolean canExecute(Side side);
	void sync(ByteBuf buf);
}
