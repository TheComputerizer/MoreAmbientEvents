package com.daedalus.ambientevents.parsing.conditions;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class OnFireCondition extends Condition {

    public OnFireCondition(JsonObject json) throws JsonIOException {
        super();
    }

    public OnFireCondition(ByteBuf buf) {

    }

    @Override
    boolean isMet(EntityPlayer player) {
        return false;
    }
}
