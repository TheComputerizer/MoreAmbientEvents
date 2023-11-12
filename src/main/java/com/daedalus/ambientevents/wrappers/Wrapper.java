package com.daedalus.ambientevents.wrappers;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

public class Wrapper {

	/**
	 * Because I love abstraction, I'm abracting away primitives.
	 * It's for good reason, though.
	 * It's so that you can use either primitives or other things that return primitives.
	 * Like a random number generator, or a mapping function.
	 * As long as it behaves according the interface, it doesn't matter how you get the value.
	 *
	 * ^ Absolutely based take - The_Computerizer
	 */

	public static INumber newNumber(Object args) throws JsonIOException {
		if(args instanceof Double) return new RawNumber((double)args);
		else if(args instanceof Integer) return new RawNumber((int)args);
		else if(args instanceof JsonObject) {
			JsonObject json = (JsonObject)args;
			if(json.has("type")) {
				switch(json.get("type").getAsString()) {
					case "random": return new RandomNumber(json);
					case "randompick": return new RandomPickNumber(json);
					case "sequentialpick": return new SequentialPickNumber(json);
					case "map": return new MapNumber(json);
					default: throw new JsonIOException("Numeric type not recognized");
				}
			}
		}
		throw new JsonIOException("Numeric value '"+args+"' not recognized");
	}

	public static IString newString(Object args) throws JsonIOException {
		if(args instanceof String) return new RawString((String)args);
		else if(args instanceof JsonObject) {
			JsonObject json = (JsonObject)args;
			if(json.has("type")) {
				switch(json.get("type").getAsString()) {
					case "randompick": return new RandomPickString(json);
					case "sequentialpick": return new SequentialPickString(json);
					default: throw new JsonIOException("String type not recognized");
				}
			}
		}

		throw new JsonIOException("String value '"+args+"' not recognized");
	}
}
