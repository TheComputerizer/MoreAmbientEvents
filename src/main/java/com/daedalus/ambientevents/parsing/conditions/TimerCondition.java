package com.daedalus.ambientevents.parsing.conditions;

import com.daedalus.ambientevents.AmbientEvents;
import com.daedalus.ambientevents.AmbientEventsRef;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.Random;

public class TimerCondition extends ComparisonCondition {

    protected final MutableInt tickTimer;

    protected TimerCondition(JsonObject json) throws JsonIOException {
        super(json);
        addNumber(json,"max");
        this.tickTimer = new MutableInt();
    }

    protected TimerCondition(ByteBuf buf) {
        super(buf);
        this.tickTimer = new MutableInt(buf.readInt());
    }

    @Override
    boolean isMet(EntityPlayer player) {
        Random rand = AmbientEvents.entityRand(player);
        boolean isTime = compare(rand,this.tickTimer.getValue());
        if(isTime) {
            if(this.tickTimer.getAndAdd(AmbientEventsRef.EVENT_TICK_RATE)>=getNum(rand,"max").intValue()) {
                this.tickTimer.setValue(0);
                return false;
            }
            return true;
        }
        return false;
    }

    public void reset() {
        this.tickTimer.setValue(0);
    }

    @Override
    public void sync(ByteBuf buf) {
        super.sync(buf);
        buf.writeInt(this.tickTimer.getValue());
    }
}
