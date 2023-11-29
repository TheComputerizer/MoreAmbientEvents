package com.daedalus.ambientevents.parsing.conditions;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class MoonCondition extends Condition {

    public MoonCondition(JsonObject json) throws JsonIOException {
        super();
    }

    public MoonCondition(ByteBuf buf) {

    }

    @Override
    boolean isMet(EntityPlayer player) {
        return false;
    }
}
