package com.canisartorus.prospectorjournal;

import com.canisartorus.prospectorjournal.lib.GeoTag;
import com.canisartorus.prospectorjournal.lib.RockMatter;
import com.canisartorus.prospectorjournal.lib.Utils;

/**	@author Alexander James
	@author Dyonovan
**/

import gregapi.tileentity.notick.TileEntityBase03MultiTileEntities;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

// @cpw.mods.fml.relauncher.SideOnly(cpw.mods.fml.relauncher.Side.CLIENT)
public class RightClickEvent {

	@cpw.mods.fml.common.eventhandler.SubscribeEvent
	public void playerRightClick(PlayerInteractEvent event) {

		if (event.isCanceled() || !event.entityPlayer.worldObj.isRemote ||
		        event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
			return;
		} else if(com.canisartorus.prospectorjournal.ConfigHandler.bookOnly) {
			if(event.entityPlayer.inventory.getCurrentItem() == null)
				return;
			else {
				ItemStack heldItem = event.entityPlayer.inventory.getCurrentItem();

				if (!heldItem.getUnlocalizedName().equalsIgnoreCase("item.ca.GeologySurveyBook")) {
				    return;
				}
			}
		}
		
		net.minecraft.world.World aWorld = event.entityPlayer.worldObj;
		net.minecraft.tileentity.TileEntity i = aWorld.getTileEntity(event.x, event.y, event.z);

		if (i instanceof TileEntityBase03MultiTileEntities) {
			if(((TileEntityBase03MultiTileEntities)i).getTileEntityName().equalsIgnoreCase("gt.multitileentity.rock")) {
				ItemStack sample = ((TileEntityBase03MultiTileEntities) i).getDrops(0, false).get(0);
				TakeSample(event, sample.getItemDamage(), Utils.ROCK );
			} else return;
		}
		
		/*
		// gregapi.block.IPrefixBlock[] gregapi.data.CS.BlocksGT.ores_normal;
		
		// GtOres are always gregapi.block.prefixBlock !! implements IPrefixBlock, IBlockRetrievable, IBlockMaterial
		//		if (b.mPrefix.contains(TD.Prefix.)
		//		if getUnlocalizedName.
				//	b.getItemStackFromBlock(aWorld, aX, y, z, SIDE_INVALID).getItemDamage();

		// From gregapi.block.ToolCompat.prospectStone(...)
			gregapi.data.CS.SIDE_INVALID = (byte) 6;
		
			gregapi.oredict.OreDictItemData tAssotiation = OM.anyassociation((tBlock instanceof gregapi.block.IBlockRetrievable ? ((IBlockRetrievable)tBlock).getItemStackFromBlock(aWorld, tX, tY, tZ, SIDE_INVALID) : ST.make(tBlock, 1, aWorld.getBlockMetadata(tX, tY, tZ))));
			if (tAssotiation != null && tAssotiation.mPrefix.containsAny(TD.Prefix.STANDARD_ORE, TD.Prefix.DENSE_ORE)) {
				
		// from gregapi.util.WD
			public static ItemStack stack(World aWorld, int aX, int aY, int aZ) {
				Block tBlock = aWorld.getBlock(aX, aY, aZ);
				return gregapi.util.ST.make(tBlock, 1, tBlock instanceof IBlockExtendedMetaData ? ((IBlockExtendedMetaData)tBlock).getExtendedMetaData(aWorld, aX, aY, aZ) : aWorld.getBlockMetadata(aX, aY, aZ));
			}
			@SuppressWarnings("unlikely-arg-type")
			public static boolean ore(Block aBlock, short aMetaData) {return (aBlock instanceof IBlockPlacable && (gregapi.data.CS.BlocksGT.stoneToBrokenOres.containsValue(aBlock) || BlocksGT.stoneToNormalOres.containsValue(aBlock) || BlocksGT.stoneToSmallOres.containsValue(aBlock)) || gregapi.util.OM.prefixcontains(ST.make(aBlock, 1, aMetaData), gregapi.data.TD.Prefix.ORE));}
		
*/
		
		net.minecraft.block.Block b = aWorld.getBlock(event.x, event.y, event.z);
		
		if(b instanceof gregapi.block.prefixblock.PrefixBlock) {
			ItemStack sample = ((gregapi.block.prefixblock.PrefixBlock) b).getItemStackFromBlock(aWorld, event.x, event.y, event.z, gregapi.data.CS.SIDE_INVALID);
			String tName = b.getUnlocalizedName();
			if(tName.endsWith(".bedrock")) {
				TakeSample(event, sample.getItemDamage(), Utils.BEDROCK);
			} else if (tName.startsWith("gt.meta.ore.normal.")) {
				TakeSample(event, sample.getItemDamage(), Utils.ORE_VEIN);
			}
			// XXX Chat "not worth recording"
			return;
		} else if( b instanceof gregapi.block.misc.BlockBaseFlower) {
			int type = 0;
			if(b.getUnlocalizedName().equalsIgnoreCase("gt.block.flower.a")) {
				switch(event.entityPlayer.worldObj.getBlockMetadata(event.x, event.y, event.z)) {
				case 0:	// Gold
					type = 790;
					break;
				case 5:	// Uraninite
					type = 9134;
					break;
				case 6:	// Cooperite
					type = 9116;
					break;
				case 1:	// Galena
					type = 9117;
					break;
				case 4:	// Pentlandite
					type = 9145;
					break;
				case 2:	// Chalcopyrite
					type = 9111;
					break;
				default:
					// unspecified Orechid vein.
				}
			} else if(b.getUnlocalizedName().equalsIgnoreCase("gt.block.flower.b")) {
				switch(event.entityPlayer.worldObj.getBlockMetadata(event.x, event.y, event.z)) {
				case 5:	// Pitchblende
					type = 9155;
					break;
				case 3:	// Copper
					type = 290;
					break;
				case 2:	// Gold
					type = 790;
					break;
				case 0:	// Arsenopyrite
					type = 9216;
					break;
				case 4:	// Redstone
					type = 8333;
					break;
				case 1:	// Stibnite
					type = 9131;
					break;
				default:
					// something new
				}
			}
			TakeSample(event, type, Utils.FLOWER);
		} else {
			// some random block
			if(event.entityPlayer.inventory.getCurrentItem() == null)
				return;
			else {
				ItemStack heldItem = event.entityPlayer.inventory.getCurrentItem();
				if (heldItem.getUnlocalizedName().equalsIgnoreCase("item.ca.GeologySurveyBook")) {
				    net.minecraft.client.Minecraft.getMinecraft().displayGuiScreen(new com.canisartorus.prospectorjournal.GuiMain());
				}
			}
		}
	}
	
