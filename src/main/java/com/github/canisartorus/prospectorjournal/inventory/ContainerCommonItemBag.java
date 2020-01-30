package com.github.canisartorus.prospectorjournal.inventory;

import java.util.List;

import com.github.canisartorus.prospectorjournal.gui.IOversizeContainer;

import gregapi.data.CS;
import gregapi.util.ST;
import gregapi.util.UT;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/*
 * Substantially copied from gregapi.gui.ContainerCommon
 * by Gregorius Techneticies
 * Some adaptation by Canis Artorus to an AbstractInventoryBehavior instead of a gregapi.tileentity.ITileEntityInventoryGUI
 */

public class ContainerCommonItemBag extends Container implements IOversizeContainer {
	final int mSlotCount;
	net.minecraft.entity.player.InventoryPlayer mInvPlayer;
	final IInventory mInventory;
	protected int mWidth = 176, mHeight = 166;
	
	public ContainerCommonItemBag(IInventory aInventory, EntityPlayer aPlayer, int currentItem) {
		mInvPlayer = aPlayer.inventory;
		mInventory = aInventory;
		mSlotCount = aInventory.getSizeInventory();
		int[] tOffset = addSlots(mInventory);
		bindPlayerInventory(mInvPlayer, currentItem, tOffset);
		detectAndSendChanges();
	}

