package com.daedalus.ambientevents.parsing.conditions;

import com.daedalus.ambientevents.AmbientEvents;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Random;

public class ChanceCondition extends Condition {


	public ChanceCondition(JsonObject json) throws JsonIOException {
		super();
		addNumber(json,"chance");
	}

	public ChanceCondition(ByteBuf buf) {
		super(buf);
	}

	@Override
	public boolean isMet(EntityPlayer player) {
		Random rand = AmbientEvents.entityRand(player);
        return rand.nextDouble()*getNum(rand,"chance").doubleValue()<1d;
    }
}
