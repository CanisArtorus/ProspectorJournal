package com.github.canisartorus.prospectorjournal;

import com.github.canisartorus.prospectorjournal.compat.IEHandler;
import com.github.canisartorus.prospectorjournal.lib.GeoTag;
import com.github.canisartorus.prospectorjournal.lib.RockMatter;
import com.github.canisartorus.prospectorjournal.lib.Utils;
import com.github.canisartorus.prospectorjournal.lib.Utils.ChatString;
import com.github.canisartorus.prospectorjournal.network.PacketOreSurvey;

import gregapi.block.metatype.BlockStones;
import gregapi.item.multiitem.MultiItem;
import gregapi.tileentity.notick.TileEntityBase03MultiTileEntities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class JournalBehaviour extends gregapi.item.multiitem.behaviors.IBehavior.AbstractBehaviorDefault {
	public static JournalBehaviour INSTANCE = new JournalBehaviour();
	@Override 
	public boolean onItemUse(MultiItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, byte aSide, float hitX, float hitY, float hitZ) {
		if(ConfigHandler.bookOnly) {
			if(lookForSample(aWorld, aX, aY, aZ, aPlayer))
				return true;
		} 
		if(aPlayer.isSneaking() ) {
		    ProspectorJournal.PROXY.openGuiMain();
		}
		return false;
	}
	
	@Override
	public ItemStack onItemRightClick(MultiItem aItem, ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
		ProspectorJournal.PROXY.openGuiMain();
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
		if(!aWorld.isRemote) {
			// stuff that needs server-side data
		final net.minecraft.tileentity.TileEntity i = aWorld.getTileEntity(x, y, z);

		if (i instanceof TileEntityBase03MultiTileEntities) {
			if(((TileEntityBase03MultiTileEntities)i).getTileEntityName().equalsIgnoreCase("gt.multitileentity.rock")) {
				// serverside data only!!!
				final ItemStack sample = ((gregtech.tileentity.misc.MultiTileEntityRock)i).mRock;	//XXX GT
				if(sample == null) {
					// is default rock.
					if(ConfigHandler.trackRock) TakeSampleServer(aWorld, x, y, z, 
							(short)((TileEntityBase03MultiTileEntities) i).getDrops(0, false).get(0).getItemDamage(), Utils.ROCK, aPlayer);
					else Utils.chatAt(aPlayer, ChatString.ROCK);	//"Just normal rock");
				} else if(gregapi.util.OM.is(gregapi.data.OD.itemFlint, sample)) {
					Utils.chatAt(aPlayer, ChatString.FLINT);
				} else if( ! ConfigHandler.trackRock && gregapi.util.OM.materialcontains(sample, gregapi.data.TD.Properties.STONE)) {
					Utils.chatAt(aPlayer, ChatString.ROCK);
				} else 
					TakeSampleServer(aWorld, x, y, z, (short)sample.getItemDamage(), Utils.ROCK, aPlayer);
				return true;
			} return false;
		} else if (gregapi.data.MD.IE.mLoaded)
			return IEHandler.takeExcavatorSample(aWorld, x, y, z, aPlayer, i);
		} else {
			// works client-side, since it's based only on block meta-id
		net.minecraft.block.Block b = aWorld.getBlock(x, y, z);

		if(b instanceof gregapi.block.prefixblock.PrefixBlock) {
			final ItemStack sample = ((gregapi.block.prefixblock.PrefixBlock) b).getItemStackFromBlock(aWorld, x, y, z, gregapi.data.CS.SIDE_INVALID);
			final String tName = b.getUnlocalizedName();
			if(tName.endsWith(".bedrock")) {
				TakeSample(aWorld, x, y, z, sample.getItemDamage(), Utils.BEDROCK, aPlayer);
			} else if (tName.startsWith("gt.meta.ore.normal.")) {
				TakeSample(aWorld, x, y, z, sample.getItemDamage(), Utils.ORE_VEIN, aPlayer);
			} else 
				Utils.chatAt(aPlayer, ChatString.SMALL);// "Small ore, not worth recording");
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
		} else if(ConfigHandler.trackRock &&  b instanceof BlockStones && b.getDamageValue(aWorld, x, y, z) == BlockStones.STONE) {
			TakeSample(aWorld, x, y, z, ((BlockStones) b).mMaterial.mID, Utils.ORE_VEIN, aPlayer);
		}
		}
		return false;
	}
	
//	@cpw.mods.fml.relauncher.SideOnly(cpw.mods.fml.relauncher.Side.SERVER)
	static void TakeSampleServer(final World aWorld, int x, int y, int z, short meta, byte sourceType, final EntityPlayer aPlayer) {
		if (sourceType == Utils.ROCK && ( meta == 8649 || meta == 8757) ) {
			Utils.chatAt(aPlayer, ChatString.METEOR);// "It fell from the sky. Not related to an ore vein.");
//			return;
		} else 
			Utils.NW_PJ.sendToPlayer(new PacketOreSurvey(x, y, z, meta, sourceType), (EntityPlayerMP) aPlayer);
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
//	@cpw.mods.fml.relauncher.SideOnly(cpw.mods.fml.relauncher.Side.CLIENT)
	public static void TakeSample(final World aWorld, int x, int y, int z, int meta, byte sourceType, final EntityPlayer aPlayer) {
		final int dim = aWorld.provider.dimensionId;
		if(ConfigHandler.debug)
			System.out.println(ProspectorJournal.MOD_NAME+"[Info] Sampling "+meta+" at "+ x +","+y+","+z+" on world "+dim);
	    if(sourceType == Utils.FLOWER || sourceType == Utils.BEDROCK) {
//	    	if(!aWorld.isRemote) return; // handled client-side 
	    	boolean match = false;
	    	if(ProspectorJournal.bedrockFault.size() != 0) {
		    	for (GeoTag m : ProspectorJournal.bedrockFault) {
		    		// flower could be non-specific - match any
		    		if(dim == m.dim && meta == m.ore) {
		    			// include adjacent chunks as same unit.
		    			if(m.x >= x - 40 && m.x <= x + 40 && m.z >= z - 40 && m.z <= z + 40) {
		    				match = true;
		    				if(sourceType == Utils.BEDROCK) {
								m.x = x;
		    					m.z = z;
		    					m.sample = false;
		    					m.dead = false;
			    				Utils.writeJson(Utils.GT_BED_FILE);
		    				} else {
		    					Utils.chatAt(aPlayer, ChatString.FLOWERS);// "Sure are plenty of these Flowers here.");
		    				}
		    				break;
		    			}
		    		} else if (m.dim == dim && m.ore == 0 && sourceType == Utils.BEDROCK) {
		    			// found a vein under non-specific flowers
		    			if(m.x >= x - 40 && m.x <= x + 40 && m.z >= z - 40 && m.z <= z + 40) {
		    				ProspectorJournal.bedrockFault.remove(m);
//	    					match = false;
	    					break;
		    			}
    		
		    		} else if(m.dim == dim && meta == 0 && ! m.sample) {
		    			if(m.x >= x - 40 && m.x <= x + 40 && m.z >= z - 40 && m.z <= z + 40) {
		    				match = true;
		    				Utils.chatAt(aPlayer, ChatString.BEDFLOWER);// "I expected to find these Orechids here.");
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
                	case Utils.ROCK:	//result of server-side message only
                		if(n.sample) {
                			n.multiple += 1;
                    		if(n.y > y)
                    			n.y = (short) y;
                		} else {
                			if(n.y > y)
                				continue;
                			Utils.chatAt(aPlayer, ChatString.DUPE);// "I've already found this vein.");
                			return;
                		}
                		break;
                	default:
                    	if(n.sample)
                    		return;
                    	if (n.y > (short) 10)
                    		continue;
						Utils.chatAt(aPlayer, ChatString.RECLASS);// "I've found that vein already!");
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