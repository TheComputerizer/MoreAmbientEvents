package com.daedalus.ambientevents.parsing.conditions;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class DimensionCondition extends Condition {


    public DimensionCondition(JsonObject json) throws JsonIOException {
        super();
        addNumber(json,"dimension");
    }

    public DimensionCondition(ByteBuf buf) {
        super(buf);
    }

    @Override
    public boolean isMet(EntityPlayer player) {
        return player.dimension==getNum(player,"dimension").intValue();
    }
}
