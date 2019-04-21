package com.github.canisartorus.prospectorjournal;

import com.github.canisartorus.prospectorjournal.lib.GeoTag;
import com.github.canisartorus.prospectorjournal.lib.RockMatter;
import com.github.canisartorus.prospectorjournal.lib.Utils;

import gregapi.item.multiitem.MultiItem;
import gregapi.tileentity.notick.TileEntityBase03MultiTileEntities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

class JournalBehaviour extends gregapi.item.multiitem.behaviors.IBehavior.AbstractBehaviorDefault {
	public static JournalBehaviour INSTANCE = new JournalBehaviour();
	@Override 
	public boolean onItemUse(MultiItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, byte aSide, float hitX, float hitY, float hitZ) {
		if(ConfigHandler.bookOnly) {
			if(aWorld.isRemote && lookForSample(aWorld, aX, aY, aZ, aPlayer))
				return true;
		} else if(aWorld.isRemote) {
			// XXX if sneak-using?
		    net.minecraft.client.Minecraft.getMinecraft().displayGuiScreen(new com.github.canisartorus.prospectorjournal.GuiMain());
		}
		return false;
	}
	
	@Override
	public ItemStack onItemRightClick(MultiItem aItem, ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
		if(aWorld.isRemote)
			net.minecraft.client.Minecraft.getMinecraft().displayGuiScreen(new com.github.canisartorus.prospectorjournal.GuiMain());
		return aStack;
	}
	
