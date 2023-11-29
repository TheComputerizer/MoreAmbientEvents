package com.daedalus.ambientevents.client.gui;

import com.daedalus.ambientevents.client.ClientEventManager;
import com.daedalus.ambientevents.client.gui.widgets.WPushButton;
import com.daedalus.ambientevents.client.gui.widgets.WVanillaTextField;
import com.daedalus.ambientevents.client.gui.widgets.WWidget;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.util.Objects;

public class ConfiguratorGUI extends WWidget {

	public static JsonObject eventsJSON;
	public static JsonObject manifestJSON;

	protected WPushButton exit;
	protected EventList eventList;
	protected SubLists subLists;
	protected ValueTabs valueTabs;
	
	protected boolean firstStart = true;

	public ConfiguratorGUI(Minecraft mc) {
		super(null);
		manifestJSON = ClientEventManager.readManifest(mc.getResourceManager());
		if(Objects.isNull(manifestJSON)) manifestJSON = new JsonObject();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.draw(mouseX,mouseY,partialTicks);
		super.drawScreen(mouseX,mouseY,partialTicks);
	}

	@Override
	public void initGui() {
		super.initGui();
		int exitHeight = 12;
		if(this.firstStart) {
			this.palette = new Palette();
			this.exit = new WPushButton(this,"Exit");
			this.eventList = new EventList(this);
			this.subLists = new SubLists(this);
			this.valueTabs = new ValueTabs(this);
		}
		this.eventList.setSize(this.width/2,this.height/2);
		this.eventList.setOnEventSelectedAction(this.subLists::populate);
		this.subLists.setSize(this.width/2,this.height/2);
		this.subLists.move(this.width/2,0);
		this.subLists.setOnElementSelectedAction(this.valueTabs::populate);
		this.exit.setSize(50,exitHeight);
		this.exit.move(this.width/2-25,this.height-exitHeight);
		this.exit.setOnClickAction(this::exit);
		this.valueTabs.setSize(this.width,this.height/2-exitHeight);
		this.valueTabs.move(0,this.height/2);
		this.show();
		this.firstStart = false;
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX,mouseY,mouseButton);
		this.onMouseClick(mouseX,mouseY,mouseButton);
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX,mouseY,state);
		this.onMouseRelease(mouseX,mouseY,state);
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		super.mouseClickMove(mouseX,mouseY,clickedMouseButton,timeSinceLastClick);
		this.onMouseDrag(mouseX,mouseY,clickedMouseButton,timeSinceLastClick);
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar,keyCode);
		this.onKeyTyped(typedChar,keyCode);
	}
	
	public void exit(int i) {
		WVanillaTextField.nextID = 0;
		this.mc.displayGuiScreen(null);
	}
}
