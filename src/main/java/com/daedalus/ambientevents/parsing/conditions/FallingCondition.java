package com.daedalus.ambientevents.parsing.conditions;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class FallingCondition extends ComparisonCondition {

    public FallingCondition(JsonObject json) throws JsonIOException {
        super(json);
    }

    public FallingCondition(ByteBuf buf) {
        super(buf);
    }

    @Override
    public boolean isMet(EntityPlayer player) {
        return !player.onGround;
    }
}
