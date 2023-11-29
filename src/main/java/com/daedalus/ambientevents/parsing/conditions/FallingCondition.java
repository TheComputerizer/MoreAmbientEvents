package com.daedalus.ambientevents.parsing.conditions;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class FallingCondition extends Condition {

    public FallingCondition(JsonObject json) throws JsonIOException {
        super();
    }

    public FallingCondition(ByteBuf buf) {

    }

    @Override
    boolean isMet(EntityPlayer player) {
        return false;
    }
}
