package com.github.canisartorus.prospectorjournal;

import static gregapi.data.CS.COMPAT_TC;

import java.util.BitSet;
import java.util.List;

import gregapi.code.ArrayListNoNulls;
import gregapi.code.TagData;
import gregapi.data.CS;
import gregapi.data.LH;
import gregapi.data.TD;
import gregapi.data.TC.TC_AspectStack;
import gregapi.item.IItemEnergy;
import gregapi.item.multiitem.MultiItem;
import gregapi.item.multiitem.behaviors.IBehavior;
import gregapi.util.OM;
import gregapi.util.ST;
import gregapi.util.UT;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

/**
 * Specialty MultiItem that has ToolStats in addition to the Behaviours
 *   most parts copied from MultiItemTool and/or MultiItemRandom
 *   severely cut down for the specific use
 * @author Alexander James
 * @author Gregorius Techneticies
 *
 */
public abstract class MultiSampleBoxItem extends MultiItem implements Runnable {
	public final BitSet mEnabledItems = new BitSet(32767);
	public final BitSet mVisibleItems = new BitSet(32767);
	public final IIcon[][] mIconList = new IIcon[32767][1];

	public MultiSampleBoxItem(String aModID, String aUnlocalized) {
		super(aModID, aUnlocalized);
		CS.GAPI.mBeforeInit.add(this);
	}

	@Override
	public IItemEnergy getEnergyStats(ItemStack aStack) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long[] getFluidContainerStats(ItemStack aStack) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * Add your Items here, and not within the Constructor.
	 * This gets called after all the PrefixItems and PrefixBlocks have been registered to the OreDict, what is during the @Init-Phase of the regular API.
	 */
	public abstract void addItems();
	
	private boolean mAllowedToAddItems = false;
	
	@Override
	public final void run() {
		mAllowedToAddItems = true;
		addItems();
	}
	
	// freshly customized
	@SuppressWarnings("unchecked")
	public final ItemStack addItem(int aID, String aEnglish, Object... aRandomData) {
		if (mAllowedToAddItems && aID >= 0 && aID < 32767 && aID != CS.W) {
			ItemStack rStack = ST.make(this, 1, aID);
			if (UT.Code.stringValid(aEnglish)) {
				mEnabledItems.set(aID);
				mVisibleItems.set(aID);
				LH.add(getUnlocalizedName(rStack) + ".name", aEnglish);
			}
			List<TC_AspectStack> tAspects = new ArrayListNoNulls<>();
			// Important Stuff to do first
			for (Object tRandomData : aRandomData) if (tRandomData instanceof TagData) {
				if (tRandomData == TD.Creative.HIDDEN           ) {mVisibleItems.set(aID, false); continue;}
				if (tRandomData == TD.Properties.AUTO_BLACKLIST ) {OM.blacklist_(rStack); continue;}
			}
			// now check for the rest
			for (Object tRandomData : aRandomData) if (tRandomData != null) {
				if (tRandomData instanceof IBehavior) {
					addItemBehavior(aID, (IBehavior<MultiItem>)tRandomData);
					continue;
				}
				if (tRandomData instanceof TC_AspectStack) {
					((TC_AspectStack)tRandomData).addToAspectList(tAspects);
					continue;
				}
				//XXX
				OM.reg(tRandomData, rStack);
			}			
			if (COMPAT_TC != null) COMPAT_TC.registerThaumcraftAspectsToItem(rStack, tAspects, false);
			
			rStack = ST.make(this, 1, aID);
			ST.update(rStack);
			return rStack;
		}
		return null;
	}

}
