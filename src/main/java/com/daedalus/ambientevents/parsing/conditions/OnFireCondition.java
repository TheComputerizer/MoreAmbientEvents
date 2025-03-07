package com.daedalus.ambientevents.parsing.conditions;

import com.google.gson.JsonIOException;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class OnFireCondition extends Condition {

    public OnFireCondition() throws JsonIOException {
        super();
    }

    public OnFireCondition(ByteBuf buf) {
        super(buf);
    }

    @Override
    public boolean isMet(EntityPlayer player) {
        return player.isBurning();
    }
}
