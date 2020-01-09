package com.github.canisartorus.prospectorjournal;

import static gregapi.data.CS.COMPAT_TC;
import static gregapi.data.CS.TOOL_SOUNDS;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregapi.code.ArrayListNoNulls;
import gregapi.code.ItemStackSet;
import gregapi.code.ObjectStack;
import gregapi.code.TagData;
import gregapi.data.CS;
import gregapi.data.IL;
import gregapi.data.LH;
import gregapi.data.MD;
import gregapi.data.MT;
import gregapi.data.TD;
import gregapi.data.TC.TC_AspectStack;
import gregapi.enchants.Enchantment_Radioactivity;
import gregapi.item.IItemEnergy;
import gregapi.item.IItemGTHandTool;
import gregapi.item.multiitem.MultiItem;
import gregapi.item.multiitem.MultiItemTool;
import gregapi.item.multiitem.behaviors.IBehavior;
import gregapi.item.multiitem.tools.IToolStats;
import gregapi.old.Textures;
import gregapi.oredict.OreDictMaterial;
import gregapi.util.OM;
import gregapi.util.ST;
import gregapi.util.UT;
import gregapi.util.WD;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;

/**
 * Specialty MultiItem that has ToolStats in addition to the Behaviours
 *   most parts copied from MultiItemTool and/or MultiItemRandom
 *   severely cut down for the specific use
 * @author Alexander James
 * @author Gregorius Techneticies
 *
 */
public abstract class MultiSampleBoxItem extends MultiItem implements Runnable, IItemGTHandTool {
	public final BitSet mEnabledItems = new BitSet(32767);
	public final BitSet mVisibleItems = new BitSet(32767);
	public final IIcon[][] mIconList = new IIcon[32767][1];
	public final HashMap<Short, IToolStats> mToolStats = new HashMap<>();
	
	public static net.minecraft.util.ChunkCoordinates LAST_USED_SPOT = null;

	public MultiSampleBoxItem(String aModID, String aUnlocalized) {
		super(aModID, aUnlocalized);
		CS.GAPI.mBeforeInit.add(this);
		setMaxStackSize(1);
		setHasSubtypes(true);
	}

	@Override
	public IItemEnergy getEnergyStats(ItemStack aStack) {return null;}

