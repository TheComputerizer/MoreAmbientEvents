package com.daedalus.ambientevents.conditions;

import org.json.*;

import net.minecraft.entity.player.EntityPlayer;

public class CommonCondition implements ICondition {
	
	public static ICondition newCondition(JSONObject args) throws Exception {
		// Factory method for creating new conditions from JSON based config
		ICondition output = null;
		
		String type = args.getString("type");
		
		switch (type) {
		
		case "playerpos":	output = new PlayerPosCondition(args);
		break;
		
		default:			throw new Exception("Unrecogized condition type: " + type);
		}
		
		return output;
	}
	
	public CommonCondition(JSONObject args) {}
	public CommonCondition() {}
	public boolean isMet(EntityPlayer player) {
		return false;
	}
}
