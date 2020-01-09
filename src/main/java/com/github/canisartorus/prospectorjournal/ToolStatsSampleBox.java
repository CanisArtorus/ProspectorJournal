package com.github.canisartorus.prospectorjournal;

import gregapi.data.CS;
import gregapi.data.MT;
import gregapi.data.OP;
import gregapi.item.multiitem.MultiItemTool;
import gregapi.item.multiitem.tools.ToolStats;
import gregapi.oredict.OreDictMaterial;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;

public class ToolStatsSampleBox extends ToolStats {

	public ToolStatsSampleBox() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isMinableBlock(Block aBlock, byte aMetaData) { 
		if (aBlock instanceof gregapi.block.multitileentity.MultiTileEntityBlock
				&& aBlock.getMaterial() == Material.redstoneLight
				&& aBlock.stepSound == Block.soundTypeStone) {
			// Any of the UtilStone MTEs - ceramic and stone faucets, molds, cups, nozzles; plant pot; garbage bin; rock; mixing bowl; mortar.
//			((gregapi.block.multitileentity.MultiTileEntityBlock)aBlock).getUnlocalizedName();
			return true;
		}
//		aStack.getUnlocalizedName().equalsIgnoreCase("gt.multitileentity.rock");
		return false;
	}

	@Override public boolean canCollect()	{return true;}
	@Override public float getSpeedMultiplier()	{return 4.0f;} 
	@Override public String getDeathMessage()	{ return "[KILLER] put [VICTIM] in their collection.";}
	@Override public int getToolDamagePerBlockBreak()	{return 1;}
	@Override public float getBaseDamage()	{return 0.5f;}
	@Override public boolean isMiningTool()	{return true;}
	private long mMaterialAmount = 9 * CS.U2;
	  
	@Override public ItemStack getBrokenItem(ItemStack aStack){
		return mMaterialAmount < CS.U4 ? null : OP.scrapGt.mat(
				MultiSampleBoxItem.getPrimaryMaterial(aStack, MT.NULL), 1+CS.RNGSUS.nextInt(1+(int)(4*mMaterialAmount/CS.U)));
	}
	  
	@Override
	public Enchantment[] getEnchantments(ItemStack aStack, OreDictMaterial aMaterial) {
		return FORTUNE_ENCHANTMENT;
	}
		
	@Override
	public int[] getEnchantmentLevels(ItemStack aStack) {
		return new int[] {(2+MultiItemTool.getPrimaryMaterial(aStack, MT.NULL).mToolQuality) / 2};
	}

	@Override public int getRenderPasses()	{return 2;}
	@Override public net.minecraft.util.IIcon getIcon(ItemStack aStack, int aRenderPass)	{
		return (MultiItemTool.getPrimaryMaterial(aStack, MT.Steel).mTextureSetsItems.get(OP.chest.mIconIndexItem)).getIcon(aRenderPass);
//		  return gregapi.old.Textures.ItemIcons.VOID;
	}
	@Override public short[] getRGBa(ItemStack aStack, int aRenderPass)	{
		return MultiItemTool.getPrimaryMaterial(aStack, MT.Steel).mRGBaSolid;
	}
	  
}
