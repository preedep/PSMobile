package com.epro.psmobile.adapter;

public class ListItemRow<T> {

	private boolean isRowHeader = false;
	private String textRowHeader = "";
	private T Item;
	
	public ListItemRow() {
		// TODO Auto-generated constructor stub
	}

	public boolean isRowHeader() {
		return isRowHeader;
	}

	public void setRowHeader(boolean isRowHeader) {
		this.isRowHeader = isRowHeader;
	}

	public String getTextRowHeader() {
		return textRowHeader;
	}

	public void setTextRowHeader(String textRowHeader) {
		this.textRowHeader = textRowHeader;
	}

	public T getItem() {
		return Item;
	}

	public void setItem(T item) {
		Item = item;
	}

	
}
