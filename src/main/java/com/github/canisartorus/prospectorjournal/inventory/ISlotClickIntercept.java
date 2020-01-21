package com.github.canisartorus.prospectorjournal.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public interface ISlotClickIntercept {

	/*
	 * Decide if the Container.slotClick(...) function should get overridden 
	 */
	boolean shouldInterceptClick(Slot aSlot, int aContainerIndex, int aInventoryIndex, EntityPlayer aPlayer, boolean bShift, boolean bMouse,
			int aMouse, int aShift);

	/*
	 * Actually overrides the behavior of Container.slotClick(aContainerIndex, aMouse, aShift, aPlayer)
	 */
	ItemStack slotClick(Slot aSlot, int aContainerIndex, int aInventoryIndex, EntityPlayer aPlayer, boolean bShift, boolean bMouse, int aMouse,
			int aShift);

}
