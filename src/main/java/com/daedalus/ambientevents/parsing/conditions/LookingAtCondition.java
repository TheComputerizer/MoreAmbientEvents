package com.daedalus.ambientevents.parsing.conditions;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class LookingAtCondition extends Condition {

    public LookingAtCondition(JsonObject json) throws JsonIOException {
        super();
        addString(json,"object");
    }

    public LookingAtCondition(ByteBuf buf) {
        super(buf);
    }

    @Override
    public boolean isMet(EntityPlayer player) {
        return false;
    }
}
