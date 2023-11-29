package com.daedalus.ambientevents.parsing.conditions;

import com.daedalus.ambientevents.capability.CapabilityHandler;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class TimeSinceSleepCondition extends ComparisonCondition {


	public TimeSinceSleepCondition(JsonObject json) throws JsonIOException {
		super(json);
	}

	public TimeSinceSleepCondition(ByteBuf buf) {
		super(buf);
	}

	@Override
	public boolean isMet(EntityPlayer player) {
        return compare(player,CapabilityHandler.timeSinceSleep(player),d -> d*24000);
    }
}