	@Override
	public Long[] getFluidContainerStats(ItemStack aStack) {return null;}
	
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
//				if(tRandomData instanceof AbstractInventoryBehavior) {
//					AbstractInventoryBehavior<MultiItem> tBehave = (AbstractInventoryBehavior<MultiItem>) tRandomData;
//					if(tBehave.doCollect())	net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(tBehave);
//					addItemBehavior(aID, tBehave);
//				}
				if (tRandomData instanceof IBehavior) {
					addItemBehavior(aID, (IBehavior<MultiItem>)tRandomData);
					continue;
				}
				if (tRandomData instanceof TC_AspectStack) {
					((TC_AspectStack)tRandomData).addToAspectList(tAspects);
					continue;
				}
				if (tRandomData instanceof IToolStats) {
					mToolStats.put((short)aID, (IToolStats)tRandomData);
//					((IToolStats)tRandomData).onStatsAddedToTool(new MIToolWrapper(this), aID);
					continue;
				}
				if (tRandomData instanceof ItemStackSet) {
					((ItemStackSet<?>)tRandomData).add(rStack.copy());
					continue;
				}
				OM.reg(tRandomData, rStack);
			}			
			if (COMPAT_TC != null) COMPAT_TC.registerThaumcraftAspectsToItem(rStack, tAspects, false);
			
			rStack = ST.make(this, 1, aID);
			ST.update(rStack);
			return rStack;
		}
		return null;
	}
	
	public final ItemStack getToolWithStats(int aToolID, OreDictMaterial aPrimaryMaterial) {
		ItemStack rStack = ST.make(this,  1, aToolID);
		IToolStats tToolStats = getToolStats(rStack);
		if (tToolStats != null) {
			NBTTagCompound tMainNBT = UT.NBT.getOrCreate(rStack), tToolNBT = UT.NBT.make();
			if(aPrimaryMaterial != null) {				
				if (aPrimaryMaterial.mID > 0) tToolNBT.setShort("a", aPrimaryMaterial.mID); 
				else tToolNBT.setString("b", aPrimaryMaterial.toString());
				UT.NBT.setNumber(tToolNBT, "j", 100L*(long)(aPrimaryMaterial.mToolDurability * tToolStats.getMaxDurabilityMultiplier()));
			}
			tMainNBT.setTag("GT.ToolStats", tToolNBT);
			UT.NBT.set(rStack, tMainNBT);
		}
		initEnchants(rStack);
		isItemStackUsable(rStack);
		return rStack;
	}
	
	public void initEnchants(ItemStack aStack) {
		if (aStack == null || aStack.stackSize <= 0) return;
		IToolStats tStats = getToolStatsInternal(aStack);
		if (tStats == null || getEnergyStats(aStack) != null) {
			NBTTagCompound aNBT = aStack.getTagCompound();
			if (aNBT != null) aNBT.removeTag("ench");
			return;
		}
		OreDictMaterial aMaterial = getPrimaryMaterial(aStack, MT.NULL);
		HashMap<Integer, Integer> tMap = new HashMap<>(), tResult = new HashMap<>();
		for (ObjectStack<Enchantment> tEnchantment : aMaterial.mEnchantmentTools) {
			tMap.put(tEnchantment.mObject.effectId, tEnchantment.amountInt());
			if (tEnchantment.mObject == Enchantment.fortune) tMap.put(Enchantment.looting.effectId, tEnchantment.amountInt());
			if (tEnchantment.mObject == Enchantment.knockback) tMap.put(Enchantment.punch.effectId, tEnchantment.amountInt());
			if (tEnchantment.mObject == Enchantment.fireAspect) tMap.put(Enchantment.flame.effectId, tEnchantment.amountInt());
		}
		Enchantment[] tEnchants = tStats.getEnchantments(aStack, aMaterial);
		int[] tLevels = tStats.getEnchantmentLevels(aStack);
		for (int i = 0; i < tEnchants.length; i++) if (tLevels[i] > 0) {
			Integer tLevel = tMap.get(tEnchants[i].effectId);
			tMap.put(tEnchants[i].effectId, tLevel == null ? tLevels[i] : tLevel == tLevels[i] ? tLevel+1 : Math.max(tLevel, tLevels[i]));
		}
		for (Entry<Integer, Integer> tEntry : tMap.entrySet()) {
			if (tEntry.getKey() == 33 || (tEntry.getKey() == 20 && tEntry.getValue() > 2) || tEntry.getKey() == Enchantment_Radioactivity.INSTANCE.effectId) tResult.put(tEntry.getKey(), tEntry.getValue()); else
			if ("enchantment.railcraft.crowbar.implosion".equalsIgnoreCase(Enchantment.enchantmentsList[tEntry.getKey()].getName())) {
				if (tStats.isWeapon()) tResult.put(tEntry.getKey(), tEntry.getValue());
			} else switch(Enchantment.enchantmentsList[tEntry.getKey()].type) {
			case weapon:
				if (tStats.isWeapon()) tResult.put(tEntry.getKey(), tEntry.getValue());
				break;
			case all:
				tResult.put(tEntry.getKey(), tEntry.getValue());
				break;
			case armor: case armor_feet: case armor_head: case armor_legs: case armor_torso:
				break;
			case bow:
				if (tStats.isRangedWeapon()) tResult.put(tEntry.getKey(), tEntry.getValue());
				break;
			case breakable:
				break;
			case fishing_rod:
				break;
			case digger:
				if (tStats.isMiningTool()) tResult.put(tEntry.getKey(), tEntry.getValue());
				break;
			}
		}
		EnchantmentHelper.setEnchantments(tResult, aStack);
	}
	
	@Override
	public boolean isItemStackUsable(ItemStack aStack) {
		if (getToolDamage(aStack) <= getToolMaxDamage(aStack)) return super.isItemStackUsable(aStack);
		return false;
	}

	/**
	 * Called by the Block Harvesting Event within the GT_Proxy
	 */
	public void onHarvestBlockEvent(ArrayList<ItemStack> aDrops, ItemStack aStack, EntityPlayer aPlayer, Block aBlock, int aX, int aY, int aZ, byte aMetaData, int aFortune, boolean aSilkTouch, BlockEvent.HarvestDropsEvent aEvent) {
		if (aBlock instanceof BlockTorch || IL.GC_Torch_Glowstone.equal(aBlock) || IL.AETHER_Torch_Ambrosium.equal(aBlock) || (aMetaData == 1 && IL.TC_Block_Air.equal(aBlock))) return;
		IToolStats tStats = getToolStats(aStack);
		if (isItemStackUsable(aStack) && getDigSpeed(aStack, aBlock, aMetaData) > 0) {
			int tDamage = tStats.convertBlockDrops(aDrops, aStack, aPlayer, aBlock, (getToolMaxDamage(aStack) - getToolDamage(aStack)) / tStats.getToolDamagePerDropConversion(), aX, aY, aZ, aMetaData, aFortune, aSilkTouch, aEvent);
			if (WD.dimBTL(aPlayer.worldObj) && !getPrimaryMaterial(aStack).contains(TD.Properties.BETWEENLANDS)) tDamage *= 4;
			if (!UT.Entities.hasInfiniteItems(aPlayer)) doDamage(aStack, tDamage * tStats.getToolDamagePerDropConversion(), aPlayer);
		}
	}
	
	public boolean canCollectDropsDirectly(ItemStack aStack, Block aBlock, byte aMetaData) {
		if (aBlock instanceof BlockTorch || IL.GC_Torch_Glowstone.equal(aBlock) || IL.AETHER_Torch_Ambrosium.equal(aBlock) || (aMetaData == 1 && IL.TC_Block_Air.equal(aBlock))) return true;
		IToolStats tStats = getToolStats(aStack);
		return (tStats.canCollect() || getPrimaryMaterial(aStack).contains(TD.Properties.MAGNETIC_ACTIVE)) && isItemStackUsable(aStack) && getDigSpeed(aStack, aBlock, aMetaData) > 0;
	}
	
	public float onBlockBreakSpeedEvent(float aDefault, ItemStack aStack, EntityPlayer aPlayer, Block aBlock, int aX, int aY, int aZ, byte aMetaData, PlayerEvent.BreakSpeed aEvent) {
		if (aBlock instanceof BlockTorch || IL.GC_Torch_Glowstone.equal(aBlock) || IL.AETHER_Torch_Ambrosium.equal(aBlock) || (aMetaData == 1 && IL.TC_Block_Air.equal(aBlock))) return Float.MAX_VALUE;
		IToolStats tStats = getToolStats(aStack);
		return tStats == null ? aDefault : tStats.getMiningSpeed(aBlock, aMetaData, aDefault, aPlayer, aPlayer.worldObj, aX, aY, aZ);
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack aStack, EntityPlayer aPlayer, Entity aEntity) {
		IToolStats tStats = getToolStats(aStack);
		if (tStats == null || !isItemStackUsable(aStack)) return true;
		if (TOOL_SOUNDS) UT.Sounds.play(tStats.getEntityHitSound(), 20, 1, aEntity);
		if (super.onLeftClickEntity(aStack, aPlayer, aEntity)) return true;
		if (aEntity.canAttackWithItem()) {
			int tFireAspect = EnchantmentHelper.getFireAspectModifier(aPlayer);
			boolean tIgnitesFire = !aEntity.isBurning() && tFireAspect > 0 && aEntity instanceof EntityLivingBase;
			if (tIgnitesFire) aEntity.setFire(1);
			if (aEntity.hitByEntity(aPlayer)) {
				if (tIgnitesFire) aEntity.extinguish();
			} else {
				float tMagicDamage = tStats.getMagicDamageAgainstEntity(aEntity instanceof EntityLivingBase?EnchantmentHelper.getEnchantmentModifierLiving(aPlayer, (EntityLivingBase)aEntity):0, aEntity, aStack, aPlayer), tDamage = tStats.getNormalDamageAgainstEntity((float)aPlayer.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue() + getToolCombatDamage(aStack), aEntity, aStack, aPlayer);
				if (tDamage + tMagicDamage > 0) {
					boolean tCriticalHit = aPlayer.fallDistance > 0 && !aPlayer.onGround && !aPlayer.isOnLadder() && !aPlayer.isInWater() && !aPlayer.isPotionActive(Potion.blindness) && aPlayer.ridingEntity == null && aEntity instanceof EntityLivingBase;
					if (tCriticalHit && tDamage > 0) tDamage *= 1.5;
					float tFullDamage = (MD.TFC.mLoaded ? (tDamage+tMagicDamage) * 10 : (tDamage+tMagicDamage));
					// Avoiding the Betweenlands Damage Cap in a fair way. Only Betweenlands Materials will avoid it. And maybe some super Lategame Items.
					if (MD.BTL.mLoaded && aEntity.getClass().getName().startsWith("thebetweenlands") && getPrimaryMaterial(aStack).contains(TD.Properties.BETWEENLANDS)) {
						float tDamageToDeal = tFullDamage;
						DamageSource tSource = tStats.getDamageSource(aPlayer, aEntity);
						
						while (tDamageToDeal > 0 && aEntity.attackEntityFrom(tSource, Math.min(tDamageToDeal, 12) / 0.3F)) {
							tDamageToDeal -= 12;
							if (tDamageToDeal > 0) aEntity.hurtResistantTime = 0;
						}
						if (tDamageToDeal < tFullDamage) {
							tStats.afterDealingDamage(tDamage, tMagicDamage, tFireAspect, tCriticalHit, aEntity, aStack, aPlayer);
							if (!UT.Entities.hasInfiniteItems(aPlayer)) doDamage(aStack, tStats.getToolDamagePerEntityAttack(), aPlayer);
						}
					} else {
						if (aEntity.attackEntityFrom(tStats.getDamageSource(aPlayer, aEntity), tFullDamage)) {
							tStats.afterDealingDamage(tDamage, tMagicDamage, tFireAspect, tCriticalHit, aEntity, aStack, aPlayer);
							if (!UT.Entities.hasInfiniteItems(aPlayer)) doDamage(aStack, tStats.getToolDamagePerEntityAttack(), aPlayer);
						}
					}
				}
			}
		}
		if (aStack.stackSize <= 0) aPlayer.destroyCurrentEquippedItem();
		return true;
	}
	
	@Override
	public final int getMaxItemUseDuration(ItemStack aStack) {
		return 7200;
	}
	
	@Override
	public void addAdditionalToolTips(List<String> aList, ItemStack aStack, boolean aF3_H) {
		long tMaxDamage = getToolMaxDamage(aStack), tDamage = getToolDamage(aStack);
		OreDictMaterial tMaterial = getPrimaryMaterial(aStack, MT.NULL);
		IToolStats tStats = getToolStats(aStack);
		if (tMaxDamage > 0 && tStats != null) {
			aList.add(LH.Chat.WHITE + "Durability: " + LH.Chat.GREEN + (tMaxDamage - tDamage) + " / " + tMaxDamage + LH.Chat.GRAY);
			aList.add(LH.Chat.WHITE + tMaterial.getLocal() + LH.Chat.YELLOW + " lvl " + getHarvestLevel(aStack, "") + LH.Chat.GRAY);
			float tCombat = getToolCombatDamage(aStack);
			aList.add(LH.Chat.WHITE + "Attack Damage: " + LH.Chat.BLUE + "+" + tCombat + LH.Chat.RED + " (= " + ((tCombat+1)/2) + " Hearts)" + LH.Chat.GRAY);
			aList.add(LH.Chat.WHITE + "Mining Speed: " + LH.Chat.PINK + Math.max(Float.MIN_NORMAL, tStats.getSpeedMultiplier() * getPrimaryMaterial(aStack, MT.NULL).mToolSpeed) + LH.Chat.GRAY);
			aList.add(LH.Chat.WHITE + "Crafting Uses: " + LH.Chat.GREEN + UT.Code.divup(getEnergyStats(aStack) == null ? tMaxDamage - tDamage : getEnergyStored(TD.Energy.EU, aStack), tStats.getToolDamagePerContainerCraft()) + LH.Chat.GRAY);
			if (MD.BTL.mLoaded && tMaterial.contains(TD.Properties.BETWEENLANDS)) aList.add(LH.Chat.GREEN + LH.get("Betweenlands"));//LH.get(LH.TOOLTIP_BETWEENLANDS_RESISTANCE));
			if (tStats.canCollect() || getPrimaryMaterial(aStack).contains(TD.Properties.MAGNETIC_ACTIVE)) aList.add(LH.Chat.DGRAY + LH.get(LH.TOOLTIP_AUTOCOLLECT));
		}
	}
	
	public static final OreDictMaterial getPrimaryMaterial(ItemStack aStack, OreDictMaterial aDefault) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag("GT.ToolStats");
			if (aNBT != null) {
				if (aNBT.hasKey("a")) return OreDictMaterial.MATERIAL_ARRAY[aNBT.getShort("a")];
				if (aNBT.hasKey("b")) return OreDictMaterial.get(aNBT.getString("b"));
				return OreDictMaterial.get(aNBT.getString("PrimaryMaterial"));
			}
		}
		return aDefault;
	}
	
	public static final OreDictMaterial getPrimaryMaterial(ItemStack aStack) {
		return getPrimaryMaterial(aStack, MT.NULL);
	}
	
	public float getToolCombatDamage(ItemStack aStack) {
		IToolStats tStats = getToolStats(aStack);
		if (tStats == null) return 0;
		return tStats.getBaseDamage() + getPrimaryMaterial(aStack, MT.NULL).mToolQuality;
	}
	
	public static final long getToolMaxDamage(ItemStack aStack) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag("GT.ToolStats");
			if (aNBT.hasKey("j")) return Math.max(1, aNBT.getLong("j"));
			return Math.max(1, aNBT.getLong("MaxDamage"));
		}
		return 1;
	}
	
	public static final long getToolDamage(ItemStack aStack) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag("GT.ToolStats");
			if (aNBT.hasKey("k")) return aNBT.getLong("k");
			return aNBT.getLong("Damage");
		}
		return 0;
	}
	
	public static final boolean setToolDamage(ItemStack aStack, long aDamage) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			UT.NBT.setNumber(aNBT.getCompoundTag("GT.ToolStats"), "k", aDamage);
			return true;
		}
		return false;
	}
	
	public final boolean doDamage(ItemStack aStack, long aAmount) {
		return doDamage(aStack, aAmount, null);
	}
	
	public final boolean doDamage(ItemStack aStack, long aAmount, EntityLivingBase aPlayer) {
		if (!isItemStackUsable(aStack)) return false;
		IItemEnergy tElectric = getEnergyStats(aStack);
		if (tElectric == null || CS.RNGSUS.nextInt(Math.max(5, getPrimaryMaterial(aStack, MT.NULL).mToolQuality * 25)) == 0) {
			long tNewDamage = getToolDamage(aStack) + aAmount;
			setToolDamage(aStack, tNewDamage);
			if (tNewDamage >= getToolMaxDamage(aStack)) {
				IToolStats tStats = getToolStats(aStack);
				if (tStats == null) {
					aStack.stackSize = 0;
				} else {
					if (TOOL_SOUNDS) {
						if (aPlayer == null) {
							if (LAST_USED_SPOT == null) {
								UT.Sounds.play(tStats.getBreakingSound(), 200, 1);
							} else {
								UT.Sounds.play(tStats.getBreakingSound(), 200, 1, LAST_USED_SPOT);
								LAST_USED_SPOT = null;
							}
						} else {
							UT.Sounds.play(tStats.getBreakingSound(), 200, 1, aPlayer);
							LAST_USED_SPOT= null;
						}
					}
					ItemStack tBroken = tStats.getBrokenItem(aStack);
					for (gregapi.item.multiitem.behaviors.IBehavior<MultiItem> tBehaviour : this.mItemBehaviors.get((short)aStack.getItemDamage())) {
						if (tBehaviour instanceof AbstractInventoryBehavior) {
							((AbstractInventoryBehavior<MultiItem>)tBehaviour).spillContents(aStack, aPlayer);
							break;
						}
					}
					if (ST.invalid(tBroken) || tBroken.stackSize <= 0) {
						aStack.stackSize = 0;
					} else if (aPlayer instanceof EntityPlayer) {
						if (tBroken.stackSize > 64) tBroken.stackSize = 64;
						if (!aPlayer.worldObj.isRemote) UT.Inventories.addStackToPlayerInventoryOrDrop((EntityPlayer)aPlayer, tBroken, false);
						aStack.stackSize = 0;
					} else {
						if (tBroken.stackSize > 64) tBroken.stackSize = 64;
						ST.set(aStack, tBroken);
					}
				}
			}
			return tElectric == null || useEnergy(TD.Energy.EU, aStack, aAmount, aPlayer, null, null, 0, 0, 0, true);
		}
		return useEnergy(TD.Energy.EU, aStack, aAmount, aPlayer, null, null, 0, 0, 0, true);
	}
	
	@Override
	public float getDigSpeed(ItemStack aStack, Block aBlock, int aMetaData) {
		if (aBlock == CS.NB || aBlock == Blocks.bedrock) return 0;
		if (aBlock instanceof BlockTorch || IL.GC_Torch_Glowstone.equal(aBlock) || IL.AETHER_Torch_Ambrosium.equal(aBlock) || (aMetaData == 1 && IL.TC_Block_Air.equal(aBlock))) return 10;
		if (!isItemStackUsable(aStack)) return 0;
		IToolStats tStats = getToolStats(aStack);
		if (tStats == null || Math.max(0, getHarvestLevel(aStack, "")) < aBlock.getHarvestLevel(aMetaData)) return 0;
		return tStats.getMiningSpeed(aBlock, (byte)aMetaData) * Math.max(Float.MIN_NORMAL, tStats.getSpeedMultiplier() * getPrimaryMaterial(aStack, MT.Steel).mToolSpeed);
	}
	
	@Override
	public final boolean canHarvestBlock(Block aBlock, ItemStack aStack) {
		return IL.TC_Block_Air.equal(aBlock) || getDigSpeed(aStack, aBlock, (byte)0) > 0;
	}
	
	@Override
	public final int getHarvestLevel(ItemStack aStack, String aToolClass) {
		IToolStats tStats = getToolStats(aStack);
		return tStats == null ? -1 : tStats.getBaseQuality() + getPrimaryMaterial(aStack, MT.NULL).mToolQuality; 
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack aStack, World aWorld, Block aBlock, int aX, int aY, int aZ, EntityLivingBase aPlayer) {
		if (aBlock instanceof BlockTorch || IL.GC_Torch_Glowstone.equal(aBlock) || IL.AETHER_Torch_Ambrosium.equal(aBlock) || IL.TC_Block_Air.equal(aBlock)) return true;
		if (!isItemStackUsable(aStack)) return false;
		IToolStats tStats = getToolStats(aStack);
		if (tStats == null) return false;
		if (TOOL_SOUNDS) UT.Sounds.play(tStats.getMiningSound(), 5, 1, aX, aY, aZ);
		boolean rReturn = (getDigSpeed(aStack, aBlock, aWorld.getBlockMetadata(aX, aY, aZ)) > 0);
		if (!UT.Entities.hasInfiniteItems(aPlayer)) {
			double tDamage = Math.max(1, tStats.getToolDamagePerBlockBreak() * aBlock.getBlockHardness(aWorld, aX, aY, aZ));
			if (WD.dimBTL(aWorld) && !getPrimaryMaterial(aStack).contains(TD.Properties.BETWEENLANDS)) tDamage *= 4;
			doDamage(aStack, (int)tDamage, aPlayer);
		}
		return rReturn;
	}

	public IToolStats getToolStats(ItemStack aStack) {
		isItemStackUsable(aStack);
		return getToolStatsInternal(aStack);
	}
	
	private IToolStats getToolStatsInternal(ItemStack aStack) {
		return aStack == null ? null : getToolStatsInternal(ST.meta_(aStack));
	}
	
	private IToolStats getToolStatsInternal(int aDamage) {
		return mToolStats.get((short)aDamage);
	}
	
	@Override
	public void onCreated(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
		IToolStats tStats = getToolStats(aStack);
		if (tStats != null && aPlayer != null) tStats.onToolCrafted(aStack, aPlayer);
		super.onCreated(aStack, aWorld, aPlayer);
	}

	@Override public int getItemEnchantability() {return 0;}
	@Override public boolean isBookEnchantable(ItemStack aStack, ItemStack aBook) {return false;}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister aIconRegister) {
		for (short aMeta = 0, tMaxMeta = (short)mEnabledItems.length(); aMeta < tMaxMeta; aMeta++) if (mEnabledItems.get(aMeta) && getToolStatsInternal(aMeta) == null) {
			for (byte k = 1; k < mIconList[aMeta].length; k++) {
				mIconList[aMeta][k] = aIconRegister.registerIcon(mModID + ":" + getUnlocalizedName() + "/" + aMeta + "/" + k);
			}
			mIconList[aMeta][0] = aIconRegister.registerIcon(mModID + ":" + getUnlocalizedName() + "/" + aMeta);
		}
	}
	
	@Override
	public final int getItemStackLimit(ItemStack aStack) {
		return 1;
	}
	
	@Override
	public boolean isFull3D() {
		return true;
	}

	@Override
	public IIcon getIconIndex(ItemStack aStack) {
		return getIcon(aStack, 0);
	}
	
	@Override
	public IIcon getIcon(ItemStack aStack, int aRenderPass) {
		return getIcon(aStack, aRenderPass, null, null, 0);
	}
	
	@Override
	public IIcon getIcon(ItemStack aStack, int aRenderPass, EntityPlayer aPlayer, ItemStack aUsedStack, int aUseRemaining) {
		IToolStats tStats = getToolStatsInternal(aStack);
		if (tStats == null) {
			short aMetaData = ST.meta_(aStack);
			if (!UT.Code.exists(aMetaData, mIconList)) return Textures.ItemIcons.RENDERING_ERROR.getIcon(0);
			return mIconList[aMetaData][0];
		}
		if(aRenderPass < tStats.getRenderPasses()) {
			IIcon rIcon = tStats.getIcon(aStack, aRenderPass);
			return rIcon == null ? Textures.ItemIcons.VOID.getIcon(0) : rIcon;
		}
		if(aPlayer == null) {
			if (aRenderPass == tStats.getRenderPasses()) {
				long tDamage = MultiItemTool.getToolDamage(aStack), tMaxDamage = MultiItemTool.getToolMaxDamage(aStack);
				if (tMaxDamage <= 0) return Textures.ItemIcons.VOID.getIcon(0);
				if (tDamage <= 0) return Textures.ItemIcons.DURABILITY_BAR[8].getIcon(0);
				if (tDamage >= tMaxDamage) return Textures.ItemIcons.DURABILITY_BAR[0].getIcon(0);
				return Textures.ItemIcons.DURABILITY_BAR[(int)Math.max(0, Math.min(7, ((tMaxDamage-tDamage)*8L) / tMaxDamage))].getIcon(0);
			}
			IItemEnergy tElectric = getEnergyStats(aStack);
			if (tElectric != null) {
				long tStored = tElectric.getEnergyStored(TD.Energy.EU, aStack), tCapacity = tElectric.getEnergyCapacity(TD.Energy.EU, aStack);
				if (tStored <= 0) return Textures.ItemIcons.ENERGY_BAR[0].getIcon(0);
				if (tStored >= tCapacity) return Textures.ItemIcons.ENERGY_BAR[8].getIcon(0);
				return Textures.ItemIcons.ENERGY_BAR[7-(int)Math.max(0, Math.min(6, ((tCapacity-tStored)*7L) / tCapacity))].getIcon(0);
			}

		}
		return Textures.ItemIcons.VOID.getIcon(0);
	}
	
	@Override
	public IIcon getIconFromDamage(int aMetaData) {
		return getIconIndex(ST.make(this,  1, aMetaData));
	}
	
	@Override
	public IIcon getIconFromDamageForRenderPass(int aMetaData, int aRenderPass) {
		return getIconFromDamage(aMetaData);
	}
	
	@Override
	public boolean requiresMultipleRenderPasses() { return true; }
	
	@Override
	public int getRenderPasses(int aMetaData) {
		IToolStats tStats = getToolStatsInternal(aMetaData);
		if (tStats != null) return tStats.getRenderPasses()+2;
		return 2;
	}

}
