package com.daedalus.ambientevents.actions;

import java.util.Random;

import org.json.JSONObject;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;

public class LightningAction implements IAction {

	protected int chance;
	protected enum Target {player, nearPlayer};
	protected Target target;
	protected Random random;
	
	protected double x;
	protected double y;
	protected double z;
	
	public LightningAction (JSONObject args) throws Exception {
		random = new Random();
		
		String targ = args.getString("target");
		
		switch (targ) {
		
		case "player":	target = Target.player;
		break;
		case "nearPlayer":	target = Target.nearPlayer;
		break;
		
		default: 		throw new Exception("Unrecognized target: " + targ);
		}
		
		chance = args.getInt("chance");
	}
	
	@Override
	public void execute(EntityPlayer player) {
		if (random.nextInt(chance) == 0) {

			if (target == Target.player) {
				x = player.posX;
				y = player.posY;
				z = player.posZ;
			}
			
			player.world.addWeatherEffect(new EntityLightningBolt(player.world, x, y, z, false));
		}
	}
}
