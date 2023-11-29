package com.daedalus.ambientevents.parsing.conditions;

import com.daedalus.ambientevents.AmbientEvents;
import com.daedalus.ambientevents.parsing.comparisons.NumericComparison;
import com.daedalus.ambientevents.parsing.strings.StringType;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

public abstract class ComparisonCondition extends Condition {

    protected NumericComparison comparison;

    protected ComparisonCondition(JsonObject json) throws JsonIOException {
        super();
        addNumber(json,"value");
        this.comparison = new NumericComparison(StringType.tryAutoParse(json,"comparison",true));
    }

    protected ComparisonCondition(ByteBuf buf) {
        super(buf);
        this.comparison = new NumericComparison(buf);
    }

    protected boolean compare(EntityPlayer player, double d1) {
        return compare(player,d1,null);
    }

    protected boolean compare(Random rand, double d1) {
        return compare(rand,d1,null);
    }

    protected boolean compare(EntityPlayer player, double d1, @Nullable Function<Double,Double> valTransform) {
        return compare(AmbientEvents.entityRand(player),d1,valTransform);
    }

    protected boolean compare(Random rand, double d1, @Nullable Function<Double,Double> valTransform) {
        Number val = getNum(rand,"value");
        double d2 = Objects.nonNull(valTransform) ? valTransform.apply(val.doubleValue()) : val.doubleValue();
        return this.comparison.compare(rand,d1,d2);
    }

    @Override
    public void sync(ByteBuf buf) {
        super.sync(buf);
        this.comparison.sync(buf);
    }
}
