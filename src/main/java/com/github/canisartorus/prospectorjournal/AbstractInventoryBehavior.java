package com.github.canisartorus.prospectorjournal;

import java.util.List;

import gregapi.data.CS;
import gregapi.item.multiitem.MultiItem;
import gregapi.util.ST;
import gregapi.util.UT;
import gregtech.tileentity.inventories.MultiTileEntityDrawerQuad;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class AbstractInventoryBehavior<E extends MultiItem> extends gregapi.item.multiitem.behaviors.IBehavior.AbstractBehaviorDefault
	implements Iterable<ItemStack> {
	
//	protected final MultiItem mItem;
	
	protected java.util.HashMap<EntityPlayer, Integer> oDefragPoint = new java.util.HashMap<>();
	protected byte mSize = 8;
	protected int mStackSize = 64;
	protected int mCapacity = Integer.MAX_VALUE;
	
	public boolean doCollect() {return true;}
	public boolean directCollect() {return false;}
	protected boolean isEasyToUse() {return false;}
	protected boolean doDeposit() {return true;}
	
	public AbstractInventoryBehavior(Number aSize, Number aStackSize, Number aCapacity) {
		mSize = (byte) aSize;
		mStackSize = (int) aStackSize;
		mCapacity = (int) aCapacity;
	}

	@Override public List<String> getAdditionalToolTips(MultiItem aItem, List<String> aList, ItemStack aStack) {
		//XXX
		return aList;
	}
	
	@Override public boolean onItemUse(MultiItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, byte aSide, float aHitX, float aHitY, float aHitZ) {
		if (!doDeposit()) return false;
		net.minecraft.tileentity.TileEntity tTile = aWorld.getTileEntity(aX, aY, aZ);
		if(tTile != null && tTile instanceof IInventory) {
			int[] tSlots = CS.ZL_INTEGER;
			// Prefer to get the right drawer as if by hand, not by pipe
			if(tTile instanceof gregtech.tileentity.inventories.MultiTileEntityDrawerQuad) {
				if(aSide == ((gregapi.tileentity.base.TileEntityBase09FacingSingle)tTile).mFacing) {
					float[] tCoords = UT.Code.getFacingCoordsClicked(aSide, aHitX, aHitY, aHitZ);
					int quad = (tCoords[0] > 0.5 ? 1 : 0) + (tCoords[1] > 0.5 ? 2 : 0);
					quad *= 36;
					tSlots = new int[36];
					for(int i = 0; i<36; i++) tSlots[i] = i+quad;
				}
			} else if(tTile instanceof ISidedInventory) {
				if (aSide != CS.SIDE_INSIDE)
					tSlots = ((ISidedInventory)tTile).getAccessibleSlotsFromSide(aSide);
			} else {
				tSlots = UT.Code.getAscendingArray(((IInventory)tTile).getSizeInventory());
			}
			return drainInto(aPlayer, aStack, (IInventory)tTile, tSlots);
		}
		return false;
	}
	
	@Override public boolean onItemUseFirst(MultiItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, byte aSide, float hitX, float hitY, float hitZ) {
		if (aPlayer.isSneaking()) return false;
		if (directCollect() && ! aWorld.isRemote) {
			return collectBlock(aStack, aPlayer, aWorld, aX, aY, aZ);
		}
		if (aSide == CS.SIDE_TOP) {
			if(aWorld.isRemote) {
				openGUI(aStack, aPlayer);
				return true;
			}
		}
		return false;
	}
	
	@Override public ItemStack onItemRightClick(MultiItem aItem, ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
		if(!aPlayer.isSneaking() || aWorld.isRemote) {
			if(aWorld.isRemote && isEasyToUse()) openGUI(aStack, aPlayer);
			return aStack;
		}
		ItemStack rStack = aStack.copy();
		if(defragment(aItem, aStack, aPlayer))
			return rStack;
		return aStack;
	}
	
	protected abstract boolean collectBlock(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ);

	public abstract void openGUI(ItemStack aStack, EntityPlayer aPlayer);
	
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
	
	public boolean drainInto(EntityPlayer aPlayer, ItemStack aStack, IInventory tTile, int[] tSlots) {
		boolean rChange = false;
		//XXX
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
				//XXX play in-progress sound
				if(ConfigHandler.debug) System.out.println(new StringBuilder("InventoryBehavior defragement paused after ").append(System.nanoTime() - tTime));
				return rChange;
			}
			if(i == aPlayer.inventory.currentItem) continue;
			ItemStack tThere = aPlayer.inventory.mainInventory[i];
			if(net.minecraft.item.Item.getIdFromItem(aItem) == net.minecraft.item.Item.getIdFromItem(tThere.getItem())) {
				if(! aItem.mItemBehaviors.containsKey((short)tThere.getItemDamage())) continue;
				AbstractInventoryBehavior<MultiItem> tOtherInv = null;
				for(gregapi.item.multiitem.behaviors.IBehavior<MultiItem> tBehave : aItem.mItemBehaviors.get((short)tThere.getItemDamage())) {
					if(tBehave instanceof AbstractInventoryBehavior) {
						tOtherInv = (AbstractInventoryBehavior<MultiItem>)tBehave;
						break;
					}
				}
				if(tOtherInv == null) continue;
				for(ItemStack tThing : this) {
					if(tOtherInv.isAcceptableItem(tThere, tThing)) {
						for(ItemStack tYours : tOtherInv) {
							if(tThing.isItemEqual(tYours) && tThing.getItemDamage() == tYours.getItemDamage() && tYours.stackSize < tOtherInv.mStackSize) {
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
			}
		}
		//XXX play finished sound
		oDefragPoint.put(aPlayer, 0);
		if(ConfigHandler.debug) System.out.println(new StringBuilder("InventoryBehavior defragment complete in ").append(System.nanoTime() - tTime));
		return rChange;
	}
	
	public boolean isAcceptableItem(ItemStack aStack, ItemStack aTest) {return true;}
	
	protected abstract ItemStack insert(ItemStack aStack, ItemStack aThingToAdd);
	protected abstract ItemStack remove(ItemStack aStack, ItemStack aRequest);
	
//	@Override public abstract java.util.Iterator<ItemStack> iterator();
	
	public abstract IInventory getInventory(ItemStack aStack, EntityPlayer aPlayer);
	
}