	protected int[] addSlots(IInventory aInventory) {
		int i = 0;
		if(useDefaultSlots()) switch(mSlotCount) {
		case  1:
			addSlotAt( i++, 80, 35);
			break;
		case  2:
			addSlotAt( i++, 71, 35);
			addSlotAt( i++, 89, 35);
			break;
		case  3:
			addSlotAt( i++, 62, 35);
			addSlotAt( i++, 80, 35);
			addSlotAt( i++, 98, 35);
			break;
		case  4:
			addSlotAt( i++, 71, 26);
			addSlotAt( i++, 89, 26);
			addSlotAt( i++, 71, 44);
			addSlotAt( i++, 89, 44);
			break;
		case  5:
			addSlotAt( i++, 44, 35);
			addSlotAt( i++, 62, 35);
			addSlotAt( i++, 80, 35);
			addSlotAt( i++, 98, 35);
			addSlotAt( i++,116, 35);
			break;
		case  6:
			addSlotAt( i++, 62, 26);
			addSlotAt( i++, 80, 26);
			addSlotAt( i++, 98, 26);
			addSlotAt( i++, 62, 44);
			addSlotAt( i++, 80, 44);
			addSlotAt( i++, 98, 44);
			break;
		case  7:
			addSlotAt( i++, 26, 35);
			addSlotAt( i++, 44, 35);
			addSlotAt( i++, 62, 35);
			addSlotAt( i++, 80, 35);
			addSlotAt( i++, 98, 35);
			addSlotAt( i++,116, 35);
			addSlotAt( i++,134, 35);
			break;
		case  8:
			addSlotAt( i++, 53, 26);
			addSlotAt( i++, 71, 26);
			addSlotAt( i++, 89, 26);
			addSlotAt( i++,107, 26);
			addSlotAt( i++, 53, 44);
			addSlotAt( i++, 71, 44);
			addSlotAt( i++, 89, 44);
			addSlotAt( i++,107, 44);
			break;
		case  9:
			addSlotAt( i++, 62, 17);
			addSlotAt( i++, 80, 17);
			addSlotAt( i++, 98, 17);
			addSlotAt( i++, 62, 35);
			addSlotAt( i++, 80, 35);
			addSlotAt( i++, 98, 35);
			addSlotAt( i++, 62, 53);
			addSlotAt( i++, 80, 53);
			addSlotAt( i++, 98, 53);
			break;
		// case 11 part of 44
		case 12:
			addSlotAt( i++, 35, 26);
			addSlotAt( i++, 53, 26);
			addSlotAt( i++, 71, 26);
			addSlotAt( i++, 89, 26);
			addSlotAt( i++,107, 26);
			addSlotAt( i++,125, 26);
			addSlotAt( i++, 35, 44);
			addSlotAt( i++, 53, 44);
			addSlotAt( i++, 71, 44);
			addSlotAt( i++, 89, 44);
			addSlotAt( i++,107, 44);
			addSlotAt( i++,125, 44);
			break;
		case 14:
			addSlotAt( i++, 26, 26);
			addSlotAt( i++, 44, 26);
			addSlotAt( i++, 62, 26);
			addSlotAt( i++, 80, 26);
			addSlotAt( i++, 98, 26);
			addSlotAt( i++,116, 26);
			addSlotAt( i++,134, 26);
			addSlotAt( i++, 26, 44);
			addSlotAt( i++, 44, 44);
			addSlotAt( i++, 62, 44);
			addSlotAt( i++, 80, 44);
			addSlotAt( i++, 98, 44);
			addSlotAt( i++,116, 44);
			addSlotAt( i++,134, 44);
			break;
		case 15:
			addSlotAt( i++, 44, 17);
			addSlotAt( i++, 62, 17);
			addSlotAt( i++, 80, 17);
			addSlotAt( i++, 98, 17);
			addSlotAt( i++,116, 17);
			addSlotAt( i++, 44, 35);
			addSlotAt( i++, 62, 35);
			addSlotAt( i++, 80, 35);
			addSlotAt( i++, 98, 35);
			addSlotAt( i++,116, 35);
			addSlotAt( i++, 44, 53);
			addSlotAt( i++, 62, 53);
			addSlotAt( i++, 80, 53);
			addSlotAt( i++, 98, 53);
			addSlotAt( i++,116, 53);
			break;
		case 32:	//ca
			addSlotAt( i++, 17,  8);
			addSlotAt( i++,143,  8);
			addSlotAt( i++, 17, 26);
			addSlotAt( i++,143, 26);
			addSlotAt( i++, 17, 44);
			addSlotAt( i++,143, 44);
			addSlotAt( i++, 17, 62);
			addSlotAt( i++,143, 62);
			//fall through
		case 24:	//ca
			addSlotAt( i++, 35,  8);
			addSlotAt( i++,125,  8);
			addSlotAt( i++, 35, 26);
			addSlotAt( i++,125, 26);
			addSlotAt( i++, 35, 44);
			addSlotAt( i++,125, 44);
			addSlotAt( i++, 35, 62);
			addSlotAt( i++,125, 62);
			// double fall through
		case 16:
			addSlotAt( i++, 53,  8);
			addSlotAt( i++, 71,  8);
			addSlotAt( i++, 89,  8);
			addSlotAt( i++,107,  8);
			addSlotAt( i++, 53, 26);
			addSlotAt( i++, 71, 26);
			addSlotAt( i++, 89, 26);
			addSlotAt( i++,107, 26);
			addSlotAt( i++, 53, 44);
			addSlotAt( i++, 71, 44);
			addSlotAt( i++, 89, 44);
			addSlotAt( i++,107, 44);
			addSlotAt( i++, 53, 62);
			addSlotAt( i++, 71, 62);
			addSlotAt( i++, 89, 62);
			addSlotAt( i++,107, 62);
			break;
		case 18:
			addSlotAt( i++,  8, 26);
			addSlotAt( i++, 26, 26);
			addSlotAt( i++, 44, 26);
			addSlotAt( i++, 62, 26);
			addSlotAt( i++, 80, 26);
			addSlotAt( i++, 98, 26);
			addSlotAt( i++,116, 26);
			addSlotAt( i++,134, 26);
			addSlotAt( i++,152, 26);
			addSlotAt( i++,  8, 44);
			addSlotAt( i++, 26, 44);
			addSlotAt( i++, 44, 44);
			addSlotAt( i++, 62, 44);
			addSlotAt( i++, 80, 44);
			addSlotAt( i++, 98, 44);
			addSlotAt( i++,116, 44);
			addSlotAt( i++,134, 44);
			addSlotAt( i++,152, 44);
			break;
		// case 20 part of 36
		// case 22 part of 44
		// case 24 extends 16
		// case 25 part of 40
		case 27:
			addSlotAt( i++,  8, 17);
			addSlotAt( i++, 26, 17);
			addSlotAt( i++, 44, 17);
			addSlotAt( i++, 62, 17);
			addSlotAt( i++, 80, 17);
			addSlotAt( i++, 98, 17);
			addSlotAt( i++,116, 17);
			addSlotAt( i++,134, 17);
			addSlotAt( i++,152, 17);
			addSlotAt( i++,  8, 35);
			addSlotAt( i++, 26, 35);
			addSlotAt( i++, 44, 35);
			addSlotAt( i++, 62, 35);
			addSlotAt( i++, 80, 35);
			addSlotAt( i++, 98, 35);
			addSlotAt( i++,116, 35);
			addSlotAt( i++,134, 35);
			addSlotAt( i++,152, 35);
			addSlotAt( i++,  8, 53);
			addSlotAt( i++, 26, 53);
			addSlotAt( i++, 44, 53);
			addSlotAt( i++, 62, 53);
			addSlotAt( i++, 80, 53);
			addSlotAt( i++, 98, 53);
			addSlotAt( i++,116, 53);
			addSlotAt( i++,134, 53);
			addSlotAt( i++,152, 53);
			break;
		// case 28 part of 36
		// case 30 part of 40
		// case 32 extends 16
		// case 33 part of 44
		// case 35 part of 40
		case 36:
			addSlotAt( i++,  8,  8);
			addSlotAt( i++,152,  8);
			addSlotAt( i++,  8, 26);
			addSlotAt( i++,152, 26);
			addSlotAt( i++,  8, 44);
			addSlotAt( i++,152, 44);
			addSlotAt( i++,  8, 62);
			addSlotAt( i++,152, 62);
			// fall through
		case 28:	//ca
			addSlotAt( i++, 26,  8);
			addSlotAt( i++,134,  8);
			addSlotAt( i++, 26, 26);
			addSlotAt( i++,134, 26);
			addSlotAt( i++, 26, 44);
			addSlotAt( i++,134, 44);
			addSlotAt( i++, 26, 62);
			addSlotAt( i++,134, 62);
			// double fall through
		case 20:	//ca
			addSlotAt( i++, 44,  8);
			addSlotAt( i++, 62,  8);
			addSlotAt( i++, 80,  8);
			addSlotAt( i++, 98,  8);
			addSlotAt( i++,116,  8);
			addSlotAt( i++, 44, 26);
			addSlotAt( i++, 62, 26);
			addSlotAt( i++, 80, 26);
			addSlotAt( i++, 98, 26);
			addSlotAt( i++,116, 26);
			addSlotAt( i++, 44, 44);
			addSlotAt( i++, 62, 44);
			addSlotAt( i++, 80, 44);
			addSlotAt( i++, 98, 44);
			addSlotAt( i++,116, 44);
			addSlotAt( i++, 44, 62);
			addSlotAt( i++, 62, 62);
			addSlotAt( i++, 80, 62);
			addSlotAt( i++, 98, 62);
			addSlotAt( i++,116, 62);
			break;
		case 45:	//ca
			addSlotAt( i++,152,  8);
			addSlotAt( i++,152, 26);
			addSlotAt( i++,152, 44);
			addSlotAt( i++,152, 62);
			addSlotAt( i++,152, 80);
			// pass on
		case 40:	//ca
			addSlotAt( i++,  8,  8);
			addSlotAt( i++,  8, 26);
			addSlotAt( i++,  8, 44);
			addSlotAt( i++,  8, 62);
			addSlotAt( i++,  8, 80);
			// fall through
		case 35:	//ca
			addSlotAt( i++,134,  8);
			addSlotAt( i++,134, 26);
			addSlotAt( i++,134, 44);
			addSlotAt( i++,134, 62);
			addSlotAt( i++,134, 80);
			// double fall through
		case 30:
			addSlotAt( i++, 26,  8);
			addSlotAt( i++, 26, 26);
			addSlotAt( i++, 26, 44);
			addSlotAt( i++, 26, 62);
			addSlotAt( i++, 26, 80);
			// still falling
		case 25:	//ca
			mHeight = 184;
			addSlotAt( i++, 44,  8);
			addSlotAt( i++, 62,  8);
			addSlotAt( i++, 80,  8);
			addSlotAt( i++, 98,  8);
			addSlotAt( i++,116,  8);
			addSlotAt( i++, 44, 26);
			addSlotAt( i++, 62, 26);
			addSlotAt( i++, 80, 26);
			addSlotAt( i++, 98, 26);
			addSlotAt( i++,116, 26);
			addSlotAt( i++, 44, 44);
			addSlotAt( i++, 62, 44);
			addSlotAt( i++, 80, 44);
			addSlotAt( i++, 98, 44);
			addSlotAt( i++,116, 44);
			addSlotAt( i++, 44, 62);
			addSlotAt( i++, 62, 62);
			addSlotAt( i++, 80, 62);
			addSlotAt( i++, 98, 62);
			addSlotAt( i++,116, 62);
			addSlotAt( i++, 44, 80);
			addSlotAt( i++, 62, 80);
			addSlotAt( i++, 80, 80);
			addSlotAt( i++, 98, 80);
			addSlotAt( i++,116, 80);
			return new int[] {102,  0};
		case 44:	//ca
			addSlotAt( i++,  8, 62);
			addSlotAt( i++, 26, 62);
			addSlotAt( i++, 44, 62);
			addSlotAt( i++, 62, 62);
			addSlotAt( i++, 80, 62);
			addSlotAt( i++, 98, 62);
			addSlotAt( i++,116, 62);
			addSlotAt( i++,134, 62);
			addSlotAt( i++,152, 62);
			addSlotAt( i++,170, 62);
			addSlotAt( i++,188, 62);			
			// fall through
		case 33:	//ca
			addSlotAt( i++,  8,  8);
			addSlotAt( i++, 26,  8);
			addSlotAt( i++, 44,  8);
			addSlotAt( i++, 62,  8);
			addSlotAt( i++, 80,  8);
			addSlotAt( i++, 98,  8);
			addSlotAt( i++,116,  8);
			addSlotAt( i++,134,  8);
			addSlotAt( i++,152,  8);
			addSlotAt( i++,170,  8);
			addSlotAt( i++,188,  8);
			// double fall through
		case 22:	//ca
			addSlotAt( i++,  8, 26);
			addSlotAt( i++, 26, 26);
			addSlotAt( i++, 44, 26);
			addSlotAt( i++, 62, 26);
			addSlotAt( i++, 80, 26);
			addSlotAt( i++, 98, 26);
			addSlotAt( i++,116, 26);
			addSlotAt( i++,134, 26);
			addSlotAt( i++,152, 26);
			addSlotAt( i++,170, 26);
			addSlotAt( i++,188, 26);
			//pass through
		case 11:	//ca
			mWidth = 212;
			addSlotAt( i++,  8, 44);
			addSlotAt( i++, 26, 44);
			addSlotAt( i++, 44, 44);
			addSlotAt( i++, 62, 44);
			addSlotAt( i++, 80, 44);
			addSlotAt( i++, 98, 44);
			addSlotAt( i++,116, 44);
			addSlotAt( i++,134, 44);
			addSlotAt( i++,152, 44);
			addSlotAt( i++,170, 44);
			addSlotAt( i++,188, 44);
			return new int[] { 84, 18};
		// case 45 extends 40			
		case 48:	//ca
			mWidth = 230;
			addSlotAt( i++,  8,  8);
			addSlotAt( i++, 26,  8);
			addSlotAt( i++, 44,  8);
			addSlotAt( i++, 62,  8);
			addSlotAt( i++, 80,  8);
			addSlotAt( i++, 98,  8);
			addSlotAt( i++,116,  8);
			addSlotAt( i++,134,  8);
			addSlotAt( i++,152,  8);
			addSlotAt( i++,170,  8);
			addSlotAt( i++,188,  8);
			addSlotAt( i++,206,  8);
			addSlotAt( i++,  8, 26);
			addSlotAt( i++, 26, 26);
			addSlotAt( i++, 44, 26);
			addSlotAt( i++, 62, 26);
			addSlotAt( i++, 80, 26);
			addSlotAt( i++, 98, 26);
			addSlotAt( i++,116, 26);
			addSlotAt( i++,134, 26);
			addSlotAt( i++,152, 26);
			addSlotAt( i++,170, 26);
			addSlotAt( i++,188, 26);
			addSlotAt( i++,206, 26);
			addSlotAt( i++,  8, 44);
			addSlotAt( i++, 26, 44);
			addSlotAt( i++, 44, 44);
			addSlotAt( i++, 62, 44);
			addSlotAt( i++, 80, 44);
			addSlotAt( i++, 98, 44);
			addSlotAt( i++,116, 44);
			addSlotAt( i++,134, 44);
			addSlotAt( i++,152, 44);
			addSlotAt( i++,170, 44);
			addSlotAt( i++,188, 44);
			addSlotAt( i++,206, 44);
			addSlotAt( i++,  8, 62);
			addSlotAt( i++, 26, 62);
			addSlotAt( i++, 44, 62);
			addSlotAt( i++, 62, 62);
			addSlotAt( i++, 80, 62);
			addSlotAt( i++, 98, 62);
			addSlotAt( i++,116, 62);
			addSlotAt( i++,134, 62);
			addSlotAt( i++,152, 62);
			addSlotAt( i++,170, 62);
			addSlotAt( i++,188, 62);
			addSlotAt( i++,206, 62);			
			return new int[] { 84, 27};
		case 50:	//ca
			mWidth = 194;
			addSlotAt( i++,  8,  8);
			addSlotAt( i++, 26,  8);
			addSlotAt( i++, 44,  8);
			addSlotAt( i++, 62,  8);
			addSlotAt( i++, 80,  8);
			addSlotAt( i++, 98,  8);
			addSlotAt( i++,116,  8);
			addSlotAt( i++,134,  8);
			addSlotAt( i++,152,  8);
			addSlotAt( i++,170,  8);
			addSlotAt( i++,  8, 26);
			addSlotAt( i++, 26, 26);
			addSlotAt( i++, 44, 26);
			addSlotAt( i++, 62, 26);
			addSlotAt( i++, 80, 26);
			addSlotAt( i++, 98, 26);
			addSlotAt( i++,116, 26);
			addSlotAt( i++,134, 26);
			addSlotAt( i++,152, 26);
			addSlotAt( i++,170, 26);
			addSlotAt( i++,  8, 44);
			addSlotAt( i++, 26, 44);
			addSlotAt( i++, 44, 44);
			addSlotAt( i++, 62, 44);
			addSlotAt( i++, 80, 44);
			addSlotAt( i++, 98, 44);
			addSlotAt( i++,116, 44);
			addSlotAt( i++,134, 44);
			addSlotAt( i++,152, 44);
			addSlotAt( i++,170, 44);
			addSlotAt( i++,  8, 62);
			addSlotAt( i++, 26, 62);
			addSlotAt( i++, 44, 62);
			addSlotAt( i++, 62, 62);
			addSlotAt( i++, 80, 62);
			addSlotAt( i++, 98, 62);
			addSlotAt( i++,116, 62);
			addSlotAt( i++,134, 62);
			addSlotAt( i++,152, 62);
			addSlotAt( i++,170, 62);
			addSlotAt( i++,  8, 80);
			addSlotAt( i++, 26, 80);
			addSlotAt( i++, 44, 80);
			addSlotAt( i++, 62, 80);
			addSlotAt( i++, 80, 80);
			addSlotAt( i++, 98, 80);
			addSlotAt( i++,116, 80);
			addSlotAt( i++,134, 80);
			addSlotAt( i++,152, 80);
			addSlotAt( i++,170, 80);
			return new int[] {102, 9};
		case 55:	//ca
			mWidth = 212;
			mHeight = 184;
			addSlotAt( i++,  8,  8);
			addSlotAt( i++, 26,  8);
			addSlotAt( i++, 44,  8);
			addSlotAt( i++, 62,  8);
			addSlotAt( i++, 80,  8);
			addSlotAt( i++, 98,  8);
			addSlotAt( i++,116,  8);
			addSlotAt( i++,134,  8);
			addSlotAt( i++,152,  8);
			addSlotAt( i++,170,  8);
			addSlotAt( i++,188,  8);
			addSlotAt( i++,  8, 26);
			addSlotAt( i++, 26, 26);
			addSlotAt( i++, 44, 26);
			addSlotAt( i++, 62, 26);
			addSlotAt( i++, 80, 26);
			addSlotAt( i++, 98, 26);
			addSlotAt( i++,116, 26);
			addSlotAt( i++,134, 26);
			addSlotAt( i++,152, 26);
			addSlotAt( i++,170, 26);
			addSlotAt( i++,188, 26);
			addSlotAt( i++,  8, 44);
			addSlotAt( i++, 26, 44);
			addSlotAt( i++, 44, 44);
			addSlotAt( i++, 62, 44);
			addSlotAt( i++, 80, 44);
			addSlotAt( i++, 98, 44);
			addSlotAt( i++,116, 44);
			addSlotAt( i++,134, 44);
			addSlotAt( i++,152, 44);
			addSlotAt( i++,170, 44);
			addSlotAt( i++,188, 44);
			addSlotAt( i++,  8, 62);
			addSlotAt( i++, 26, 62);
			addSlotAt( i++, 44, 62);
			addSlotAt( i++, 62, 62);
			addSlotAt( i++, 80, 62);
			addSlotAt( i++, 98, 62);
			addSlotAt( i++,116, 62);
			addSlotAt( i++,134, 62);
			addSlotAt( i++,152, 62);
			addSlotAt( i++,170, 62);
			addSlotAt( i++,188, 62);			
			addSlotAt( i++,  8, 80);
			addSlotAt( i++, 26, 80);
			addSlotAt( i++, 44, 80);
			addSlotAt( i++, 62, 80);
			addSlotAt( i++, 80, 80);
			addSlotAt( i++, 98, 80);
			addSlotAt( i++,116, 80);
			addSlotAt( i++,134, 80);
			addSlotAt( i++,152, 80);
			addSlotAt( i++,170, 80);
			addSlotAt( i++,188, 80);			
			return new int[] {102, 18};
		case 60:	//ca
			mWidth = 230;
			mHeight = 184;
			addSlotAt( i++,  8,  8);
			addSlotAt( i++, 26,  8);
			addSlotAt( i++, 44,  8);
			addSlotAt( i++, 62,  8);
			addSlotAt( i++, 80,  8);
			addSlotAt( i++, 98,  8);
			addSlotAt( i++,116,  8);
			addSlotAt( i++,134,  8);
			addSlotAt( i++,152,  8);
			addSlotAt( i++,170,  8);
			addSlotAt( i++,188,  8);
			addSlotAt( i++,206,  8);
			addSlotAt( i++,  8, 26);
			addSlotAt( i++, 26, 26);
			addSlotAt( i++, 44, 26);
			addSlotAt( i++, 62, 26);
			addSlotAt( i++, 80, 26);
			addSlotAt( i++, 98, 26);
			addSlotAt( i++,116, 26);
			addSlotAt( i++,134, 26);
			addSlotAt( i++,152, 26);
			addSlotAt( i++,170, 26);
			addSlotAt( i++,188, 26);
			addSlotAt( i++,206, 26);
			addSlotAt( i++,  8, 44);
			addSlotAt( i++, 26, 44);
			addSlotAt( i++, 44, 44);
			addSlotAt( i++, 62, 44);
			addSlotAt( i++, 80, 44);
			addSlotAt( i++, 98, 44);
			addSlotAt( i++,116, 44);
			addSlotAt( i++,134, 44);
			addSlotAt( i++,152, 44);
			addSlotAt( i++,170, 44);
			addSlotAt( i++,188, 44);
			addSlotAt( i++,206, 44);
			addSlotAt( i++,  8, 62);
			addSlotAt( i++, 26, 62);
			addSlotAt( i++, 44, 62);
			addSlotAt( i++, 62, 62);
			addSlotAt( i++, 80, 62);
			addSlotAt( i++, 98, 62);
			addSlotAt( i++,116, 62);
			addSlotAt( i++,134, 62);
			addSlotAt( i++,152, 62);
			addSlotAt( i++,170, 62);
			addSlotAt( i++,188, 62);
			addSlotAt( i++,206, 62);			
			addSlotAt( i++,  8, 80);
			addSlotAt( i++, 26, 80);
			addSlotAt( i++, 44, 80);
			addSlotAt( i++, 62, 80);
			addSlotAt( i++, 80, 80);
			addSlotAt( i++, 98, 80);
			addSlotAt( i++,116, 80);
			addSlotAt( i++,134, 80);
			addSlotAt( i++,152, 80);
			addSlotAt( i++,170, 80);
			addSlotAt( i++,188, 80);
			addSlotAt( i++,206, 80);
			return new int[] {102, 27};
		}
		return new int[] { 84,  0};
	}

