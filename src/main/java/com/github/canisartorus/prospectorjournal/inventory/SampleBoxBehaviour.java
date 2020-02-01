package com.github.canisartorus.prospectorjournal.inventory;

import static gregapi.data.CS.RNGSUS;

import java.util.List;

import com.github.canisartorus.prospectorjournal.ConfigHandler;
import com.github.canisartorus.prospectorjournal.JournalBehaviour;
import com.github.canisartorus.prospectorjournal.gui.ContainerClientItemBag;

import gregapi.block.multitileentity.IMultiTileEntity.IMTE_GetDrops;
import gregapi.code.ArrayListNoNulls;
import gregapi.code.ObjectStack;
import gregapi.data.CS.SFX;
import gregapi.data.LH;
import gregapi.data.MT;
import gregapi.data.OP;
import gregapi.item.multiitem.MultiItem;
import gregapi.oredict.OreDictMaterial;
import gregapi.oredict.OreDictMaterialStack;
import gregapi.util.OM;
import gregapi.util.UT;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

/**
 * A FilteredBoxBehaviour specifically for collecting sample Rocks from the world.
 * Defaults to writing info in the Journal when it picks up a rock.
 * @author Alexander James
 *
 */
public class SampleBoxBehaviour  extends FilteredBoxBehavior {
	
	protected final byte mFortune, mFire;
	// be a journal and LookForSample when CollectBlock
	public boolean bJournal = true;
	
	public SampleBoxBehaviour(OreDictMaterial aMaterial, Number aSize, Number aStackSize, Number aCapacity) {
		super(aSize, Math.min(64, aStackSize.intValue()), aCapacity);

		int tFortune = 0, tFire = 0;
		boolean tSilky = false;
		final int iFortune = (2 + aMaterial.mToolQuality) / 2;
		if( ConfigHandler.fortunateBoxes || ConfigHandler.smeltBoxes) {
			for( ObjectStack<Enchantment> tEnch : aMaterial.mEnchantmentTools) {
				if( tEnch.mObject.effectId == Enchantment.fortune.effectId) {
					tFortune = tEnch.amountInt();
				} else if (tEnch.mObject.effectId == Enchantment.silkTouch.effectId) {
					tSilky = true;
				} else if (tEnch.mObject.effectId == Enchantment.fireAspect.effectId) {
					tFire = tEnch.amountInt();
				}
			}
		}
		mFortune = (byte) (ConfigHandler.fortunateBoxes ? (tFortune == iFortune ? iFortune +1 : Math.max(iFortune, tFortune) ): 0);
		mFire = (byte) (tSilky ? 0 : ConfigHandler.smeltBoxes ? tFire : 0);
		NAME_TAGS.put("gt.meta.rockGt", new String[] {"ca.rockList", "ca.rockCnt"});
		if(mFire != 0) NAME_TAGS.put("gt.meta.chunkGt", new String[]{"ca.pigList", "ca.pigCnt"});
	}
	public SampleBoxBehaviour(OreDictMaterial aMaterial, Number aSize, Number aStackSize) {	this(aMaterial, aSize, aStackSize, Integer.MAX_VALUE);	}
	public SampleBoxBehaviour(OreDictMaterial aMaterial, Number aSize) {						this(aMaterial, aSize, 64, 		Integer.MAX_VALUE);	}

	@Override public boolean willCollectFromWorld() {return true;}
	
	@Override protected boolean canCollect(ItemStack aStack, World aWorld, int aX, int aY, int aZ) {
		net.minecraft.tileentity.TileEntity tTile = aWorld.getTileEntity(aX, aY, aZ);
		if(tTile instanceof IMTE_GetDrops) {
			ArrayListNoNulls<ItemStack> tDrops = ((IMTE_GetDrops)tTile).getDrops(mFortune, true);
			for (ItemStack tThing : tDrops) {
				if ( ! isAcceptableItem(aStack, tThing)) return false;
			}
			return true;
		}
		return false;
	}
	
	@Override
	protected boolean collectBlock(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ) {
		net.minecraft.tileentity.TileEntity tTile = aWorld.getTileEntity(aX, aY, aZ);
		if(tTile instanceof IMTE_GetDrops) {
//			final int tFortune, tFire;
//			if(aStack.getEnchantmentTagList() != null) {
//				verifyEnchants(aStack);
//				tFortune = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, aStack);
//				tFire = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, aStack);
//			} else {
//				tFortune = mFortune;
//				tFire = mFire;
//			}
			ArrayListNoNulls<ItemStack> tDrops = ((IMTE_GetDrops)tTile).getDrops(mFortune, true);
			for (ItemStack tThing : tDrops) {
				if ( ! isAcceptableItem(aStack, tThing)) return false;
			}
			for (ItemStack tThing : tDrops) {
				tThing = autoSmelt(tThing);
				ItemStack tExtra = insert(aStack, tThing);
				UT.Inventories.addStackToPlayerInventoryOrDrop(aPlayer, tExtra, false, aWorld, aX+0.5, aY+0.5, aZ+0.5);
			}
			if(bJournal) JournalBehaviour.lookForSample(aWorld, aX, aY, aZ, aPlayer);
			UT.Sounds.send(aWorld, SFX.MC_COLLECT, 0.2F, ((RNGSUS.nextFloat() - RNGSUS.nextFloat()) * 0.7F + 1) * 2, aPlayer);
			return aWorld.setBlock(aX, aY, aZ, Blocks.air , 0, 3);
		}
		return false;
	}
	
	// Not checked for item validity, internal use only.
	protected ItemStack autoSmelt(ItemStack aThing) {
		if(mFire != 0 && OP.rockGt.contains(aThing)) {
			if(OM.association(aThing).hasValidPrefixMaterialData()) {
				OreDictMaterialStack tMat = OM.association(aThing).mMaterial;
				long tFuse = tMat.mMaterial.mMeltingPoint;
				if(tFuse < mFire * 300 +300 )
					return OM.dustOrSolid(tMat);	// Not everything has a chunk, so they give a small dust instead
//					return OP.chunkGt.mat(tMat, aThing.stackSize);
//			return OP.chunkGt.mat(OreDictMaterial.MATERIAL_ARRAY[aThing.getItemDamage()], aThing.stackSize);
			}
		}			
		return aThing;
	}

	@Override
	public List<String> getAdditionalToolTips(MultiItem aItem, List<String> aList, ItemStack aStack) {
		if( mFortune > 0)
			aList.add( LH.Chat.BLUE + LH.Chat.ITALIC + Enchantment.fortune.getTranslatedName(mFortune) + (mFire ==0 ? "" : "  " + StatCollector.translateToLocal("enchantment.autosmelt.name")));
		super.getAdditionalToolTips(aItem, aList, aStack);
		aList.add(LH.Chat.GRAY + StatCollector.translateToLocal("tooltip.collectrock.name"));
		return aList;
	}

//	@Override	public IInventory getInventory(ItemStack aStack, EntityPlayer aPlayer) {return new BoxInventory(aStack, aPlayer);}
	@Override
	public void openGUI(ItemStack aStack, EntityPlayer aPlayer) {
		net.minecraft.client.Minecraft.getMinecraft().displayGuiScreen(new ContainerClientItemBag(new ContainerCommonItemBag(
				getInventory(aStack, aPlayer), aPlayer, aPlayer.inventory.currentItem) {
					@Override 	protected void addSlotAt(int aIndex, int aX, int aY) {
						addSlotToContainer(new SlotGhost(mInventory, aIndex, aX, aY, OP.rockGt.mat(MT.Steel, 0)));
					}
		} ));
	}

}
