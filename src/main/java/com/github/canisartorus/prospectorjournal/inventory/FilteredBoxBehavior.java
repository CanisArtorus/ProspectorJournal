package com.github.canisartorus.prospectorjournal.inventory;

import java.util.Map.Entry;

import gregapi.data.CS;
import gregapi.data.MD;
import gregapi.item.multiitem.MultiItem;
import gregapi.util.ST;
import gregapi.util.UT;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * An InventoryBehavior that uses NBT tag int[] to store items by meta,
 * best used when very few different Items will be stored, and they have many simple sub-types.
 * Completely ignores and forgets NBT tags.
 * Currently only works for gregtech items (all prefix items, and most multiItemRandom)
 * @author Alexander James
 */
public class FilteredBoxBehavior extends AbstractInventoryBehavior<MultiItem> {

	public java.util.HashMap<String, String[]> NAME_TAGS = new java.util.HashMap<>();
	
	//total capacity tracking tags
	public static final String TAG_CAPACITY = "ca.ItemFilled";
	public static final String TAG_STACKS = "ca.StackFilled";

	public FilteredBoxBehavior(Number aSize) {
		super(aSize);
	}
	public FilteredBoxBehavior(Number aSize, Number aStackSize, Number aCapacity) {
		super(aSize, aStackSize, aCapacity);
	}

	public FilteredBoxBehavior(Number aSize, Number aStackSize) {
		super(aSize, aStackSize);
	}

	private static void backFill(int[] aTypes, int[] aAmounts, final int index) {
		int j = 0;
		for (int i = index; i< aTypes.length-j && i< aAmounts.length-j; i++) {
			if(aAmounts[i+j] <= 0) {
				i--;
				j++;
				continue;
			}
			aTypes[i] = aTypes[i+j];
			aAmounts[i] = aAmounts[i+j];
		}
	}

	@Override
	public boolean isAcceptableItem(ItemStack aStack, ItemStack aTest) {
//		return aTest.getUnlocalizedName().equalsIgnoreCase("gt.meta.rockGt") || (mFire != 0 && aTest.getUnlocalizedName().equalsIgnoreCase("gt.meta.chunkGt"));
		return NAME_TAGS.containsKey(aTest.getUnlocalizedName());
	}
	
	/**
	 * A better way to add valid items to your bag, without needing a subclass constructor.
	 * @param sUnlocalized	The unlocalized name of the item to accept and store
	 * @param tagMetaList	the nbtTag title to put the sub-id list at
	 * @param tagCountList	the nbtTag title to put the amounts list at
	 * @return this, modified
	 */
	public FilteredBoxBehavior registerItem(String sUnlocalized, String tagMetaList, String tagCountList) {
		this.NAME_TAGS.put(sUnlocalized, new String[] {tagMetaList, tagCountList});
		return this;
	}
	
	public boolean deRegisterItem(String sUnlocalized) {
		return null != this.NAME_TAGS.remove(sUnlocalized);
	}