	protected void addSlotAt(int aIndex, int aX, int aY) {
		addSlotToContainer(new Slot(mInventory, aIndex, aX, aY));
	}
	
	// merged with vazkii.botania.client.gui.bag.ContainerFlowerBag()
	// also locks the slot the item with this inventory is (probably) in.
	protected void bindPlayerInventory(InventoryPlayer aInventoryPlayer, int currentItem, int[] aOffset) {
		for (int i = 0; i < 3; i++) for (int j = 0; j < 9; j++) {
			addSlotToContainer(new Slot(aInventoryPlayer, j + i * 9 + 9, aOffset[1] + 8 + j * 18, aOffset[0] + i * 18));
		}
		for (int i = 0; i < 9; i++) {
			if(i == currentItem) addSlotToContainer(new SlotLocked(aInventoryPlayer, i, aOffset[1] + 8 + i * 18, aOffset[0] + 58));
			else addSlotToContainer(new Slot(aInventoryPlayer, i, aOffset[1] + 8 + i * 18, aOffset[0] + 58));
		}
	}
	
	protected int getAllSlotCount() {	return getSlotCount() + getStartIndex();	}
	protected int getShiftSlotCount() {	return getSlotCount();	}
	protected int getShiftStartIndex() {	return getStartIndex();	}
	protected int getSlotCount() {	return mInventory.getSizeInventory();	}
	protected int getStartIndex() {	return 0;	}
	protected boolean useDefaultSlots() {	return true;	}

