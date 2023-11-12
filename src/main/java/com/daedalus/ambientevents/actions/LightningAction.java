package com.daedalus.ambientevents.actions;

import com.daedalus.ambientevents.wrappers.INumber;
import com.daedalus.ambientevents.wrappers.IString;
import com.daedalus.ambientevents.wrappers.Wrapper;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Random;

public class LightningAction extends CommonAction {

	protected IString target;
	protected INumber radius;

	protected double x;
	protected double y;
	protected double z;

	public LightningAction(JsonObject args) throws JsonIOException {
		super(args);
		if(args.has("target")) this.target = Wrapper.newString(args.get("target"));
		else this.target = Wrapper.newString("player");
		if(args.has("radius")) this.radius = Wrapper.newNumber(args.get("radius"));
		else this.radius = Wrapper.newNumber(1);
	}

	@Override
	public void execute(EntityPlayer player) {
		Random rand = player.world.rand;
		if(this.chance.getValue(rand)<1) {
			switch (this.target.getValue()) {
			case "player":
				this.x = player.posX;
				this.y = player.posY;
				this.z = player.posZ;
				break;
			case "nearplayer":
				this.x = player.posX+rand.nextInt((int)this.radius.getValue(rand)*2)-this.radius.getValue(rand);
				this.y = player.posY;
				this.z = player.posZ+rand.nextInt((int)this.radius.getValue(rand)*2)-this.radius.getValue(rand);
				break;
			default:
				this.x = 0;
				this.y = 0;
				this.z = 0;
			}
			player.world.addWeatherEffect(new EntityLightningBolt(player.world,this.x,this.y,this.z,false));
		}
	}
}
