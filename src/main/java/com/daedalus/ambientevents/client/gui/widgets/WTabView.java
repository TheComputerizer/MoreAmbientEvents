package com.daedalus.ambientevents.client.gui.widgets;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class WTabView extends WWidget {

	protected final List<WTabLabel> labels = new ArrayList<>();
	protected final List<WWidget> widgets = new ArrayList<>();
	
	protected Consumer<String> callback;
	protected int border = 2;
	protected int selected;
	
	public WTabView(WWidget parentIn) {
		super(parentIn);
	}
	
	public void addTab(WWidget widget, String labelIn) {
		WTabLabel label = new WTabLabel(this,this.labels.size(),labelIn);
		label.setOnClickAction(this::onTabSwitch);
		if(!this.labels.isEmpty()) {
			int location = this.labels.get(this.labels.size()-1).offsetX + this.labels.get(this.labels.size()-1).width;
			label.move(location,0);
		} else label.move(this.border, 0);
		this.labels.add(label);
		widget.setParent(this);
		widget.move(this.border, label.height);
		widget.setSize(this.width-2*this.border,this.height-this.border-label.height);
		this.widgets.add(widget);
		if(this.labels.size()==1) this.labels.get(0).select();
	}
	
	public void clear() {
		this.labels.clear();
		this.widgets.clear();
		this.subWidgets.clear();
	}
	
	public void onTabSwitch(int tabID) {
		this.selected = tabID;
		for(int i=0; i<this.widgets.size(); i++) {
			if(i==tabID) this.widgets.get(i).show();
			else {
				this.widgets.get(i).hide();
				this.labels.get(i).deselect();
			}
		}
		if(Objects.nonNull(this.callback)) this.callback.accept(this.labels.get(this.selected).label);
	}
	
	@Override
	public void setSize(int width, int height) {
		super.setSize(width,height);
		for(WWidget widget : this.widgets)
			widget.setSize(this.width-2*this.border,this.height-this.border-this.labels.get(0).height);
	}
	
	@Override
	public void show() {
		this.visible = true;
		for(WTabLabel label : labels) label.show();
		this.onTabSwitch(this.selected);
	}
	
	@Override
	public void draw(int mouseX, int mouseY, float partialTicks) {
		if(this.visible && !this.labels.isEmpty()) {
			drawRect(0,this.labels.get(0).height,this.width,this.height,this.palette.edging);
			super.draw(mouseX,mouseY,partialTicks);
		}
	}
}
