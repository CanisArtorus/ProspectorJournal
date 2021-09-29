package com.github.canisartorus.prospectorjournal.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;

public abstract class AbstractMenuData {

	// protected boolean obviousEnd = false;
	final String menuName;
	final byte mType;

	AbstractMenuData(String aName, byte aType) {
		menuName = aName;
		mType = aType;
	}

	public abstract void forget();

	public abstract int sortBy(short sortBy, short dimID, int aX, int aZ);

	// String getExhaustName() {
	// 	if (obviousEnd)
	// 		return StatCollector.translateToLocal("btn.delete.name");
	// 	return StatCollector.translateToLocal("btn.exhaust.name");
	// }

	abstract boolean exhaust(int iEntry);

	public abstract void trackCoords(int i);

	@SideOnly(Side.CLIENT)
	abstract void drawDataRow(int aEntry, int aStart, int l, GuiScreen aMenu, FontRenderer FRO, short lastSort);

	abstract java.util.Map<Short, Integer> getSong(int aEntry);

	abstract short getMajor(int aEntry);

}
