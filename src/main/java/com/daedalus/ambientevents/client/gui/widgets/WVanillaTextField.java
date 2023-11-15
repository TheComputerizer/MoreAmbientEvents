package com.daedalus.ambientevents.client.gui.widgets;

import net.minecraft.client.gui.GuiTextField;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WVanillaTextField extends WWidget {

	public static int nextID = 0;
	protected GuiTextField textField;
	protected Consumer<String> callback;
	protected Pattern pattern = Pattern.compile(".*");
	protected Matcher match;
	protected String lastGoodContent;

	public WVanillaTextField(WWidget parent) {
		this(parent,0,0,0,0);
	}

	public WVanillaTextField(WWidget parent, int x, int y, int width, int height) {
		super(parent);
		this.textField = new GuiTextField(nextID++,this.fontRenderer,0,0,0,0);
		this.setSize(width,height);
		this.move(x,y);
	}

	@Override
	public void setSize(int width, int height) {
		super.setSize(width,height);
		this.textField.height = height;
		this.textField.width = width;
	}

	@Override
	public void setFocused() {
		super.setFocused();
		this.textField.setFocused(true);
	}

	@Override
	public void setUnfocused() {
		super.setUnfocused();
		this.textField.setFocused(false);
	}

	@Override
	public void onKeyTyped(char typedChar, int keyCode) {
		this.textField.textboxKeyTyped(typedChar,keyCode);
		this.checkText();
		if(Objects.nonNull(this.callback)) this.callback.accept(this.getText());
	}

	@Override
	public void onMouseClick(int mouseX, int mouseY, int mouseButton) {
		this.textField.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void draw(int mouseX, int mouseY, float partialTicks) {
		if(this.visible) this.textField.drawTextBox();
	}

	protected void checkText() {
		this.match = this.pattern.matcher(this.textField.getText());
		if(this.match.matches()) this.lastGoodContent = this.textField.getText();
        else this.textField.setText(this.lastGoodContent);
	}

	public void setValidRegex(String regex) {
		this.pattern = Pattern.compile(regex);
	}

	public void setOnTextChangedAction(Consumer<String> onTextChangedAction) {
		this.callback = onTextChangedAction;
	}

	public String getText() {
		return this.textField.getText();
	}
	
	public void setText(String text) {
		this.textField.setText(text);
		this.textField.setCursorPosition(0);
	}
}