	@Override public int getSizeX() {	return mWidth;	}
	@Override public int getSizeY() {	return mHeight;	}

	@Override
	public ItemStack slotClick(int aIndex, int aMouse, int aShift, EntityPlayer aPlayer) {
		Slot aSlot = (aIndex >= 0 && aIndex < inventorySlots.size()) ? (Slot)inventorySlots.get(aIndex) : null;
		
		try {
			if (aSlot != null && mInventory instanceof ISlotClickIntercept && ((ISlotClickIntercept)mInventory).shouldInterceptClick(aSlot, aIndex, aSlot.getSlotIndex(), aPlayer, aShift == 1, aMouse != 0, aMouse, aShift)) {
				ItemStack rStack = ((ISlotClickIntercept)mInventory).slotClick(aSlot, aIndex, aSlot.getSlotIndex(), aPlayer, aShift == 1, aMouse != 0, aMouse, aShift);
				detectAndSendChanges();
				return rStack;
			}
		} catch (Throwable e) {e.printStackTrace(CS.ERR); return null;}
		
		if (aIndex >= 0) {
			if (aSlot == null || aSlot instanceof SlotLocked) return null;
			if (aIndex < getAllSlotCount()) if (aIndex < getStartIndex() || aIndex >= getStartIndex() + getSlotCount()) return null;
		}
		
		try {return super.slotClick(aIndex, aMouse, aShift, aPlayer);} catch (Throwable e) {e.printStackTrace(CS.ERR);}
		
		ItemStack rStack = null;
		InventoryPlayer aPlayerInventory = aPlayer.inventory;
		ItemStack tTempStack;
		int tTempStackSize;
		ItemStack aHoldStack;
		
		if ((aShift == 0 || aShift == 1) && (aMouse == 0 || aMouse == 1)) {
			if (aIndex == -999) {
				if (aPlayerInventory.getItemStack() != null && aIndex == -999) {
					if (aMouse == 0) {
						aPlayer.dropPlayerItemWithRandomChoice(aPlayerInventory.getItemStack(), true);
						aPlayerInventory.setItemStack(null);
					}
					if (aMouse == 1) {
						aPlayer.dropPlayerItemWithRandomChoice(aPlayerInventory.getItemStack().splitStack(1), true);
						if (aPlayerInventory.getItemStack().stackSize == 0) {
							aPlayerInventory.setItemStack(null);
						}
					}
				}
			} else if (aShift == 1) {
				if (aSlot != null && aSlot.canTakeStack(aPlayer)) {
					tTempStack = transferStackInSlot(aPlayer, aIndex);
					if (tTempStack != null) {
						rStack = ST.copy(tTempStack);
						if (aSlot.getStack() != null && aSlot.getStack().getItem() == tTempStack.getItem()) {
							slotClick(aIndex, aMouse, aShift, aPlayer);
						}
					}
				}
			} else {
				if (aIndex < 0) {
					return null;
				}
				if (aSlot != null) {
					tTempStack = aSlot.getStack();
					ItemStack tHeldStack = aPlayerInventory.getItemStack();
					if (tTempStack != null) {
						rStack = ST.copy(tTempStack);
					}
					if (tTempStack == null) {
						if (tHeldStack != null && aSlot.inventory.isItemValidForSlot(aSlot.getSlotIndex(), tHeldStack) && aSlot.isItemValid(tHeldStack)) {
							tTempStackSize = aMouse == 0 ? tHeldStack.stackSize : 1;
							if (tTempStackSize > aSlot.getSlotStackLimit()) {
								tTempStackSize = aSlot.getSlotStackLimit();
							}
							aSlot.putStack(tHeldStack.splitStack(tTempStackSize));

							if (tHeldStack.stackSize == 0) {
								aPlayerInventory.setItemStack(null);
							}
						}
					} else if (aSlot.canTakeStack(aPlayer)) {
						if (tHeldStack == null) {
							tTempStackSize = aMouse == 0 ? tTempStack.stackSize : (tTempStack.stackSize + 1) / 2;
							aHoldStack = aSlot.decrStackSize(tTempStackSize);
							aPlayerInventory.setItemStack(aHoldStack);
							if (tTempStack.stackSize == 0) {
								aSlot.putStack(null);
							}
							aSlot.onPickupFromSlot(aPlayer, aPlayerInventory.getItemStack());
						} else if (aSlot.inventory.isItemValidForSlot(aSlot.getSlotIndex(), tHeldStack) && aSlot.isItemValid(tHeldStack)) {
							if (tTempStack.getItem() == tHeldStack.getItem() && tTempStack.getItemDamage() == tHeldStack.getItemDamage() && ItemStack.areItemStackTagsEqual(tTempStack, tHeldStack)) {
								tTempStackSize = aMouse == 0 ? tHeldStack.stackSize : 1;
								if (tTempStackSize > aSlot.getSlotStackLimit() - tTempStack.stackSize) {
									tTempStackSize = aSlot.getSlotStackLimit() - tTempStack.stackSize;
								}
								if (tTempStackSize > tHeldStack.getMaxStackSize() - tTempStack.stackSize) {
									tTempStackSize = tHeldStack.getMaxStackSize() - tTempStack.stackSize;
								}
								tHeldStack.splitStack(tTempStackSize);
								if (tHeldStack.stackSize == 0) {
									aPlayerInventory.setItemStack(null);
								}
								tTempStack.stackSize += tTempStackSize;
							} else if (tHeldStack.stackSize <= aSlot.getSlotStackLimit()) {
								aSlot.putStack(tHeldStack);
								aPlayerInventory.setItemStack(tTempStack);
							}
						} else if (tTempStack.getItem() == tHeldStack.getItem() && tHeldStack.getMaxStackSize() > 1 && (!tTempStack.getHasSubtypes() || tTempStack.getItemDamage() == tHeldStack.getItemDamage()) && ItemStack.areItemStackTagsEqual(tTempStack, tHeldStack)) {
							tTempStackSize = tTempStack.stackSize;

							if (tTempStackSize > 0 && tTempStackSize + tHeldStack.stackSize <= tHeldStack.getMaxStackSize()) {
								tHeldStack.stackSize += tTempStackSize;
//								tTempStack = 									// re-directed to amount pulled out (not a stack anywhere), test is incorrect
										aSlot.decrStackSize(tTempStackSize);	//BUG!!!
//								tTempStack.stackSize -= tTempStackSize;

//								if (tTempStack.stackSize == 0) {					//BUG!!!
								if(aSlot.getStack().stackSize == 0) {
									aSlot.putStack(null);
								}

								aSlot.onPickupFromSlot(aPlayer, aPlayerInventory.getItemStack());
							}
						}
					}
					aSlot.onSlotChanged();
				}
			}
		} else if (aShift == 2 && aMouse >= 0 && aMouse < 9) {	// press a hotbar key
			if (aSlot.canTakeStack(aPlayer)) {
				tTempStack = aPlayerInventory.getStackInSlot(aMouse);
				boolean var9 = tTempStack == null || aSlot.inventory == aPlayerInventory && aSlot.isItemValid(tTempStack);
				tTempStackSize = -1;

				if (!var9) {
					tTempStackSize = aPlayerInventory.getFirstEmptyStack();
					var9 |= tTempStackSize > -1;
				}

				if (aSlot.getHasStack() && var9) {
					aHoldStack = aSlot.getStack();
					aPlayerInventory.setInventorySlotContents(aMouse, aHoldStack);

					if ((aSlot.inventory != aPlayerInventory || !aSlot.isItemValid(tTempStack)) && tTempStack != null) {
						if (tTempStackSize > -1) {
							aPlayerInventory.addItemStackToInventory(tTempStack);
//							aSlot.decrStackSize(aHoldStack.stackSize);
							aSlot.putStack(null);
							aSlot.onPickupFromSlot(aPlayer, aHoldStack);
						} else if(aSlot.inventory.isItemValidForSlot(aSlot.getSlotIndex(), tTempStack) && aSlot.isItemValid(tTempStack)) {	//edge cases	!ca
							aSlot.putStack(tTempStack);
						} else {
							aPlayer.dropPlayerItemWithRandomChoice(tTempStack, true);
						}
					} else {
//						aSlot.decrStackSize(aHoldStack.stackSize);
						aSlot.putStack(tTempStack);
						aSlot.onPickupFromSlot(aPlayer, aHoldStack);
					}
				} else if (!aSlot.getHasStack() && tTempStack != null && aSlot.inventory.isItemValidForSlot(aSlot.getSlotIndex(), tTempStack) && aSlot.isItemValid(tTempStack)) {
					aPlayerInventory.setInventorySlotContents(aMouse, null);
					aSlot.putStack(tTempStack);
				}
			}
		} else if (aShift == 3 && UT.Entities.hasInfiniteItems(aPlayer) && aPlayerInventory.getItemStack() == null && aIndex >= 0) {
			// use the pick block key
			if (aSlot != null && aSlot.getHasStack()) {
				tTempStack = ST.copy(aSlot.getStack());
				tTempStack.stackSize = tTempStack.getMaxStackSize();
				aPlayerInventory.setItemStack(tTempStack);
			}
		}
		return rStack;
	}


