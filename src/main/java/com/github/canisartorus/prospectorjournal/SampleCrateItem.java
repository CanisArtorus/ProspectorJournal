package com.github.canisartorus.prospectorjournal;

import java.util.List;

import gregapi.block.multitileentity.IMultiTileEntity.IMTE_GetBlockHardness;
import gregapi.block.multitileentity.IMultiTileEntity.IMTE_GetExplosionResistance;
import gregapi.block.multitileentity.IMultiTileEntity.IMTE_GetMaxStackSize;
import gregapi.block.multitileentity.IMultiTileEntity.IMTE_OnItemRightClick;
import gregapi.block.multitileentity.IMultiTileEntity.IMTE_OnToolClick;
import gregapi.block.multitileentity.IMultiTileEntity.IMTE_OnlyPlaceableWhenSneaking;
import gregapi.damage.DamageSources;
import gregapi.block.multitileentity.MultiTileEntityItemInternal;
import gregapi.data.CS;
import gregapi.data.CS.GarbageGT;
import gregapi.data.CS.SFX;
import gregapi.item.multiitem.MultiItemTool;
import gregapi.data.MT;
import gregapi.data.OP;
import gregapi.oredict.OreDictMaterial;
import gregapi.render.ITexture;
import gregapi.tileentity.base.TileEntityBase05Inventories;
import gregapi.util.OM;
import gregapi.util.ST;
import gregapi.util.UT;
import gregapi.util.UT.Enchantments;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.event.world.BlockEvent;

/**
 * @author Alexander James
 * 
 */