	void TakeSample(PlayerInteractEvent event, int meta, byte sourceType) {
	    int dim = event.entityPlayer.worldObj.provider.dimensionId;
	
	    if(sourceType == Utils.FLOWER || sourceType == Utils.BEDROCK) {
	    	boolean match = false;
	    	if(ProspectorJournal.bedrockFault.size() != 0) {
		    	for (GeoTag m : ProspectorJournal.bedrockFault) {
		    		// flower could be non-specific - match any
		    		if(dim == m.dim && meta == m.ore) {
		    			// include adjacent chunks as same unit.
		    			if(m.x >= event.x - 16 && m.x <= event.x + 16 && m.z >= event.z - 16 && m.z <= event.z + 16) {
		    				match = true;
		    				if(sourceType == Utils.BEDROCK) {
								m.x = event.x;
		    					m.z = event.z;
		    					m.sample = false;
		    					m.dead = false;
			    				Utils.writeJson(Utils.GT_BED_FILE);
		    				} else {
		    					//XXX Chat "Sure are plenty of these Flowers here."
		    				}
		    				break;
		    			}
		    		} else if (m.dim == dim && m.ore == 0 && sourceType == Utils.BEDROCK) {
		    			// found a vein under non-specific flowers
		    			if(m.x >= event.x - 16 && m.x <= event.x + 16 && m.z >= event.z - 16 && m.z <= event.z + 16) {
		    				ProspectorJournal.bedrockFault.remove(m);
//	    					match = false;
	    					break;
		    			}
    		
		    		} else if(m.dim == dim && meta == 0 && ! m.sample) {
		    			if(m.x >= event.x - 16 && m.x <= event.x + 16 && m.z >= event.z - 16 && m.z <= event.z + 16) {
		    				match = true;
		    				//XXX Chat "I expected to find these Orechids here."
		    				break;
		    			}
		    		}
		    	}
	    	}
	    	if (!match) {
		    	//make a new entry
	    		ProspectorJournal.bedrockFault.add(new GeoTag(meta, dim, event.x, event.z, sourceType == Utils.BEDROCK ? false : true));
	    		Utils.writeJson(Utils.GT_BED_FILE);
	    	}
	    }
	    if(meta == 0) return;
        if (ProspectorJournal.rockSurvey.size() != 0) {
            for (RockMatter n : ProspectorJournal.rockSurvey) {
                if (meta == n.ore && dim == n.dim && (event.x / 16) == n.cx() && (event.z /16) == n.cz()) {
                	switch(sourceType) {
                	case Utils.ORE_VEIN:
                		if( n.sample ) {
                			if( (int)n.y > event.y) {
                				n.sample = false;
                				if(n.dead) {
                					n.dead = false;
                					n.multiple = 1;
                				}
                				n.y = (short)event.y;
                			} else 
                				continue;
                		} else {
//                    			n.y = (short)Math.round((n.y*n.multiple + event.y)/(n.multiple+1.0f));
            				n.multiple += 1;
            				n.dead = false;
                		}
                		break;
                	case Utils.ROCK:
                		if(n.sample) {
                			n.multiple += 1;
                    		if((int)n.y > event.y)
                    			n.y = (short) event.y;
                		} else {
                			if((int)n.y > event.y)
                				continue;
                			//XXX Chat "I've already found this vein."
                			return;
                		}
                		break;
                	default:
                    	if(n.sample)
                    		return;
                    	if (n.y > (short) 10) {
                    		continue;
                    	} else {
                    		//XXX Chat "I've found that vein already!"
                    		return;
                    	}
                	}
                	// Editing an existing vein
                	if(n.multiple == 4) {
            			n.x = n.cx() *16 + 8;
            			n.z = n.cz() *16 + 8;
                	} else if (n.multiple < 4) {
                		n.x = event.x;
                		n.z = event.z;
                	}
                    Utils.writeJson(Utils.GT_FILE);
                    return;
                }
            }
        }
        // Found traces of a new vein
        switch(sourceType) {
        case Utils.ORE_VEIN:
        	ProspectorJournal.rockSurvey.add(new RockMatter(meta, dim, event.x, event.y, event.z, false));
        	break;
        case Utils.ROCK:
        	ProspectorJournal.rockSurvey.add(new RockMatter(meta, dim, event.x, event.y, event.z, true));
        	break;
        default:
        	ProspectorJournal.rockSurvey.add(new RockMatter(meta, dim, event.x, 10, event.z, true));
		}
        Utils.writeJson(Utils.GT_FILE);
	}
}