	@Override
	public ItemStack transferStackInSlot(EntityPlayer aPlayer, int aIndex) {
		ItemStack rStack = null;
		Slot tSlot = (Slot)inventorySlots.get(aIndex);
		
		// null checks and checks if the item can be stacked (maxStackSize > 1)
		if (getSlotCount() > 0 && tSlot != null && tSlot.getHasStack() && !(tSlot instanceof SlotLocked)) {
			ItemStack tStack = tSlot.getStack();
			rStack = ST.copy(tStack);
			
			// BagItem -> Player
			if (aIndex < getAllSlotCount()) {
				if ( !mergeItemStack(tStack, getAllSlotCount(), getAllSlotCount()+36, true)) {
					return null;
				}
			// Player -> BagItem
			} else if (!mergeItemStack(tStack, getShiftStartIndex(), getShiftStartIndex()+getShiftSlotCount(), false)) {
				return null;
			}
			
			if (tStack.stackSize == 0) tSlot.putStack(null); else tSlot.onSlotChanged();

			// from vazkii.botania.client.gui.bag.ContainerFlowerBag.transferStackInSlot(...)
			if(rStack.stackSize == tStack.stackSize) return null;
		}
		return rStack;
	}
	
	@Override
	protected boolean mergeItemStack(ItemStack aStack, int aStartIndex, int aEndIndex, boolean aReverse) {
		boolean rSuccess = false;
		int tIndex = aReverse?aEndIndex-1:aStartIndex;
		
		if (aStack.isStackable()) {
			while (aStack.stackSize > 0 && (aReverse ? tIndex >= aStartIndex : tIndex < aEndIndex)) {
				Slot tSlot = (Slot)inventorySlots.get(tIndex);
				int tLimit = Math.min(aStack.getMaxStackSize(), tSlot.getSlotStackLimit());
				ItemStack tStack = tSlot.getStack();
				if (!(tSlot instanceof SlotLocked) && tSlot.isItemValid(aStack) && ST.equal(aStack, tStack)) {
					int tSize = tStack.stackSize + aStack.stackSize;
					if (tSize <= tLimit) {
						aStack.stackSize = 0;
						tStack.stackSize = tSize;
						tSlot.onSlotChanged();
						rSuccess = true;
					} else if (tStack.stackSize < tLimit) {
						aStack.stackSize -= tLimit - tStack.stackSize;
						tStack.stackSize = tLimit;
						tSlot.onSlotChanged();
						rSuccess = true;
					}
				}
				if (aReverse) tIndex--; else tIndex++;
			}
		}
		if (aStack.stackSize > 0) {
			if (aReverse) tIndex = aEndIndex - 1; else tIndex = aStartIndex;
			while (aReverse ? tIndex >= aStartIndex : tIndex < aEndIndex) {
				Slot tSlot = (Slot)inventorySlots.get(tIndex);
				if (!(tSlot instanceof SlotLocked) && tSlot.isItemValid(aStack)) {
					ItemStack tStack = tSlot.getStack();
					if (tStack == null) {
						tStack = ST.amount(Math.min(aStack.stackSize, Math.min(aStack.getMaxStackSize(), tSlot.getSlotStackLimit())), aStack);
						tSlot.putStack(tStack);
						tSlot.onSlotChanged();
						aStack.stackSize -= tStack.stackSize;
						rSuccess = true;
						if (aStack.stackSize <= 0) break;
					}
				}
				if (aReverse) tIndex--; else tIndex++;
			}
		}
		return rSuccess;
	}
	

