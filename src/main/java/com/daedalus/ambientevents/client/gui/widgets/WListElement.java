package com.daedalus.ambientevents.client.gui.widgets;

public class WListElement<T> {

	public String text;
	public T item;
	
	public WListElement() {}
	
	public WListElement (String text, T item) {
		this.text = text;
		this.item = item;
	}
}
