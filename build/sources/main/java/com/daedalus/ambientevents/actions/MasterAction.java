package com.daedalus.ambientevents.actions;

import java.util.ArrayList;

import org.json.JSONObject;

import com.daedalus.ambientevents.AmbientEvents;

import org.apache.logging.log4j.Level;
import org.json.JSONArray;

import net.minecraft.entity.player.EntityPlayer;

public class MasterAction implements IAction {

	ArrayList<IAction> actions;
	
	public MasterAction(JSONArray args) throws Exception {
		actions = new ArrayList<IAction>();
		for (int i = 0; i < args.length(); i++) {
			actions.add(CommonAction.newAction(args.getJSONObject(i)));
		}
	}
	
	public void execute(EntityPlayer player) {
		for (int i = 0; i < actions.size(); i++) {
			actions.get(i).execute(player);
		}
	}
}
