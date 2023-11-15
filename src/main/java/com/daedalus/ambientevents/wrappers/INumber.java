package com.daedalus.ambientevents.wrappers;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;

import java.util.Random;

public interface INumber {

	boolean parse(JsonElement json) throws JsonIOException;
	Number getValue(Random rand);
}
