package com.daedalus.ambientevents.comparisons;

import com.daedalus.ambientevents.wrappers.IString;
import com.daedalus.ambientevents.wrappers.StringType;
import io.netty.buffer.ByteBuf;

import java.util.Random;

public class NumericComparison {

	protected IString symbol;

	protected static boolean greaterThan(double in1, double in2) {
		return in1>in2;
	}

	protected static boolean lessThan(double in1, double in2) {
		return in1<in2;
	}

	public NumericComparison(IString symbolIn) {
		this.symbol = symbolIn;
	}

	public NumericComparison(ByteBuf buf) {
		this.symbol = StringType.sync(buf);
	}

	public boolean compare(Random rand, double in1, double in2) {
		switch(this.symbol.getValue(rand)) {
			case ">": return greaterThan(in1,in2);
			case "<": return lessThan(in1,in2);
			default: return false;
		}
	}

	public void sync(ByteBuf buf) {
		this.symbol.sync(buf);
	}
}
