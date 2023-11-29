package com.daedalus.ambientevents.parsing.conditions;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class TimeOfDayCondition extends ComparisonCondition {

	public TimeOfDayCondition(JsonObject json) throws JsonIOException {
		super(json);
	}

	public TimeOfDayCondition(ByteBuf buf) {
		super(buf);
	}

	@Override
	public boolean isMet(EntityPlayer player) {
        return compare(player,player.world.getWorldTime());
    }
}
