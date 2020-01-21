package com.github.canisartorus.prospectorjournal.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/*
 * From forestry.core.gui.slots.SlotLocked and SlotForestry (c) 2011-2014
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 */
public class SlotLocked extends Slot {

	public SlotLocked(IInventory inventory, int slotIndex, int xPos, int yPos) {
		super(inventory, slotIndex, xPos, yPos);
	}

	@Override
	public boolean canTakeStack(EntityPlayer player) { return false;	}

	@Override
	public void onPickupFromSlot(EntityPlayer player, ItemStack itemStack) {	;	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack) {	return false;	}

	@Override
	public boolean getHasStack() {	return false;	}

	@Override
	public ItemStack decrStackSize(int i) {	return null;	}
	
	@Override
	public void putStack(ItemStack itemStack) {	;	}

}