	@Override
	protected ItemStack insert(ItemStack aStack, final ItemStack aThingToAdd) {
			if ( ! isAcceptableItem(aStack, aThingToAdd)) return aThingToAdd;
	//		if( ! aStack.hasTagCompound() ) aStack.setTagCompound(new NBTTagCompound());
	//		NBTTagCompound oTag = aStack.getTagCompound();
			NBTTagCompound oTag = UT.NBT.getOrCreate(aStack);
			int oCapacity = oTag.hasKey(TAG_CAPACITY) ? oTag.getInteger(TAG_CAPACITY) : 0;
			if ( oCapacity >= mCapacity) return aThingToAdd;
			byte oStacks = oTag.hasKey(TAG_STACKS) ? oTag.getByte(TAG_STACKS) : 0;
			int[] oAmounts = CS.ZL_INTEGER, oTypes = CS.ZL_INTEGER;
			final String type_tag = NAME_TAGS.get(aThingToAdd.getUnlocalizedName())[0];
			final String num_tag = NAME_TAGS.get(aThingToAdd.getUnlocalizedName())[1];
	//		if (aThingToAdd.getUnlocalizedName().equalsIgnoreCase("gt.meta.rockGt") ) {
	//			oTypes = oTag.hasKey(TAG_ROCKS) ? oTag.getIntArray(TAG_ROCKS) : CS.ZL_INTEGER;
	//			oAmounts = oTag.hasKey(TAG_ROCKS_N) ? oTag.getIntArray(TAG_ROCKS_N) : CS.ZL_INTEGER;
	//		} else if (aThingToAdd.getUnlocalizedName().equalsIgnoreCase("gt.meta.chunkGt")) {
				oTypes = oTag.hasKey(type_tag) ? oTag.getIntArray(type_tag) : CS.ZL_INTEGER;
				oAmounts = oTag.hasKey(num_tag) ? oTag.getIntArray(num_tag) : CS.ZL_INTEGER;
	//		}
			ItemStack rStack = aThingToAdd.copy();
			boolean tDirty = false;
			for (int i = 0; i< oTypes.length && rStack.stackSize > 0; i++) {
	//			if(oAmounts[i] == 0) {	// how?
	//				backFill(oTypes, oAmounts, i);
	//				if (oAmounts[i] == 0) { // there are no more things to iterate though
	//					if(i< mSize && oCapacity < mCapacity) {	//but there is empty space
	//						int tAdd = Math.min(mCapacity - oCapacity, Math.min(mStackSize, rStack.stackSize));
	//						tDirty=true;
	//						rStack.stackSize -= tAdd;
	//						oAmounts[i] = tAdd;
	//						oTypes[i] = aThingToAdd.getItemDamage();
	//						oCapacity += tAdd;
	//						oStacks++;
	//						continue;
	//					}
	//					break;
	//				}
	//			}
				if( oTypes[i] == aThingToAdd.getItemDamage()) {
					int tAddNum = Math.max(0, Math.min(mStackSize - oAmounts[i], mCapacity - oCapacity) );
					if (tAddNum > 0) {
						tAddNum = Math.min(tAddNum, rStack.stackSize);
						tDirty= true;
						rStack.stackSize -= tAddNum;
						oAmounts[i] += tAddNum;
						oCapacity += tAddNum;
					}
				}
			}
			if(rStack.stackSize > 0 && oTypes.length < mSize && oCapacity < mCapacity) {	//have more stuff, unused space exits, but array would overflow
				int[] tTypes = new int[mSize], tAmounts = new int[mSize];
				for ( int i = 0; i < mSize; i++) {
					if(i < oAmounts.length) {
						tTypes[i] = oTypes[i];
						tAmounts[i] = oAmounts[i];
					} else {
						int tAdd = Math.min(mCapacity - oCapacity, Math.min(mStackSize, rStack.stackSize));
						tDirty=true;
						rStack.stackSize -= tAdd;
						tAmounts[i] = tAdd;
						tTypes[i] = aThingToAdd.getItemDamage();
						oCapacity += tAdd;
						oStacks++;
					}
					if(rStack.stackSize <= 0 || oCapacity >= mCapacity) break;
				}
				oTypes = tTypes;
				oAmounts = tAmounts;
			}
			if (tDirty) { //save changes
				oTag.setInteger(TAG_CAPACITY, oCapacity);
				oTag.setByte(TAG_STACKS, oStacks);
	//			if (aThingToAdd.getUnlocalizedName().equalsIgnoreCase("gt.meta.rockGt") ) {
					oTag.setIntArray(type_tag, oTypes);
					oTag.setIntArray(num_tag, oAmounts);
	//			} else if (aThingToAdd.getUnlocalizedName().equalsIgnoreCase("gt.meta.chunkGt")) {
	//				oTag.setIntArray(TAG_CHUNKS, oTypes);
	//				oTag.setIntArray(TAG_CHUNKS_N, oAmounts);
	//			}
				UT.NBT.set(aStack, oTag);
				return rStack;
			}
			return aThingToAdd;
		}

