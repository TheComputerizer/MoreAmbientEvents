package com.daedalus.ambientevents.client.gui.widgets;

import com.daedalus.ambientevents.client.gui.Palette;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class WDropDownMenu <T> extends WWidget {
	
	protected WListView<T> menu;
	protected WPushButton dropButton;
	protected WListElement<T> selected;
	protected int displayHeight;
	protected Consumer<WListElement<T>> selectionCallback;
	protected int maxMenuLength;

	public WDropDownMenu(WWidget parent) {
		super(parent);
		this.palette = new Palette();
		this.palette.primary = this.parent.palette.secondary;
		this.menu = new WListView<>(this);
		this.menu.setOnElementSelectedAction(this::onElementSelected);
		this.dropButton = new WPushButton(this,"v");
		this.dropButton.setOnClickAction(this::toggleMenu);
	}
	
	public void setMaxMenuLength(int lengthIn) {
		this.maxMenuLength = lengthIn;
		this.sizeMenu();
	}
	
	public void sizeMenu() {
		this.menu.setSize(this.width, 0);
		this.menu.fitToListSize();
		if(this.menu.height>this.maxMenuLength) this.menu.setSize(this.width,this.maxMenuLength);
	}
	
	@Override
	public void draw(int mouseX, int mouseY, float partialTicks) {
		if(Objects.isNull(this.selected)) this.onElementSelected(this.menu.elements.get(0));
		if(this.menu.isVisible() && !this.hasFocus) this.toggleMenu(0);
		drawRect(0,0,this.width,this.displayHeight,this.palette.trim);
		drawRect(1,1,this.width-1,this.displayHeight-1,this.palette.secondary);
		this.fontRenderer.drawString(this.selected.text,2,2,this.palette.text);
		super.draw(mouseX,mouseY,partialTicks);
	}
	
	@Override
	public void setSize(int width, int height) {
		this.width = width;
		this.displayHeight = height;
		this.height = displayHeight;
		this.sizeMenu();
		this.menu.move(0,this.height);
		this.dropButton.setSize(this.fontRenderer.getStringWidth(this.dropButton.label)+6,this.displayHeight);
		this.dropButton.move(this.width-this.dropButton.width,0);
	}
	
	@Override
	public void show() {
		this.visible = true;
		this.dropButton.show();
	}
	
	public WListElement<T> getSelected() {
		return this.selected;
	}
	
	public void add(WListElement<T> option) {
		this.menu.add(option);
		this.sizeMenu();
	}
	
	public void add(List<WListElement<T>> options) {
		this.menu.add(options);
		this.sizeMenu();
	}
	
	public void add(Iterator<WListElement<T>> options) {
		while(options.hasNext()) this.menu.add(options.next());
		this.sizeMenu();
	}
	
	public void populate(List<WListElement<T>> options) {
		this.menu.populate(options);
		this.sizeMenu();
	}
	
	public void pupulate(Iterator<WListElement<T>> options) {
		this.menu.populate(options);
		this.sizeMenu();
	}
	
	public void setOnElementSelectedAction(Consumer<WListElement<T>> onElementSelectedAction) {
		this.selectionCallback = onElementSelectedAction;
	}
	
	public void onElementSelected(WListElement<T> element) {
		this.selected = element;
		this.menu.hide();
		this.height = this.displayHeight;
		if(Objects.nonNull(this.selectionCallback)) this.selectionCallback.accept(element);
	}
	
	public void toggleMenu(int mouseButton) {
		if (this.menu.isVisible()) {
			this.menu.hide();
			this.height = this.displayHeight;
		} else {
			this.menu.show();
			this.height = this.displayHeight+this.menu.height;
		}
	}
}
