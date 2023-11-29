package com.daedalus.ambientevents.parsing.actions;

import com.daedalus.ambientevents.AmbientEvents;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Random;

public class LightningAction extends ServerAction {

	public LightningAction(JsonObject json) throws JsonIOException {
		super(json);
		addString(json,"target");
		addNumber(json,"radius");
	}

	public LightningAction(ByteBuf buf) {
		super(buf);
	}

	@Override
	public void execute(EntityPlayer player) {
		double x = 0d,y = 0d,z = 0d;
		Random rand = AmbientEvents.entityRand(player);
		if(getNum(rand,"chance").doubleValue()<1d) {
			switch(getStr(rand,"target")) {
				case "player":
					x = player.posX;
					y = player.posY;
					z = player.posZ;
					break;
				case "nearplayer":
					int radius = getNum(rand,"radius").intValue();
					x = randRadiusInt(rand,(int)player.posX,radius);
					y = player.posY;
					z = randRadiusInt(rand,(int)player.posZ,radius);
					break;
			}
			player.world.addWeatherEffect(new EntityLightningBolt(player.world,x,y,z,false));
		}
	}
}
