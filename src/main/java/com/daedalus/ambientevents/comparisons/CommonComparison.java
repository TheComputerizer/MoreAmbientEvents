package com.daedalus.ambientevents.comparisons;

public class CommonComparison {

	public static CommonComparison newComparison(String symbol) {
		CommonComparison output = null;
		
		switch (symbol) {
		
		case ">":	output = new GreaterThanComparison();
		break;
		case "<":	output = new LessThanComparison();
		break;
		
		default:	output = new CommonComparison();
		}
		
		return output;
	}
	
	public boolean compare(double in1, double in2) {
		return false;
	}
}
