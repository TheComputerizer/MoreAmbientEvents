package com.daedalus.ambientevents.client.gui.widgets;

public class WPushButton extends WAbstractButton {

	protected String label;

	public WPushButton(WWidget parent) {
		super(parent);
	}

	public WPushButton(WWidget parent, String text) {
		super(parent);
		this.setLabel(text);
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public void draw(int mouseX, int mouseY, float partialTicks) {
		if (this.visible) {
			int trim = this.isMouseOver(mouseX,mouseY) ? mixColors(this.palette.trim,this.palette.highlight)
					: this.palette.trim;
			int secondary = this.mouseOver ? mixColors(this.palette.secondary,this.palette.highlight)
					: this.palette.secondary;
			int text = this.mouseOver ? mixColors(this.palette.text,this.palette.highlight) : this.palette.text;
			drawRect(0,0,this.width,this.height,trim);
			drawRect(1,1,this.width-1,this.height-1,secondary);
			this.fontRenderer.drawString(this.label,this.width/2-this.fontRenderer.getStringWidth(this.label)/2,
					this.height/2-this.fontRenderer.FONT_HEIGHT/2,text);
		}
	}
}
