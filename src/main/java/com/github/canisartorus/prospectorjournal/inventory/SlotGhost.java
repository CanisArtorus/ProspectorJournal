package com.github.canisartorus.prospectorjournal.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotGhost extends Slot {

	protected final ItemStack mItem;
	
	public SlotGhost(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_, ItemStack aBaseItem) {
		super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
		mItem = aBaseItem;
	}

	/*
	 * Which item should be drawn in an empty slot,
	 * as the ghost image to demonstrate that it is filtered
	 */
	public ItemStack getEmptyItem() {	return mItem;	}

	@Override
	public boolean isItemValid(ItemStack aTest) {
		return this.inventory.isItemValidForSlot(slotNumber, aTest);
	}
}
