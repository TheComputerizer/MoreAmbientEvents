package com.daedalus.ambientevents.gui.widgets;

import java.util.function.Consumer;

import net.minecraft.client.gui.GuiButton;

public class WVanillaButton extends WAbstractButton {

	protected Consumer<Integer> callback;
	protected GuiButton button;
	protected int buttonID;
	protected String buttonText;

	public WVanillaButton(WWidget parent, int ID, String text) {
		this(parent,ID,0,0,0,0,text);
	}

	public WVanillaButton(WWidget parent, int ID, int x, int y, int width, int height, String text) {
		super(parent);
		this.buttonID = ID;
		this.buttonText = text;
		this.initGui();
		this.setSize(width,height);
		this.move(x, y);
	}

	@Override
	public void initGui() {
		this.button = new GuiButton(this.buttonID,0,0,0,0,this.buttonText);
	}

	@Override
	public void setSize(int width, int height) {
		super.setSize(width,height);
		this.button.width = width;
		this.button.height = height;
	}

	@Override
	public void draw(int mouseX, int mouseY, float partialTicks) {
		if (this.visible) {
			super.draw(mouseX,mouseY,partialTicks);
			this.button.drawButton(this.mc,mouseX,mouseY,partialTicks);
		}
	}
}
