package com.github.canisartorus.prospectorjournal.inventory;

import java.util.List;

import com.github.canisartorus.prospectorjournal.ConfigHandler;
import com.github.canisartorus.prospectorjournal.gui.ContainerClientItemBag;

import gregapi.data.CS;
import gregapi.data.CS.SFX;
import gregapi.data.LH;
import gregapi.item.multiitem.MultiItem;
import gregapi.util.ST;
import gregapi.util.UT;
import gregtech.tileentity.inventories.MultiTileEntityDrawerQuad;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public abstract class AbstractInventoryBehavior<E extends MultiItem> extends gregapi.item.multiitem.behaviors.IBehavior.AbstractBehaviorDefault {
	
//	protected final MultiItem mItem;
	
	protected java.util.HashMap<EntityPlayer, Integer> oDefragPoint = new java.util.HashMap<>();
	protected final byte mSize;
	protected final int mStackSize;
	protected final int mCapacity;
	
	public boolean willCollectFromInventory() {return true;}
	public boolean willCollectFromWorld() {return false;}
	protected boolean isEasyToUse() {return false;}
	protected boolean willDeposit() {return true;}
	protected boolean willDepositAll()	{return true;}
	
	public AbstractInventoryBehavior(Number aSize, Number aStackSize, Number aCapacity) {
		mSize = aSize.byteValue();
		int tCapacity = aCapacity.intValue();
		mStackSize = aStackSize.intValue() > 0 ? Math.min( aStackSize.intValue() , tCapacity ) : tCapacity;
		mCapacity = Math.min(tCapacity, mStackSize * mSize);
	}
	public AbstractInventoryBehavior(Number aSize, Number aStackSize) { this(aSize, aStackSize, Integer.MAX_VALUE); }
	public AbstractInventoryBehavior(Number aSize) {					this(aSize, 64		, Integer.MAX_VALUE);}

	@Override public List<String> getAdditionalToolTips(MultiItem aItem, List<String> aList, ItemStack aStack) {
		aList.add(StatCollector.translateToLocal("tooltip.itemhasinventory.name"));
		if(willDeposit()) aList.add(LH.Chat.GRAY + StatCollector.translateToLocal("tooltip.putinbox.name"));
		if(!isEasyToUse()) aList.add(StatCollector.translateToLocal("tooltip.openontable.name"));
		aList.add(LH.Chat.GRAY + StatCollector.translateToLocal("tooltip.defrag.name"));
		return aList;
	}
	
	@Override public boolean onItemUse(MultiItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, byte aSide, float aHitX, float aHitY, float aHitZ) {
		net.minecraft.tileentity.TileEntity tTile = aWorld.getTileEntity(aX, aY, aZ);
		if(willDeposit() && tTile != null && tTile instanceof IInventory) {
			int[] tSlots = CS.ZL_INTEGER;
			// Prefer to get the right drawer as if by hand, not by pipe
			if(tTile instanceof MultiTileEntityDrawerQuad) {	//XXX dependency
				if(aSide == ((gregapi.tileentity.base.TileEntityBase09FacingSingle)tTile).mFacing) {
					float[] tCoords = UT.Code.getFacingCoordsClicked(aSide, aHitX, aHitY, aHitZ);
					int quad = (tCoords[0] > 0.5 ? 1 : 0) + (tCoords[1] > 0.5 ? 2 : 0);
					quad *= 36;
					tSlots = new int[36];
					for(int i = 0; i<36; i++) tSlots[i] = i+quad;
				}
			} else if(tTile instanceof ISidedInventory && aSide != CS.SIDE_INSIDE) {
				tSlots = ((ISidedInventory)tTile).getAccessibleSlotsFromSide(aSide);
			} else {
				tSlots = UT.Code.getAscendingArray(((IInventory)tTile).getSizeInventory());
			}
			return drainInto(aPlayer, aStack, (IInventory)tTile, tSlots);
		}
		return false;
	}
	
	@Override public boolean onItemUseFirst(MultiItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, byte aSide, float hitX, float hitY, float hitZ) {
		if ( aPlayer.isSneaking()) return false;
		if(aSide == CS.SIDE_TOP) {
			if(aWorld.isRemote)	openGUI(aStack, aPlayer);
			return true;
		}
		if (willCollectFromWorld()) {
			if(!aWorld.isRemote) collectBlock(aStack, aPlayer, aWorld, aX, aY, aZ);
			return true;
		}
		return false;
	}
	
	@Override public ItemStack onItemRightClick(MultiItem aItem, ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
		if(!aPlayer.isSneaking() || aWorld.isRemote) {
			if(aWorld.isRemote && isEasyToUse()) openGUI(aStack, aPlayer);
			return aStack;
		}
//		ItemStack rStack = aStack.copy();
		defragment(aItem, aStack, aPlayer);
		return aStack;
	}
	
	@cpw.mods.fml.relauncher.SideOnly(cpw.mods.fml.relauncher.Side.SERVER)
	protected boolean collectBlock(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ) {	return false;	}

	public void openGUI(ItemStack aStack, EntityPlayer aPlayer) {
		net.minecraft.client.Minecraft.getMinecraft().displayGuiScreen(new ContainerClientItemBag(new ContainerCommonItemBag(
				getInventory(aStack, aPlayer), aPlayer, aPlayer.inventory.currentItem)));
	}
	
//	public abstract void spillContents(ItemStack aStack, EntityLivingBase aPlayer);

	/* When an EntityItem in the world is picked up.
	 * i.e. was tossed, exploded, or messy mined.
	 */
//	@cpw.mods.fml.common.eventhandler.SubscribeEvent
//	public void onPickupItem(net.minecraftforge.event.entity.player.EntityItemPickupEvent event) {
//		if(!doCollect()) return;
//		ItemStack tStack = event.item.getEntityItem();
////		if(!isAcceptableItem(tStack)) return;
//	}
	
	public boolean drainInto(EntityPlayer aPlayer, ItemStack aStack, IInventory aTile, int[] aSlots) {
		boolean rChange = false;
		for(ItemStack tStack : getIteration(aStack)) {
			if(tStack.isStackable()) {
				for(int i : aSlots) {
					if(tStack.stackSize > 0) {
						ItemStack tThere = aTile.getStackInSlot(i);
						if(aTile.isItemValidForSlot(i, tStack) && ST.equal(tStack, tThere)) {
							int tSize = tStack.stackSize + tThere.stackSize;
							if(tSize <= tThere.getMaxStackSize()) {
								remove(aStack, tStack);
								tStack.stackSize = 0;
								tThere.stackSize = tSize;
								aTile.setInventorySlotContents(i, tThere);
							} else if (tThere.stackSize < tThere.getMaxStackSize()) {
								tSize = tThere.getMaxStackSize() - tThere.stackSize;
								remove(aStack, ST.make(tStack.getItem(), tSize, tStack.getItemDamage()));
								tStack.stackSize -= tSize;
								tThere.stackSize = tThere.getMaxStackSize();
								aTile.setInventorySlotContents(i, tThere);
							}
						}
					} else break;
				}
			}
			if(tStack.stackSize > 0 && willDepositAll()) {
				for( int i : aSlots) {
					if(aTile.isItemValidForSlot(i, tStack)) {
						ItemStack tThere = aTile.getStackInSlot(i);
						if(tThere == null) {
							tThere = ST.amount(Math.min(tStack.stackSize, tStack.getMaxStackSize()), tStack);
							aTile.setInventorySlotContents(i, tThere);
							remove(aStack, tThere);
							tStack.stackSize -= tThere.stackSize;
						}
					}
					if(tStack.stackSize <= 0) break;
				}
			}
		}
		return rChange;
	}
	public boolean drainInto(EntityPlayer aPlayer, ItemStack aStack, IInventory tTile) { 
		return drainInto(aPlayer, aStack, tTile, UT.Code.getAscendingArray(tTile.getSizeInventory()));
	}

	public boolean defragment(MultiItem aItem, ItemStack aStack, EntityPlayer aPlayer) {
		long tTime = System.nanoTime();
		if(aStack != aPlayer.getCurrentEquippedItem()) return false;
		boolean rChange = false;
		for( int i = oDefragPoint.getOrDefault(aPlayer, 0); i<36; i++) {
			if ((System.nanoTime() - tTime) >= 1000){
				oDefragPoint.put(aPlayer, i);
				UT.Sounds.send(SFX.MC_DIG_GRAVEL, aPlayer);
				if(ConfigHandler.debug) System.out.println(new StringBuilder("InventoryBehavior defragement paused after ").append(System.nanoTime() - tTime));
				return rChange;
			}
			if(i == aPlayer.inventory.currentItem) continue;
			ItemStack tThere = aPlayer.inventory.mainInventory[i];
//			if(net.minecraft.item.Item.getIdFromItem(aItem) == net.minecraft.item.Item.getIdFromItem(tThere.getItem())) {
			if(tThere.getItem() instanceof MultiItem) {
				final MultiItem tItem = (MultiItem) tThere.getItem();
				if(! tItem.mItemBehaviors.containsKey((short)tThere.getItemDamage())) {
					rChange |= grabNonHotbar(aStack, aPlayer, i);
					continue;
				}
				AbstractInventoryBehavior<MultiItem> tOtherInv = null;
				for(gregapi.item.multiitem.behaviors.IBehavior<MultiItem> tBehave : tItem.mItemBehaviors.get((short)tThere.getItemDamage())) {
					if(tBehave instanceof AbstractInventoryBehavior) {
						tOtherInv = (AbstractInventoryBehavior<MultiItem>)tBehave;
						break;
					}
				}
				if(tOtherInv == null) {
					rChange |= grabNonHotbar(aStack, aPlayer, i);
					continue;
				}
				for(ItemStack tThing : this.getIteration(aStack)) {
					if(tOtherInv.isAcceptableItem(tThere, tThing)) {
						for(ItemStack tYours : tOtherInv.getIteration(tThere)) {
//							if(tThing.isItemEqual(tYours) && tThing.getItemDamage() == tYours.getItemDamage() 
							if(ST.equal(tYours, tThing) && tYours.stackSize < tOtherInv.mStackSize) {
								ItemStack iOut = this.remove(aStack, ST.make(tThing.getItem(), Math.min(tOtherInv.mStackSize - tYours.stackSize, tThing.stackSize), tThing.getItemDamage()));
								if(iOut == null || iOut == CS.NI) break;
								int tNum = iOut.stackSize;
								if(tNum == 0) break;
								ItemStack iExtra = tOtherInv.insert(tThere, iOut);
								if(iExtra == null || iExtra == CS.NI || iExtra.stackSize == 0) {
									rChange = true;
									continue;
								}
								if(iExtra.stackSize == tNum) {
									rChange |= 0 == CS.GarbageGT.trash( this.insert(aStack, iExtra) );
								} else {
									CS.GarbageGT.trash( this.insert(aStack, iExtra) );
									rChange = true;
								}
								break;
							}
						}
					}
				}
				continue;
			}
			rChange |= grabNonHotbar(aStack, aPlayer, i);
		}
		UT.Sounds.send(SFX.MC_CLICK, aPlayer);
		oDefragPoint.put(aPlayer, 0);
		if(ConfigHandler.debug) System.out.println(new StringBuilder("InventoryBehavior defragment complete in ").append(System.nanoTime() - tTime));
		return rChange;
	}
	
	private boolean grabNonHotbar(ItemStack aStack, EntityPlayer aPlayer, int i) {
		if (willCollectFromInventory() && i >= InventoryPlayer.getHotbarSize() && isAcceptableItem(aStack, aPlayer.inventory.mainInventory[i])) {
			UT.Inventories.addStackToPlayerInventoryOrDrop(aPlayer, insert(aStack, aPlayer.inventory.mainInventory[i]));
			return true;
		}
		return false;
	}
	
	public boolean isAcceptableItem(ItemStack aStack, ItemStack aTest) {return true;}
	
	protected abstract ItemStack insert(ItemStack aStack, ItemStack aThingToAdd);
	protected abstract ItemStack remove(ItemStack aStack, ItemStack aRequest);
	
	public abstract ItemStack[] getIteration(ItemStack aStack, boolean aVerify);
	public ItemStack[] getIteration(ItemStack aStack) {return getIteration(aStack, false);}
	
	public IInventory getInventory(ItemStack aStack, EntityPlayer aPlayer) {	return new BasicItemInventory(aStack, aPlayer);	}
	
	/*
	 *  volatile adapter to the underlying item storage
	 *  instance inner class has access to instance fields of its enclosing object
	 */
	public class BasicItemInventory implements IInventory {
		final ItemStack mStack;
		final EntityPlayer mPlayer;
		final ItemStack[] mInvDisplay = new ItemStack[mSize];
		boolean mDirt = false;
		
		protected BasicItemInventory(ItemStack aStack, EntityPlayer aPlayer) {
			mStack = aStack;
			mPlayer = aPlayer;
			int i = 0; 
			for (ItemStack fThing : getIteration(aStack, true)) {
				mInvDisplay[i] = fThing;
				i++;
			}
		}
		
		protected void pushChanges() {
//			final ItemStack[] oInventory = getIteration(mStack, false);
//			final ItemStack[] tInventory = new ItemStack[mSize];
			java.util.ArrayList<ItemStack> tNewInv = 
//					new java.util.ArrayList<ItemStack>(mSize);
					UT.Code.getWithoutNulls(mInvDisplay),
//			for(int i = 0; i< mSize; i++) {
//				tInventory[i] = mInvDisplay[i];
//			}
			tOldInv = UT.Code.getWithoutNulls(getIteration(mStack, false));
			for(int i = tNewInv.size() -1 ; i >=0; i--) {
				int j = tOldInv.lastIndexOf(tNewInv.get(i));
				if(j >= 0) {
					tOldInv.remove(j);
					tNewInv.remove(i);
				}
			}
			if(tOldInv.isEmpty() && tNewInv.isEmpty())	{	mDirt = false;	return;	}
			for(ItemStack tRemnant : tOldInv) {
				if(tRemnant == null) continue;
				remove(mStack, tRemnant);
			}
			for(ItemStack tSurprise : tNewInv) {
				if(tSurprise == null) continue;
				insert(mStack, tSurprise);
			}
			mDirt = false;
		}

		@Override	public int getSizeInventory() {	return mSize;	}

		@Override
		public ItemStack getStackInSlot(int iSlot) {	return mInvDisplay[iSlot];	}

		@Override
		public ItemStack decrStackSize(int iSlot, int aNum) {
			if(mInvDisplay[iSlot] == null)	return CS.NI;
			final ItemStack tStack = mInvDisplay[iSlot];
			final ItemStack rStack = remove(mStack, ST.make(tStack.getItem(), Math.min(aNum, tStack.stackSize), tStack.getItemDamage()));
//			mInvDisplay = getIteration(mStack, false);
			if(rStack.stackSize >= tStack.stackSize)	mInvDisplay[iSlot] = CS.NI;
			else mInvDisplay[iSlot].stackSize -= rStack.stackSize;
			return rStack;
		}

		@Override
		public ItemStack getStackInSlotOnClosing(int iSlot) {	return getStackInSlot(iSlot);	}

		@Override
		public void setInventorySlotContents(int iSlot, ItemStack aNew) {	//wrapping code takes the items away
//			CS.GarbageGT.trash(
					remove(mStack, mInvDisplay[iSlot]);
//					);
			insert(mStack, aNew);
//			mInvDisplay = getIteration(mStack, false);
			mInvDisplay[iSlot] = aNew;
		}

		@Override
		public String getInventoryName() {	return null;	}

		@Override
		public boolean hasCustomInventoryName() {	return false;	}

		@Override
		public int getInventoryStackLimit() {	return mStackSize;	}

		@Override
		public void markDirty() {	mDirt = true;	}

		@Override
		public boolean isUseableByPlayer(EntityPlayer aPlayer) {	return aPlayer == mPlayer;	}

		@Override
		public void openInventory() {	;	}

		@Override
		public void closeInventory() {	
			// some edits modify these ItemStacks directly, rather than calling an inventory modification function
			// we will require them to still work properly
			if (mDirt) pushChanges();
		}

		@Override
		public boolean isItemValidForSlot(int iSlot, ItemStack aTest) {	//check for replace-clicks
//			if(mInvDisplay[iSlot] == null) 
			return isAcceptableItem(mStack, aTest);
//			return ST.equal(aTest, mInvDisplay[iSlot]);
		}
		
	}
}
