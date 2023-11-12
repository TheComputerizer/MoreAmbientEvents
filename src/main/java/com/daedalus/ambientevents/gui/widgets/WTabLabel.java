package com.daedalus.ambientevents.gui.widgets;

import net.minecraft.util.SoundCategory;

import java.util.Objects;

public class WTabLabel extends WAbstractButton {

	protected String label;
	protected int padding = 2;
	protected int ID;
	public int border = 2;
	protected boolean selected = false;
	
	public WTabLabel(WWidget parent, int ID) {
		this(parent,ID,"");
	}
	
	public WTabLabel(WWidget parent, int ID, String label) {
		super(parent);
		this.ID = ID;
		this.setLabel(label);
	}
	
	public void setLabel(String label) {
		this.label = label;
		this.width = this.fontRenderer.getStringWidth(label)+this.padding*2+this.border*2;
		this.height = this.fontRenderer.FONT_HEIGHT+this.padding+this.border*2;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	@Override
	public void draw(int mouseX, int mouseY, float partialTicks) {
		if(this.visible) {
			drawRect(0,0,this.width,this.height,this.palette.edging);
			drawRect(this.border,this.border,this.width-this.border,this.height-(this.selected ? 0 : this.border),
					this.selected ? this.palette.primary : this.palette.secondary);
			this.fontRenderer.drawString(this.label,this.padding+this.border,this.padding+this.border,this.palette.text);
		}
	}

	@Override
	public void onMouseClick(int mouseX, int mouseY, int mouseButton) {
		if(this.selected) return;
		this.mc.world.playSound(this.mc.player.posX,this.mc.player.posY,this.mc.player.posZ,getClickSound(),
				SoundCategory.MASTER,0.3f,1f,true);
		this.select();
	}
	
	public void select() {
		this.selected = true;
		if(Objects.nonNull(this.callback)) this.callback.accept(this.ID);
	}
	
	public void deselect() {
		this.selected = false;
	}
	
	public boolean isSelected() {
		return this.selected;
	}
}
