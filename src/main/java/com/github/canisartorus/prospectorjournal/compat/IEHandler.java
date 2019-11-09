package com.github.canisartorus.prospectorjournal.compat;

import java.util.Map;

import com.github.canisartorus.prospectorjournal.ProspectorJournal;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler.MineralMix;
import gregapi.data.OP;

public class IEHandler {

	public static final String DEPLETED = "*Nil";
	
	public static MineralMix getByName(String sMineral) {
		if (sMineral.equals(DEPLETED))
			return null;
		for (MineralMix variant : blusunrize.immersiveengineering.api.tool.ExcavatorHandler.mineralList.keySet()) {
			if(variant.name.equalsIgnoreCase(sMineral))
				return variant;
		}
		System.out.println(ProspectorJournal.MOD_NAME+"[WARNING] Failed to identify MineralMix: "+sMineral);
		return null;
	}
	
	public static class Dwarf extends com.github.canisartorus.prospectorjournal.lib.Dwarf {

		static Map<String, Map<Short, Integer>> mCache = new java.util.HashMap<>();
		static Map<String, net.minecraft.util.IIcon> faces = new java.util.HashMap<>();
		static Map<String, Short> characters = new java.util.HashMap<>();
		
//		public static int getFractionIn(MineralMix oreSet, short material) {
//		}

		/**
		 * Cached search of the weighted list of drops for a vein type, 
		 * cross-referenced with all those geochemistry for the ultimate list of byproducts
		 * @param oreSet to be checked
		 * @return map of oremat ids to proportional amounts
		 */
		public static Map<Short, Integer> singOf(MineralMix oreSet) {
			Map<Short, Integer> sBy;
			if(oreSet == null)
				return new java.util.HashMap<>(0);
			if(mCache.containsKey(oreSet.name)) {
				sBy = mCache.get(oreSet.name);
			} else { 
				sBy = readManual(oreSet);
				mCache.put(oreSet.name, sBy);
			}
			return sBy;
		}

		/**
		 * Searches the weighted list of drops for a vein type, 
		 * and cross-references all the geochemistry for the ultimate list of byproducts
		 * Spawn weights rounded to parts per thousand
		 * 
		 * Also stores characteristic ore icon in 'faces' Map
		 * @param oreSet to be checked
		 * @return map of oremat ids to proportional amounts
		 */
		@cpw.mods.fml.relauncher.SideOnly(cpw.mods.fml.relauncher.Side.CLIENT)
		static Map<Short, Integer> readManual(MineralMix oreSet) {
			Map<Short, Integer> mProcess = new java.util.HashMap<>();
			if(oreSet == null)
				return mProcess;
			oreSet.recalculateChances();
			if(oreSet.oreOutput.length == 0)
				return mProcess;
			float total =0.0f;
			double iMax = 0.0D;
			short best = 0;
			Map<Short, Double> mContent = new java.util.HashMap<>();
			for(int i =0; i < oreSet.oreOutput.length; i++) {
				total += oreSet.recalculatedChances[i];
				net.minecraft.item.ItemStack realStuff = oreSet.oreOutput[i];
				gregapi.oredict.OreDictItemData matType = gregapi.oredict.OreDictManager.INSTANCE.getItemData(realStuff);
				if(matType != null && matType.hasValidMaterialData()) {
					for (gregapi.oredict.OreDictMaterialStack part : matType.getAllMaterialStacks()) {
						final double iWeigh = part.mAmount * oreSet.recalculatedChances[i];
						if (iWeigh > iMax && ! part.mMaterial.contains(gregapi.data.TD.Properties.STONE) && part.mMaterial.contains(gregapi.data.TD.ItemGenerator.ORES)) {
							iMax = iWeigh;
							best = part.mMaterial.mID;
						}
						mContent.put(part.mMaterial.mID, mContent.getOrDefault(part.mMaterial.mID, 0.0D) + iWeigh);
					}
				}
			}
			if(mContent.isEmpty()) {
				int better = 0;
				for(int i = 0; i < oreSet.oreOutput.length; i++) {
					if(oreSet.recalculatedChances[i] > iMax) {
						iMax = oreSet.recalculatedChances[i];
						better = i;
					}
				}
				faces.put(oreSet.name, oreSet.oreOutput[better].getIconIndex());
				characters.put(oreSet.name, best);
				return mProcess;
			}
			if(best != 0) {
				faces.put(oreSet.name, OP.crushed.mRegisteredPrefixItems.get(0).getIconFromDamage(best));
				characters.put(oreSet.name, best);
			}
			for(java.util.Map.Entry<Short, Double> piece : mContent.entrySet()) {
				if(! faces.containsKey(oreSet.name) && piece.getValue() > iMax) {
					iMax = piece.getValue();
					best = piece.getKey();
				}
				final int pAmt = (int) Math.round(piece.getValue() * 1000 / total / gregapi.data.CS.U);
				if(gregapi.oredict.OreDictMaterial.MATERIAL_ARRAY[piece.getKey()].contains(gregapi.data.TD.ItemGenerator.ORES)) {
					for(java.util.Map.Entry<Short, Integer> byProd : Dwarf.read(piece.getKey()).mByBy.entrySet()) {
						mProcess.put(byProd.getKey(), mProcess.getOrDefault(byProd.getKey(), 0) + byProd.getValue() * pAmt);
					}
				} else {
					mProcess.put(piece.getKey(), mProcess.getOrDefault(piece.getKey(), 0) + pAmt * Dwarf.UNIT);
				}
			}
			faces.putIfAbsent(oreSet.name, OP.dust.mRegisteredPrefixItems.get(0).getIconFromDamage(best));
			characters.putIfAbsent(oreSet.name, best);
			return mProcess;
		}

		/**
		 * Selects a mineral icon for the vein based on the name and weighted drop list.
		 * @param mix The blusunrize.immersiveengineering.api.tool.ExcavatorHandler.MineralMix looking for an icon
		 * @return icon of the characteristic drop from that vein type
		 */
		public static net.minecraft.util.IIcon getIcon(MineralMix mix) {
			net.minecraft.util.IIcon fId = null;
			if(mix == null)
				return null;
			if(faces.containsKey(mix.name)) {
				fId = faces.get(mix.name);
			} else {
				readManual(mix);
				fId = faces.getOrDefault(mix.name, null);
			}
			return fId;
		}

		/**
		 * Attempts to find the closest ore type to the content of this MineralMix.
		 * @param oreSet to search for similarities
		 * @return the gregapi OreDictMaterialID most characteristic of this ore vein
		 */
		public static short getMajor(MineralMix oreSet) {
			short sBest;
			if(oreSet == null)
				return 0;
			if(characters.containsKey(oreSet.name)) {
				sBest = characters.get(oreSet.name);
			} else { 
				readManual(oreSet);
				sBest = characters.getOrDefault(oreSet.name, (short) 0);
			}
			return sBest;
		}
		
	}

}
