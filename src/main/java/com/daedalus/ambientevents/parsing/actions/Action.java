package com.daedalus.ambientevents.parsing.actions;

import com.daedalus.ambientevents.parsing.WrapperHolder;
import com.daedalus.ambientevents.parsing.numbers.RawNumber;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

public abstract class Action extends WrapperHolder {

	protected Action(JsonObject json) throws JsonIOException {
		super();
		addNumber(json,"chance",new RawNumber(1d));
	}

	protected Action(ByteBuf buf) {
		super(buf);
	}

	public abstract boolean canExecute(Side side);

	public abstract void execute(EntityPlayer player);
}
