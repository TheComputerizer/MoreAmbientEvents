package com.daedalus.ambientevents.gui.widgets;

import java.util.Objects;
import java.util.function.Consumer;

import com.daedalus.ambientevents.actions.PlaySoundAction;

import net.minecraft.util.SoundCategory;

public class WAbstractButton extends WWidget {

	protected Consumer<Integer> callback;

	public WAbstractButton(WWidget parent) {
		super(parent);
	}

	public void setOnClickAction(Consumer<Integer> onClick) {
		this.callback = onClick;
	}

	@Override
	public void onMouseClick(int mouseX, int mouseY, int mouseButton) {
		this.mc.world.playSound(this.mc.player.posX,this.mc.player.posY,this.mc.player.posZ, getClickSound(),
				SoundCategory.MASTER,0.3f,1.0f,true);
		if(Objects.nonNull(this.callback)) this.callback.accept(mouseButton);
	}
}
