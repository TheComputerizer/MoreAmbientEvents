package com.daedalus.ambientevents.parsing.conditions;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class SeasonCondition extends Condition {

    public SeasonCondition(JsonObject json) throws JsonIOException {
        super();
    }

    public SeasonCondition(ByteBuf buf) {

    }

    @Override
    boolean isMet(EntityPlayer player) {
        return false;
    }
}
