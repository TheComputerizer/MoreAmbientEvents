package com.daedalus.ambientevents.parsing.conditions;

import com.google.gson.JsonIOException;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class DeathCondition extends Condition {

    public DeathCondition() throws JsonIOException {
        super();
    }

    public DeathCondition(ByteBuf buf) {
        super(buf);
    }

    @Override
    public boolean isMet(EntityPlayer player) {
        return player.isDead;
    }
}
