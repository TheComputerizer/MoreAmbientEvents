package com.daedalus.ambientevents.wrappers;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;

import java.util.Random;

public interface IString {

	boolean parse(JsonElement json) throws JsonIOException;
	String getValue(Random rand);
}
