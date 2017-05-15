package com.daedalus.ambientevents.conditions;

import org.apache.logging.log4j.Level;
import org.json.JSONObject;

import net.minecraft.entity.player.EntityPlayer;

import com.daedalus.ambientevents.AmbientEvents;
import com.daedalus.ambientevents.comparisons.*;

public class PlayerPosCondition implements ICondition {

	protected enum Dimension {x, y, z};
	protected Dimension dimension;
	protected CommonComparison comparison;
	protected double compareValue;
	
	public PlayerPosCondition(JSONObject args) throws Exception {
		String dimen = args.getString("dimension");
		
		switch (dimen) {
		
		case "x":	dimension = Dimension.x;
		break;
		case "y":	dimension = Dimension.y;
		break;
		case "z":	dimension = Dimension.z;
		break;
		
		default:	throw new Exception("Unrecognized dimension: " + dimen);
		}
		
		comparison = CommonComparison.newComparison(args.getString("comparison"));
		
		compareValue = args.getDouble("value");
	}
	
	public boolean isMet(EntityPlayer player) {
		double playerValue;
		
		switch (dimension) {
			case x: playerValue = player.posX;
			break;
			case y: playerValue = player.posY;
			break;
			case z: playerValue = player.posZ;
			break;
			
			default:return false;
		}
		
		return comparison.compare(playerValue, compareValue);
	}
}