	/**
	 * Determines if an ore sample can be generated from this location, then calls TakeSample to do so.
	 * @param aWorld
	 * @param x
	 * @param y
	 * @param z
	 * @param aPlayer
	 * @return
	 */
	public static boolean lookForSample(World aWorld, int x, int y, int z, EntityPlayer aPlayer) {
		if(!aWorld.isRemote)
			return false;
		net.minecraft.tileentity.TileEntity i = aWorld.getTileEntity(x, y, z);

		if (i instanceof TileEntityBase03MultiTileEntities) {
			if(((TileEntityBase03MultiTileEntities)i).getTileEntityName().equalsIgnoreCase("gt.multitileentity.rock")) {
				ItemStack sample = ((gregtech.tileentity.misc.MultiTileEntityRock)i).mRock;
				if(sample == null) {
					//XXX is default rock.
//					sample = ((TileEntityBase03MultiTileEntities) i).getDrops(0, false).get(0);
					Utils.chatAt(aPlayer, "Nope, just rock");
				} else 
					TakeSample(aWorld, x, y, z, sample.getItemDamage(), Utils.ROCK, aPlayer);
				return true;
			} return false;
		}

		net.minecraft.block.Block b = aWorld.getBlock(x, y, z);

		if(b instanceof gregapi.block.prefixblock.PrefixBlock) {
			ItemStack sample = ((gregapi.block.prefixblock.PrefixBlock) b).getItemStackFromBlock(aWorld, x, y, z, gregapi.data.CS.SIDE_INVALID);
			String tName = b.getUnlocalizedName();
			if(tName.endsWith(".bedrock")) {
				TakeSample(aWorld, x, y, z, sample.getItemDamage(), Utils.BEDROCK, aPlayer);
			} else if (tName.startsWith("gt.meta.ore.normal.")) {
				TakeSample(aWorld, x, y, z, sample.getItemDamage(), Utils.ORE_VEIN, aPlayer);
			}
			Utils.chatAt(aPlayer, "Small ore, not worth recording");
			return true;
		} else if( b instanceof gregapi.block.misc.BlockBaseFlower) {
			int type = 0;
			if(b.getUnlocalizedName().equalsIgnoreCase("gt.block.flower.a")) {
				switch(aWorld.getBlockMetadata(x, y, z)) {
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
				switch(aWorld.getBlockMetadata(x, y, z)) {
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
			TakeSample(aWorld, x, y, z, type, Utils.FLOWER, aPlayer);
			return true;
		}
		return false;
	}
	
	/**
	 * Generates an ore sample knowledge for this location.
	 * @param aWorld
	 * @param x
	 * @param y
	 * @param z
	 * @param meta
	 * @param sourceType
	 * @param aPlayer
	 */
	@cpw.mods.fml.relauncher.SideOnly(cpw.mods.fml.relauncher.Side.CLIENT)
	static void TakeSample(World aWorld, int x, int y, int z, int meta, byte sourceType, EntityPlayer aPlayer) {
		final int dim = aWorld.provider.dimensionId;
	    if(sourceType == Utils.FLOWER || sourceType == Utils.BEDROCK) {
	    	boolean match = false;
	    	if(ProspectorJournal.bedrockFault.size() != 0) {
		    	for (GeoTag m : ProspectorJournal.bedrockFault) {
		    		// flower could be non-specific - match any
		    		if(dim == m.dim && meta == m.ore) {
		    			// include adjacent chunks as same unit.
		    			if(m.x >= x - 16 && m.x <= x + 16 && m.z >= z - 16 && m.z <= z + 16) {
		    				match = true;
		    				if(sourceType == Utils.BEDROCK) {
								m.x = x;
		    					m.z = z;
		    					m.sample = false;
		    					m.dead = false;
			    				Utils.writeJson(Utils.GT_BED_FILE);
		    				} else {
		    					Utils.chatAt(aPlayer, "Sure are plenty of these Flowers here.");
		    				}
		    				break;
		    			}
		    		} else if (m.dim == dim && m.ore == 0 && sourceType == Utils.BEDROCK) {
		    			// found a vein under non-specific flowers
		    			if(m.x >= x - 16 && m.x <= x + 16 && m.z >= z - 16 && m.z <= z + 16) {
		    				ProspectorJournal.bedrockFault.remove(m);
//	    					match = false;
	    					break;
		    			}
    		
		    		} else if(m.dim == dim && meta == 0 && ! m.sample) {
		    			if(m.x >= x - 16 && m.x <= x + 16 && m.z >= z - 16 && m.z <= z + 16) {
		    				match = true;
		    				Utils.chatAt(aPlayer, "I expected to find these Orechids here.");
		    				break;
		    			}
		    		}
		    	}
	    	}
	    	if (!match) {
		    	//make a new entry
	    		ProspectorJournal.bedrockFault.add(new GeoTag(meta, dim, x, z, sourceType == Utils.BEDROCK ? false : true));
	    		Utils.writeJson(Utils.GT_BED_FILE);
	    	}
	    }
	    if(meta == 0) return;
        if (ProspectorJournal.rockSurvey.size() != 0) {
            for (RockMatter n : ProspectorJournal.rockSurvey) {
                if (meta == n.ore && dim == n.dim && (x / 16) == n.cx() && (z /16) == n.cz()) {
                	switch(sourceType) {
                	case Utils.ORE_VEIN:
                		if( n.sample ) {
                			if( n.y > y) {
                				n.sample = false;
                				if(n.dead) {
                					n.dead = false;
                					n.multiple = 1;
                				}
                				n.y = (short) y;
                			} else 
                				continue;
                		} else {
            				n.multiple += 1;
            				n.dead = false;
                		}
                		break;
                	case Utils.ROCK:
                		if(n.sample) {
                			n.multiple += 1;
                    		if(n.y > y)
                    			n.y = (short) y;
                		} else {
                			if(n.y > y)
                				continue;
                			Utils.chatAt(aPlayer, "I've already found this vein.");
                			return;
                		}
                		break;
                	default:
                    	if(n.sample)
                    		return;
                    	if (n.y > (short) 10)
                    		continue;
						Utils.chatAt(aPlayer, "I've found that vein already!");
						return;
                	}
                	// Editing an existing vein
                	if(n.multiple == 4) {
            			n.x = n.cx() *16 + 8;
            			n.z = n.cz() *16 + 8;
                	} else if (n.multiple < 4) {
                		n.x = x;
                		n.z = z;
                	}
                    Utils.writeJson(Utils.GT_FILE);
                    return;
                }
            }
        }
        // Found traces of a new vein
        switch(sourceType) {
        case Utils.ORE_VEIN:
        	ProspectorJournal.rockSurvey.add(new RockMatter(meta, dim, x, y, z, false));
        	break;
        case Utils.ROCK:
        	ProspectorJournal.rockSurvey.add(new RockMatter(meta, dim, x, y, z, true));
        	break;
        default:
        	ProspectorJournal.rockSurvey.add(new RockMatter(meta, dim, x, 10, z, true));
		}
        Utils.writeJson(Utils.GT_FILE);
	}
}