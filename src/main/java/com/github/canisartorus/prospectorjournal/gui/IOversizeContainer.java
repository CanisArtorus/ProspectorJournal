package com.github.canisartorus.prospectorjournal.gui;

// Allows the ContainerCommon to specify how big its ContainerClient needs to draw the inventory screen.
public interface IOversizeContainer {
//	int mWidth = 176, mHeight = 166;
	public int getSizeX();
	public int getSizeY();
}
