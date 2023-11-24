package com.daedalus.ambientevents.actions;

import com.daedalus.ambientevents.wrappers.INumber;
import com.daedalus.ambientevents.wrappers.IString;
import com.daedalus.ambientevents.wrappers.NumberType;
import com.daedalus.ambientevents.wrappers.StringType;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Random;

public class LightningAction extends CommonAction {

	protected IString target;
	protected INumber radius;

	protected double x;
	protected double y;
	protected double z;

	public LightningAction(JsonObject json) throws JsonIOException {
		super(json);
		this.target = StringType.tryAutoParse(json,"player",true);
		this.radius = NumberType.tryAutoParse(json,"radius",true);
	}

	public LightningAction(ByteBuf buf) {
		super(buf);
		this.target = StringType.sync(buf);
		this.radius = NumberType.sync(buf);
	}

	@Override
	public void execute(EntityPlayer player) {
		Random rand = player.world.rand;
		if(this.chance.getValue(rand).doubleValue()<1d) {
			switch (this.target.getValue(rand)) {
			case "player":
				this.x = player.posX;
				this.y = player.posY;
				this.z = player.posZ;
				break;
			case "nearplayer":
				int radiusInt = this.radius.getValue(rand).intValue();
				this.x = player.posX+rand.nextInt(radiusInt*2)-radiusInt;
				this.y = player.posY;
				this.z = player.posZ+rand.nextInt(radiusInt*2)-radiusInt;
				break;
			default:
				this.x = 0;
				this.y = 0;
				this.z = 0;
			}
			player.world.addWeatherEffect(new EntityLightningBolt(player.world,this.x,this.y,this.z,false));
		}
	}

	@Override
	public void sync(ByteBuf buf) {
		super.sync(buf);
		this.target.sync(buf);
		this.radius.sync(buf);
	}
}