public class SampleCrateItem extends TileEntityBase05Inventories
		implements IMTE_GetBlockHardness, IMTE_GetExplosionResistance,	// IToolStats,
		IMTE_GetMaxStackSize, IMTE_OnlyPlaceableWhenSneaking, IMTE_OnItemRightClick {

	protected float mHardness = 0.0f, mResistance = 1.0f;
	protected OreDictMaterial mMaterial = MT.NULL;
	protected int mStackLimit = 64;
	protected short mSlotCount = 8;
	
	public SampleCrateItem() {}
	
	@Override
	public void readfromNBT2(NBTTagCompound aNBT) {
		super.readFromNBT2(aNBT);
		if (aNBT.hasKey(CS.NBT_HARDNESS)) mHardness = aNBT.getFloat(CS.NBT_HARDNESS);
		if (aNBT.hasKey(CS.NBT_RESISTANCE)) mResistance = aNBT.getFloat(CS.NBT_RESISTANCE);
		if (aNBT.hasKey(CS.NBT_MATERIAL)) mMaterial = OreDictMaterial.get(aNBT.getString(CS.NBT_MATERIAL));
	}
	
	@Override
	public void writeToNBT2(NBTTagCompound aNBT) {
		super.wrtieToNBT2(aNBT);
	}
	
	@Override
	public boolean onBlockActivated2(EntityPlayer aPlayer, byte aSide, float aHitX, float aHitY, float aHitZ) {
		if (isServerSide()  && isUseableByPlayerGUI(aPlayer)) {
			openGUI(aPlayer);
		}
		return T;
	}
	
	@Override public boolean canDrop(int aInventorySlot) {return true;}
	@Override public String getTileEntityName() {return "ca.samplecrate";}
	@Override public float getExplosionResistance2() {return mResistance;}
	@Override public float getBlockHardness() {return mHardness;}
//	@Override public ITexture getTexture(Block aBlock, int aRenderPass, byte aSide, boolean[] aShouldSideBeRendered) {return null;}
//	@Override public int getRenderPasses(Block aBlock, boolean[] aShouldSideBeRendered) {return 0;}
//	@Override public boolean renderBlock(Block aBlock, RenderBlocks aRenderer, IBlockAccess aWorld, int aX, int aY, int aZ) {return true;}

	@Override public int getInventoryStackLimit() {return mStackLimit;}
	
	@Override
	public boolean isItemValidForSlot(int aSlot, ItemStack aStack) {
		return aStack.getUnlocalizedName().equalsIgnoreCase("gt.multitileentity.rock");
	}
	@Override public boolean isItemValidForSlotGUI(int aSlot, ItemStack aStack) {return aStack.getUnlocalizedName().equalsIgnoreCase("gt.multitileentity.rock");}
	
	@Override
	public void onExploded(net.minecraft.world.Explosion aExplosion) {
		for (int i =0; i< this.getSizeInventory(); i++) {
			if(gregapi.data.CS.RNGSUS.nextInt(4) !=0) GarbageGT.trash(this.getInventory(), i);
			ItemStack tStack = this.getStackInSlot(i);
			if(tStack.stackSize > 64) GarbageGT.trash(this.decrStackSize(i, tStack.stackSize - 64));
		}
		setToAir();
	}
	
	@Override
	public boolean breakBlock() {
		//XXX write to item
		return false;
	}

	@Override public boolean onlyPlaceableWhenSneaking() {return true;}

	@Override
	public byte getMaxStackSize(ItemStack aStack, byte aDefault) {
		return mStackLimit;
	}

	@Override
	public float getExplosionResistance(Entity aExploder, double aExplosionX, double aExplosionY, double aExplosionZ) {
		return mResistance;
	}

	@Override
	public float getExplosionResistance() {
		return mResistance;
	}
/*
 * @Override public boolean canCollect()	{return true;}
 * @Override public float getSpeedMultiplier()	{return 4.0f;} 
 * @Override public String getDeathMessage()	{ return "[KILLER] put [VICTIM] in their collection."}
 * @Override public int getToolDamagePerBlockBreak()	{return 1;}
 * @Override public float getBaseDamage()	{return 0.5f;}
 * // @Override public boolean isMiningTool()	{return true;}
 * private long mMaterialAmount = 
 * @Override public ItemStack getBrokenItem(ItemStack aStack){
 * 	return mMaterialAmount < CS.U4 ? null : OP.scrapGt.mat(
 * MultiItemTool.getPrimaryMaterial(aStack, MT.NULL), 1+CS.RNGSUS.nextInt(1+(int)(4*mMaterialAmount/CS.U)));
 * }
 * @Override public int getRnderPasses()	{return 1;}
 * @Override public IIcon getIcon(ItemStack aStack, int aRenderPass)	{return Textures.ItemIcons.VOID;}
 * @Override public short[] getRGBa(ItemStack aStack, int aRenderPass)	{return CS.UNCOLOURED;}
 * 
 * public boolean isMinableBlock(Block aBlock, byte aMeta){
 *  if (
 *  	) return true;
 * 	return false;
 * }
 * 
// @author Gregorius Technicities
// From gregapi.item.multiitem.tools.ToolStats
	@Override public int getToolDamagePerDropConversion()                                   {return 100;}
	@Override public int getToolDamagePerContainerCraft()                                   {return 100;}
	@Override public int getToolDamagePerEntityAttack()                                     {return 100;}
	@Override public int getBaseQuality()                                                   {return   0;}
	@Override public int getHurtResistanceTime(int aOriginalHurtResistance, Entity aEntity) {return aOriginalHurtResistance;}
	@Override public float getMaxDurabilityMultiplier()                                     {return 1.0F;}
	@Override public float getExhaustionPerAttack(Entity aEntity)                           {return 0.3F;}
	@Override public String getMiningSound()                                                {return null;}
	@Override public String getCraftingSound()                                              {return null;}
	@Override public String getEntityHitSound()                                             {return null;}
	@Override public String getBreakingSound()                                              {return SFX.MC_BREAK;}
	@Override public boolean canBlock()                                                     {return false;}
	@Override public boolean isWrench()                                                     {return false;}
	@Override public boolean isCrowbar()                                                    {return false;}
	@Override public boolean isGrafter()                                                    {return false;}
	@Override public boolean isWeapon()                                                     {return false;}
	@Override public boolean isRangedWeapon()                                               {return false;}
	@Override public boolean isMiningTool()                                                 {return true;}

	@Override
	public float getMiningSpeed(Block aBlock, byte aMetaData) {
		return isMinableBlock(aBlock, aMetaData) ? 1 : 0;
	}
	
	@Override
	public float getMiningSpeed(Block aBlock, byte aMetaData, float aDefault, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ) {
		return aDefault;
	}
	
	@Override
	public DamageSource getDamageSource(EntityLivingBase aPlayer, Entity aEntity) {
		return DamageSources.getCombatDamage(aPlayer instanceof EntityPlayer ? "player" : "mob", aPlayer, aEntity instanceof EntityLivingBase ? getDeathMessage(aPlayer, (EntityLivingBase)aEntity) : null);
	}
	
	public IChatComponent getDeathMessage(EntityLivingBase aPlayer, EntityLivingBase aEntity) {
		String aNamePlayer = aPlayer.getCommandSenderName(), aNameEntity = aEntity.getCommandSenderName();
		if (UT.Code.stringInvalid(aNamePlayer) || UT.Code.stringInvalid(aEntity)) return new ChatComponentText("Death Message lacks names of involved Players");
		aNamePlayer = aNamePlayer.trim(); aNameEntity = aNameEntity.trim();
		return getDeathMessage(aPlayer, aEntity, aNamePlayer, aNameEntity);
	}
	
	public IChatComponent getDeathMessage(EntityLivingBase aPlayer, EntityLivingBase aEntity, String aNamePlayer, String aNameEntity) {
		String rMessage = getDeathMessage();
		if (UT.Code.stringInvalid(rMessage)) return new EntityDamageSource(aPlayer instanceof EntityPlayer ? "player" : "mob", aPlayer).func_151519_b(aEntity);
		return new ChatComponentText(rMessage.replace("[KILLER]", EnumChatFormatting.GREEN+aNamePlayer+EnumChatFormatting.WHITE).replace("[VICTIM]", EnumChatFormatting.RED+aNameEntity+EnumChatFormatting.WHITE));
	}

	@Override
	public int convertBlockDrops(List<ItemStack> aDrops, ItemStack aStack, EntityPlayer aPlayer, Block aBlock, long aAvailableDurability, int aX, int aY, int aZ, byte aMetaData, int aFortune, boolean aSilkTouch, BlockEvent.HarvestDropsEvent aEvent) {
		return 0;
	}

	@Override
	public Enchantment[] getEnchantments(ItemStack aStack, OreDictMaterial aMaterial) {
		return CS.ZL_ENCHANTMENT;
	}
	
	@Override
	public int[] getEnchantmentLevels(ItemStack aStack) {
		return CS.ZL_INTEGER;
	}
	
	@Override
	public void onToolCrafted(ItemStack aStack, EntityPlayer aPlayer) {
		aPlayer.triggerAchievement(AchievementList.openInventory);
		aPlayer.triggerAchievement(AchievementList.mineWood);
		aPlayer.triggerAchievement(AchievementList.buildWorkBench);
	}
	
	@Override
	public void onStatsAddedToTool(MultiItemTool aItem, int aID) {
		//
	}
	
	@Override
	public float getNormalDamageAgainstEntity(float aOriginalDamage, Entity aEntity, ItemStack aStack, EntityPlayer aPlayer) {
		return aOriginalDamage;
	}
	
	@Override
	public float getMagicDamageAgainstEntity(float aOriginalDamage, Entity aEntity, ItemStack aStack, EntityPlayer aPlayer) {
		return aOriginalDamage;
	}

	@Override
	public void afterDealingDamage(float aNormalDamage, float aMagicDamage, int aFireAspect, boolean aCriticalHit, Entity aEntity, ItemStack aStack, EntityPlayer aPlayer) {
		if (aEntity instanceof EntityLivingBase && aFireAspect > 0) aEntity.setFire(aFireAspect * 4);
		int tKnockback = (aPlayer.isSprinting()?1:0) + (aEntity instanceof EntityLivingBase?EnchantmentHelper.getKnockbackModifier(aPlayer, (EntityLivingBase)aEntity):0);
		if (tKnockback > 0) {
			aEntity.addVelocity(-MathHelper.sin((float)(aPlayer.rotationYaw * Math.PI / 180)) * tKnockback * 0.5, 0.1, MathHelper.cos((float)(aPlayer.rotationYaw * Math.PI / 180)) * tKnockback * 0.5);
			aPlayer.motionX *= 0.6;
			aPlayer.motionZ *= 0.6;
			aPlayer.setSprinting(F);
		}
		if (aCriticalHit) aPlayer.onCriticalHit(aEntity);
		if (aMagicDamage > 0) aPlayer.onEnchantmentCritical(aEntity);
		if (aNormalDamage+aMagicDamage >= 18) aPlayer.triggerAchievement(AchievementList.overkill);
		aPlayer.setLastAttacker(aEntity);
		if (aEntity instanceof EntityLivingBase) Enchantments.applyBullshitA((EntityLivingBase)aEntity, aPlayer, aStack);
		Enchantments.applyBullshitB(aPlayer, aEntity, aStack);
		if (aEntity instanceof EntityLivingBase) aPlayer.addStat(StatList.damageDealtStat, Math.round((aNormalDamage+aMagicDamage) * 10));
		aEntity.hurtResistantTime = Math.max(1, getHurtResistanceTime(aEntity.hurtResistantTime, aEntity));
		aPlayer.addExhaustion(getExhaustionPerAttack(aEntity));
	}


 */
}
