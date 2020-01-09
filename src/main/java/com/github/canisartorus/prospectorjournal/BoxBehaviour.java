package com.github.canisartorus.prospectorjournal;

import java.util.Iterator;
import java.util.List;

import gregapi.item.multiitem.MultiItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BoxBehaviour  extends AbstractInventoryBehavior<gregapi.item.multiitem.MultiItem> {
	public BoxBehaviour(Number aSize, Number aStackSize, Number aCapacity) {
		super(aSize, aStackSize, aCapacity);
	}

	public static final String CONTENTS_TAG = "ca.sampleList";

	@Override public boolean directCollect() {return true;}
	
	@Override
	public boolean isAcceptableItem(ItemStack aStack, ItemStack aTest) {
		return aTest.getUnlocalizedName().equalsIgnoreCase("gt.meta.rockGt");
	}
	
	@Override public List<String> getAdditionalToolTips(MultiItem aItem, List<String> aList, ItemStack aStack) {
		//XXX
		return super.getAdditionalToolTips(aItem, aList, aStack);
	}
	
	@Override
	protected boolean collectBlock(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ) {
		boolean rSuccess = false;
		//XXX
		return rSuccess;
	}

	@Override
	protected ItemStack insert(ItemStack aStack, ItemStack aThingToAdd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ItemStack remove(ItemStack aStack, ItemStack aRequest) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Iterator<ItemStack> iterator() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void openGUI(ItemStack aStack, EntityPlayer aPlayer) {
		// TODO Auto-generated method stub
		
	}

	@Override	public IInventory getInventory(ItemStack aStack, EntityPlayer aPlayer) {return new BoxInventory(aStack, aPlayer);}

	// volatile adapter to the underlying item storage
	public class BoxInventory implements IInventory {
		final ItemStack mStack;
		final EntityPlayer mPlayer;
		
		protected BoxInventory(ItemStack aStack, EntityPlayer aPlayer) {
			mStack = aStack;
			mPlayer = aPlayer;
		}

		@Override	public int getSizeInventory() {	return mSize;	}

		@Override
		public ItemStack getStackInSlot(int p_70301_1_) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getInventoryName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean hasCustomInventoryName() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public int getInventoryStackLimit() {	return mStackSize;	}

		@Override
		public void markDirty() {	;	}

		@Override
		public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void openInventory() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void closeInventory() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
			// TODO Auto-generated method stub
			return false;
		}
		
	}
}
