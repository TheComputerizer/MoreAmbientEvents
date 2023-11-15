package com.daedalus.ambientevents.client.gui.widgets;

import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.daedalus.ambientevents.client.gui.Palette;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class WWidget extends GuiScreen {

	protected static final ResourceLocation CLICK_SOUND = new ResourceLocation("ui.button.click");
	protected final List<WWidget> subWidgets = new ArrayList<>();
	public int offsetX = 0;
	public int offsetY = 0;
	protected boolean visible;
	protected WWidget focus;
	protected boolean hasFocus;
	protected boolean mouseOver;
	protected WWidget dragTarget;
	protected WWidget parent;
	public Palette palette;

	public WWidget(WWidget parent) {
		if(Objects.nonNull(parent)) this.setParent(parent);
	}

	protected SoundEvent getClickSound() {
		return ForgeRegistries.SOUND_EVENTS.getValue(CLICK_SOUND);
	}

	public void move(int x, int y) {
		this.offsetX = x;
		this.offsetY = y;
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public boolean isMouseOver(int mouseX, int mouseY) {
		this.mouseOver = mouseX>=0 && mouseX<this.width && mouseY>=0 && mouseY<this.height;
		return this.mouseOver;
	}

	public void onMouseClick(int mouseX, int mouseY, int mouseButton) {
		for(WWidget subWidget : this.subWidgets) {
			if(subWidget.isVisible() && subWidget.isMouseOver(mouseX-subWidget.offsetX,mouseY-subWidget.offsetY)) {
				subWidget.onMouseClick(mouseX-subWidget.offsetX,mouseY-subWidget.offsetY,mouseButton);
				if(Objects.nonNull(this.focus) && !this.focus.equals(subWidget)) this.focus.setUnfocused();
				this.focus = subWidget;
				this.focus.setFocused();
				this.dragTarget = this.focus;
				break;
			}
		}
	}

	public void onMouseRelease(int mouseX, int mouseY, int state) {
		if(Objects.nonNull(this.focus))
			this.focus.onMouseRelease(mouseX-this.focus.offsetX,mouseY-this.focus.offsetY,state);
		this.dragTarget = null;
	}

	public void onMouseDrag(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		if(Objects.nonNull(this.dragTarget))
			this.dragTarget.onMouseDrag(mouseX-this.dragTarget.offsetX, mouseY-this.dragTarget.offsetY,
					clickedMouseButton,timeSinceLastClick);
	}

	public void setFocused() {
		this.hasFocus = true;
	}

	public void setUnfocused() {
		this.hasFocus = false;
		if(Objects.nonNull(this.focus)) this.focus.setUnfocused();
	}

	public boolean isFocused() {
		return this.hasFocus;
	}
	
	public boolean isVisible() {
		return this.visible;
	}

	public void onKeyTyped(char typedChar, int keyCode) {
		if(Objects.nonNull(this.focus)) this.focus.onKeyTyped(typedChar,keyCode);
	}

	public void addWidget(WWidget widget) {
		this.subWidgets.add(widget);
	}
	
	public void removeWidget(WWidget widget) {
		this.subWidgets.remove(widget);
	}

	public void show() {
		this.visible = true;
		for(WWidget subwidget : this.subWidgets) subwidget.show();
	}
	
	public void hide() {
		this.visible = false;
		for(WWidget subwidget : this.subWidgets) subwidget.hide();
	}
	
	public void setParent(WWidget parent) {
		if(Objects.nonNull(this.parent)) this.parent.removeWidget(this);
		this.parent = parent;
		this.parent.addWidget(this);
		this.mc = this.parent.mc;
		this.palette = this.parent.palette;
		this.fontRenderer = this.parent.fontRenderer;
	}

	public void draw(int mouseX, int mouseY, float partialTicks) {
		if(this.visible) {
			for(WWidget subwidget : this.subWidgets) {
				GlStateManager.pushMatrix();{
					GlStateManager.translate(subwidget.offsetX,subwidget.offsetY,0);
					subwidget.draw(mouseX-subwidget.offsetX,mouseY-subwidget.offsetY,partialTicks);
				}
				GlStateManager.popMatrix();
			}
		}
	}

	public static int mixColors(int color1, int color2) {
		int out = 0;
		for(int i = 0; i < 32; i += 8) {
			int color1A = color1 & (0b11111111<<i);
			int color2A = color2 & (0b11111111<<i);
			out^=((color1A+color2A)>>1) & (0b11111111<<i);
		}
		return out;
	}

	public static float getAlpha(int color) {
		return ((color & 0xff000000)>>24)/255f;
	}

	public static float getRed(int color) {
		return ((color & 0x00ff0000)>>16)/255f;
	}

	public static float getGreen(int color) {
		return ((color & 0x0000ff00)>>8)/255f;
	}

	public static float getBlue(int color) {
		return (color & 0x000000ff)/255f;
	}

	public static Number constrain(Number input, Number low, Number high) {
		if(input.doubleValue()>high.doubleValue()) return high;
		else if(input.doubleValue()<low.doubleValue()) return low;
		else return input;
	}
	
	public static JsonObject copyJSONObject(JsonObject json) {
		JsonObject result = new JsonObject();
		for(Map.Entry<String,JsonElement> entry : json.entrySet())
			result.add(entry.getKey(),entry.getValue());
		return result;
	}
	
	public static JsonArray copyJSONArray(JsonArray array) {
		JsonArray result = new JsonArray();
		for(JsonElement element : array) result.add(element);
		return result;
	}
}