	@Override
	protected ItemStack remove(ItemStack aStack, final ItemStack aRequest) {
			if ( aRequest == null || aRequest == CS.NI || aRequest.stackSize == 0 || ! isAcceptableItem(aStack, aRequest)) return CS.NI;
			if(aRequest.stackSize < 0) aRequest.stackSize = aRequest.getItem().getItemStackLimit(aRequest);
			final NBTTagCompound oTag = UT.NBT.getOrCreate(aStack);
			int oCapacity = oTag.hasKey(TAG_CAPACITY) ? oTag.getInteger(TAG_CAPACITY) : 0;
			if ( oCapacity == 0) return CS.NI;
			byte oStacks = oTag.hasKey(TAG_STACKS) ? oTag.getByte(TAG_STACKS) : 0;
			if(oStacks == 0) return CS.NI;
			int[] oAmounts = CS.ZL_INTEGER, oTypes = CS.ZL_INTEGER;
			final String type_tag = NAME_TAGS.get(aRequest.getUnlocalizedName())[0];
			final String num_tag = NAME_TAGS.get(aRequest.getUnlocalizedName())[1];
	//		if (aRequest.getUnlocalizedName().equalsIgnoreCase("gt.meta.rockGt") ) {
				oTypes = oTag.hasKey(type_tag) ? oTag.getIntArray(type_tag) : CS.ZL_INTEGER;
				oAmounts = oTag.hasKey(num_tag) ? oTag.getIntArray(num_tag) : CS.ZL_INTEGER;
	//		} else if (aRequest.getUnlocalizedName().equalsIgnoreCase("gt.meta.chunkGt")) {
	//			oTypes = oTag.hasKey(TAG_CHUNKS) ? oTag.getIntArray(TAG_CHUNKS) : CS.ZL_INTEGER;
	//			oAmounts = oTag.hasKey(TAG_CHUNKS_N) ? oTag.getIntArray(TAG_CHUNKS_N) : CS.ZL_INTEGER;
	//		}
			int rNum = 0;
			for(int i = oTypes.length; i > 0; --i) {
				if(oAmounts[i] == 0 ) {
	//				oStacks--;
					backFill(oTypes, oAmounts, i);
					continue;
				}
				if(oTypes[i] == aRequest.getItemDamage()) {
					if(oAmounts[i] <= aRequest.stackSize - rNum) {
						rNum += oAmounts[i];
						oCapacity -= oAmounts[i];
						oAmounts[i] =0;
						oStacks--;
						backFill(oTypes, oAmounts, i);
					} else {
						final int tTake = aRequest.stackSize - rNum;
						oAmounts[i] -= tTake;
						oCapacity -= tTake;
						rNum += tTake;
						break;
					}
				}
			}
			oTag.setInteger(TAG_CAPACITY, oCapacity);
			oTag.setByte(TAG_STACKS, oStacks);
	//		if (aRequest.getUnlocalizedName().equalsIgnoreCase("gt.meta.rockGt") ) {
				oTag.setIntArray(type_tag, oTypes);
				oTag.setIntArray(num_tag, oAmounts);
	//		} else if (aRequest.getUnlocalizedName().equalsIgnoreCase("gt.meta.chunkGt")) {
	//			oTag.setIntArray(TAG_CHUNKS, oTypes);
	//			oTag.setIntArray(TAG_CHUNKS_N, oAmounts);
	//		}
			UT.NBT.set(aStack, oTag);
			return ST.make(aRequest.getItem(), rNum, aRequest.getItemDamage());
		}

	@Override
	public ItemStack[] getIteration(ItemStack aStack, boolean aVerify) {
	//		return new Iterator<ItemStack>() {
	//		@SuppressWarnings("unchecked")
	//		final Entry<String, String[]>[] mTags = (Entry<String, String[]>[]) NAME_TAGS.entrySet().toArray();
	//		Entry<String, String[]> tTag;
			if( ! aStack.hasTagCompound()) return CS.ZL_IS;
			NBTTagCompound oTag = aStack.getTagCompound();
			final java.util.ArrayList<ItemStack> rInv = new java.util.ArrayList<>();
			int tCap = 0;
	//		for (int iI = 0; iI < mTags.length; iI++) {
	//			tTag = mTags[iI];
			for( Entry<String, String[]> tTag : NAME_TAGS.entrySet()) {
				final int[] oTypes = oTag.hasKey(tTag.getValue()[0]) ? oTag.getIntArray(tTag.getValue()[0]) : CS.ZL_INTEGER;
				final int[] oAmounts = oTag.hasKey(tTag.getValue()[1]) ? oTag.getIntArray(tTag.getValue()[1]) : CS.ZL_INTEGER;
				for (int j=0; j< oTypes.length; j++) {
					//XXX warning
					rInv.add(ST.make(MD.GT, tTag.getKey(), oAmounts[j], oTypes[j]));
					if(aVerify) tCap += oAmounts[j];
				}
			}
			if(aVerify) {
				oTag.setByte(TAG_STACKS, (byte) rInv.size());
				oTag.setInteger(TAG_CAPACITY, tCap);
			}
			return (ItemStack[]) rInv.toArray();
		}

}