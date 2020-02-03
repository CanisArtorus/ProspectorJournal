package com.github.canisartorus.prospectorjournal.inventory;

import com.github.canisartorus.prospectorjournal.lib.Utils;

import gregapi.data.CS;
import gregapi.util.ST;
import gregapi.util.UT;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class BackpackBehaviour extends AbstractInventoryBehavior<gregapi.item.multiitem.MultiItem> {
//TODO capacity limit
	public static final String TAG_INVENTORY = CS.NBT_INV_LIST;
//			"ca.invlist";
	
	public BackpackBehaviour(Number aSize, Number aStackSize, Number aCapacity) {
		super(aSize, aStackSize.intValue() < 64 ? aStackSize : 64, aCapacity);
	}

	public BackpackBehaviour(Number aSize, Number aStackSize) {	
		super(aSize, aStackSize.intValue() < 64 ? aStackSize : 64);	
	}

	public BackpackBehaviour(Number aSize) {	super(aSize);	}

	@Override	protected boolean isEasyToUse() {	return true;	}
	@Override	public boolean willCollectFromInventory() {	return false;	}
	

	@Override
	protected ItemStack insert(ItemStack aStack, ItemStack aThingToAdd) {
		final NBTTagCompound aNBT = UT.NBT.getOrCreate(aStack);
		final NBTTagList aInvTag = aNBT.getTagList(TAG_INVENTORY, Utils.NBT_TYPE_COMPOUND);
		int rIn = aThingToAdd.stackSize;
		for ( int i = 0; i < aInvTag.tagCount(); i++) {
			final ItemStack tThing = ST.load(aInvTag.getCompoundTagAt(i));
			if(ST.equal(aThingToAdd, tThing)) {
				final int tMax = Math.min(mStackSize, tThing.getMaxStackSize());
				if(tThing.stackSize + rIn <= tMax) {
					UT.NBT.setNumber(aInvTag.getCompoundTagAt(i), "Count", tThing.stackSize + rIn);
					return CS.NI;
				} else if (tThing.stackSize < tMax) {
					rIn -= tMax - tThing.stackSize;
					UT.NBT.setNumber(aInvTag.getCompoundTagAt(i), "Count", tMax);
					if (rIn <= 0 ) return CS.NI;	// should never happen
				}
			}
		}
		final ItemStack rOut = aThingToAdd.hasTagCompound() ?
				ST.make(ST.make(aThingToAdd.getItem(), rIn, aThingToAdd.getItemDamage()), aThingToAdd.getTagCompound())
				: ST.make(aThingToAdd.getItem(), rIn, aThingToAdd.getItemDamage());
		if(aInvTag.tagCount() < mSize) {
			aInvTag.appendTag(ST.save(rOut));
			return CS.NI;
		}
		return rOut;
	}

	@Override
	protected ItemStack remove(ItemStack aStack, ItemStack aRequest) {
		if(ST.invalid(aRequest))	return CS.NI;
		final NBTTagCompound aNBT = UT.NBT.getOrCreate(aStack);
		final NBTTagList aInvTag = aNBT.getTagList(TAG_INVENTORY, Utils.NBT_TYPE_COMPOUND);
		int rOut = 0;
		for ( int i = 0; i < aInvTag.tagCount(); i++) {
			final ItemStack tThing = ST.load(aInvTag.getCompoundTagAt(i));
			if(ST.equal(aRequest, tThing)) {
				if(tThing.stackSize == aRequest.stackSize - rOut) {
					aInvTag.removeTag(i);
					return aRequest;
				} else if (tThing.stackSize < aRequest.stackSize - rOut) {
					rOut += tThing.stackSize;
					aInvTag.removeTag(i);
					i--;	// is a List, new content slid into this index.
				} else // if(tThing.stackSize > aRequest.stackSize - rOut)
				{
					final int tLeft = tThing.stackSize + rOut - aRequest.stackSize;
//					tThing.stackSize = tLeft;
					UT.NBT.setNumber(aInvTag.getCompoundTagAt(i), "Count", tLeft);
					return aRequest;
				}
			}
		}
		return aRequest.hasTagCompound() ?
				ST.make(ST.make(aRequest.getItem(), rOut, aRequest.getItemDamage()), aRequest.getTagCompound())
				: ST.make(aRequest.getItem(), rOut, aRequest.getItemDamage());
	}

	@Override
	public ItemStack[] getIteration(ItemStack aStack, boolean aVerify) {
		final NBTTagCompound tNBT = UT.NBT.getOrCreate(aStack);
		final ItemStack[] rInv = new ItemStack[mSize];
		final java.util.List<ItemStack> tLooseBag = new java.util.ArrayList<ItemStack>();
		if(tNBT.hasKey(TAG_INVENTORY)) {
			final NBTTagList tInv = tNBT.getTagList(TAG_INVENTORY, Utils.NBT_TYPE_COMPOUND);
//			final int num = Math.min(tInv.tagCount(), mSize);
			for( int i = 0; i < tInv.tagCount(); i++) {
				final NBTTagCompound tTag = tInv.getCompoundTagAt(i);
				final ItemStack tThing = ST.load(tTag);
				if(ST.invalid(tThing))	continue;
				final short tPlace = tTag.getShort("s");
				if(tTag.hasKey("s") && tPlace < rInv.length && rInv[tPlace] == null) {
					rInv[tPlace] = tThing;
				} else	tLooseBag.add(tThing);
			}
			if(tLooseBag.isEmpty())	return rInv;
			int j = 0;
			for (int i = 0; i < rInv.length; i++) {
				if(rInv[i] == null) {
					rInv[i] = tLooseBag.get(j);
					if(j++ >= tLooseBag.size())	{
						if (! aVerify)
							return rInv;
						break;
					}
				}
			}
			saveInventory(aStack, rInv);
			while (j < tLooseBag.size()) {
				CS.GarbageGT.trash(insert(aStack, tLooseBag.get(j++)));
			}
		}
		return rInv;
	}
	
	protected void saveInventory(ItemStack aStack, ItemStack[] aInv) {
		final NBTTagList tTag = new NBTTagList();
		for(int i = 0; i < aInv.length; i++) {
			if (aInv[i] == null)	continue;
			tTag.appendTag(UT.NBT.makeShort(ST.save(aInv[i]), "s", (short)i));	// adds the short tag to the compound created by save
		}
		final NBTTagCompound tNBT = UT.NBT.getOrCreate(aStack);
		tNBT.setTag(TAG_INVENTORY, tTag);
		UT.NBT.set(aStack, tNBT);
	}

	public IInventory getInventory(ItemStack aStack, EntityPlayer aPlayer) {	return new RealItemInventory(aStack, aPlayer);	}

	public class RealItemInventory implements IInventory {
		final ItemStack mStack;
		final EntityPlayer mPlayer;
		final ItemStack[] mInv;
		final NBTTagCompound mNBT;
		boolean mDirt = false;

		protected RealItemInventory(ItemStack aStack, EntityPlayer aPlayer) {
			mStack = aStack;
			mPlayer = aPlayer;
			mNBT = UT.NBT.getOrCreate(mStack);
			mInv = getIteration(aStack, true);
		}

		@Override	public int getSizeInventory() {	return mSize;	}

		@Override	public ItemStack getStackInSlot(int iSlot) {	return mInv[iSlot];	}

		@Override
		public ItemStack decrStackSize(int iSlot, int aNum) {
			if(mInv[iSlot] == null)	return CS.NI;
			final ItemStack tStack = mInv[iSlot];
			final ItemStack rStack = remove(mStack, ST.make(tStack.getItem(), Math.min(aNum, tStack.stackSize), tStack.getItemDamage()));
			if(rStack.stackSize >= tStack.stackSize)	mInv[iSlot] = CS.NI;
			else mInv[iSlot].stackSize -= rStack.stackSize;
			return rStack;
		}

		@Override
		public ItemStack getStackInSlotOnClosing(int iSlot) {	return getStackInSlot(iSlot);	}

		@Override
		public void setInventorySlotContents(int iSlot, ItemStack aNew) {	//wrapping code takes the items away
			remove(mStack, mInv[iSlot]);
			insert(mStack, aNew);
			mInv[iSlot] = aNew;
		}

		@Override	public String getInventoryName() {	return null;	}

		@Override	public boolean hasCustomInventoryName() {	return false;	}

		@Override	public int getInventoryStackLimit() {	return mStackSize;	}

		@Override	public void markDirty() {	mDirt = true;	}

		@Override	public boolean isUseableByPlayer(EntityPlayer aPlayer) {	return aPlayer == mPlayer;	}

		@Override	public void openInventory() {	;	}

		@Override
		public void closeInventory() {	
			// some edits modify these ItemStacks directly, rather than calling an inventory modification function
			// we will require them to still work properly
			if (mDirt) saveInventory(mStack, mInv);
		}

		@Override	public boolean isItemValidForSlot(int iSlot, ItemStack aTest) {	return isAcceptableItem(mStack, aTest);	}
		
	}
}
