package com.daedalus.ambientevents.parsing;

import net.minecraft.util.WeightedRandom;

public class WeightedValue<T> extends WeightedRandom.Item {

    private final T value;
    public WeightedValue(T value, int weight) {
        super(weight);
        this.value = value;
    }

    public T getValue() {
        return this.value;
    }
}