	//----------------- greg's crash prevention of vanilla methods -------------------------//
	@Override
	public void addCraftingToCrafters(ICrafting p_75132_1_) {
		try {
			super.addCraftingToCrafters(p_75132_1_);
		} catch (Throwable e) {
			e.printStackTrace(CS.ERR);
		}
	}

//	@Override
//	protected Slot addSlotToContainer(Slot aSlot) {
//		if (aSlot == null) return null;
//		return super.addSlotToContainer(aSlot);
//	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {	return mInventory.isUseableByPlayer(p_75145_1_);	}
	
	@Override
	public void detectAndSendChanges() {
		try {
			super.detectAndSendChanges();
		} catch (Throwable e) {
			e.printStackTrace(CS.ERR);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ItemStack> getInventory() {
		try {
			return super.getInventory();
		} catch (Throwable e) {
			e.printStackTrace(CS.ERR);
		}
		return null;
	}

	@Override
	public Slot getSlot(int aIndex) {
		try {
			if (aIndex < inventorySlots.size()) return (Slot)inventorySlots.get(aIndex);
		} catch(Throwable e) {
			e.printStackTrace(CS.ERR);
		}
		return null;
	}

	@Override
	public Slot getSlotFromInventory(IInventory p_75147_1_, int p_75147_2_) {
		try {
			return super.getSlotFromInventory(p_75147_1_, p_75147_2_);
		} catch(Throwable e) {
			e.printStackTrace(CS.ERR);
		}
		return null;
	}

	@Override
	public void onContainerClosed(EntityPlayer p_75134_1_) {
		try {
			super.onContainerClosed(p_75134_1_);
		} catch(Throwable e) {
			e.printStackTrace(CS.ERR);
		}
	}

	@Override
	public void putStackInSlot(int aIndex, ItemStack aStack) {
		try {
			super.putStackInSlot(aIndex, aStack);
//			getSlot(aIndex).putStack(aStack);
		} catch(Throwable e) {
			e.printStackTrace(CS.ERR);
		}
	}

	@Override
	public void putStacksInSlots(ItemStack[] aStacks) {		
		try {
//			for (int i = 0; i < aStacks.length; ++i) getSlot(i).putStack(aStacks[i]);
			super.putStacksInSlots(aStacks);
		} catch(Throwable e) {
			e.printStackTrace(CS.ERR);
		}
	}

	@Override
	public void removeCraftingFromCrafters(ICrafting p_82847_1_) {
		try {
			super.removeCraftingFromCrafters(p_82847_1_);
		} catch(Throwable e) {
			e.printStackTrace(CS.ERR);
		}
	}

	@Override
	protected void retrySlotClick(int aIndex, int aMouse, boolean p_75133_3_, EntityPlayer aPlayer) {
		try {
			slotClick(aIndex, aMouse, 1, aPlayer);
		} catch(Throwable e) {
			e.printStackTrace(CS.ERR);
		}
	}
	
}
