package com.github.canisartorus.prospectorjournal.lib;

import java.util.HashMap;
import java.util.Map;

import com.github.canisartorus.prospectorjournal.compat.IEHandler;
import com.github.canisartorus.prospectorjournal.ProspectorJournal;
import com.github.canisartorus.prospectorjournal.ProxyServer;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler.MineralMix;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregapi.data.MD;
import gregapi.data.OP;
import gregapi.data.TD;
import gregapi.old.Textures;
import gregapi.oredict.OreDictMaterial;
import net.minecraft.util.IIcon;

public class IEDwarf  {

		static Map<String, Map<Short, Integer>> mCache = new HashMap<>();
//		static Map<String, IIcon> faces = new HashMap<>();
		static Map<String, Short> characters = new HashMap<>();

		private final ProxyServer PROXY = (ProxyServer)ProspectorJournal.PROXY;
		
//		public static int getFractionIn(MineralMix oreSet, short material) {
//		}

		/**
		 * Cached search of the weighted list of drops for a vein type, 
		 * cross-referenced with all those geochemistry for the ultimate list of byproducts
		 * @param oreName of the MineralMix to be checked
		 * @return map of oremat ids to proportional amounts
		 */
		public static Map<Short, Integer> singOf(String oreName) {
			if(oreName == null | oreName.isEmpty() | ! MD.IE.mLoaded) 
				return new HashMap<>(0);
			Map<Short, Integer> sBy;
			if(mCache.containsKey(oreName)) {
				sBy = mCache.get(oreName);
			} else {
				MineralMix tMix = IEHandler.getByName(oreName);
				if(tMix == null)	return new HashMap<>(0);
				sBy = readManual(tMix);
				mCache.put(oreName, sBy);
			}
			return sBy;
		}
//		@Deprecated
//		public static Map<Short, Integer> singOf(MineralMix oreSet) {
//			Map<Short, Integer> sBy;
//			if(oreSet == null)
//				return new HashMap<>(0);
//			if(mCache.containsKey(oreSet.name)) {
//				sBy = mCache.get(oreSet.name);
//			} else { 
//				sBy = readManual(oreSet);
//				mCache.put(oreSet.name, sBy);
//			}
//			return sBy;
//		}

		/**
		 * Searches the weighted list of drops for a vein type, 
		 * and cross-references all the geochemistry for the ultimate list of byproducts
		 * Spawn weights rounded to parts per thousand
		 * 
		 * Also stores characteristic ore icon in 'faces' Map client-side
		 * @param oreSet to be checked
		 * @return map of oremat ids to proportional amounts
		 */
		static Map<Short, Integer> readManual(MineralMix oreSet) {
			Map<Short, Integer> mProcess = new HashMap<>();
			if(oreSet == null)
				return mProcess;
			oreSet.recalculateChances();
			if(oreSet.oreOutput.length == 0)
				return mProcess;
			float total =0.0f;
			double iMax = 0.0D;
			short best = 0;
			Map<Short, Double> mContent = new HashMap<>();
			for(int i =0; i < oreSet.oreOutput.length; i++) {
				total += oreSet.recalculatedChances[i];
				net.minecraft.item.ItemStack realStuff = oreSet.oreOutput[i];
				gregapi.oredict.OreDictItemData matType = gregapi.oredict.OreDictManager.INSTANCE.getItemData(realStuff);
				if(matType != null && matType.hasValidMaterialData()) {
					for (gregapi.oredict.OreDictMaterialStack part : matType.getAllMaterialStacks()) {
						final double iWeigh = part.mAmount * oreSet.recalculatedChances[i];
						if (iWeigh > iMax && ! part.mMaterial.contains(TD.Properties.STONE) && part.mMaterial.contains(TD.ItemGenerator.ORES)) {
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
				PROXY.faces1(oreSet.name, oreSet.oreOutput[better]);
//				characters.put(oreSet.name, best);
				return mProcess;
			}
			if(best != 0) {
				PROXY.faces2(oreSet.name, best);
				characters.put(oreSet.name, best);
			}
			for(Map.Entry<Short, Double> piece : mContent.entrySet()) {
				if(! characters.containsKey(oreSet.name) && piece.getValue() > iMax) {
					iMax = piece.getValue();
					best = piece.getKey();
				}
				final int pAmt = (int) Math.round(piece.getValue() * 1000 / total / gregapi.data.CS.U);
				if(OreDictMaterial.MATERIAL_ARRAY[piece.getKey()].contains(TD.ItemGenerator.ORES)) {
					for(Map.Entry<Short, Integer> byProd : Dwarf.read(piece.getKey()).mByBy.entrySet()) {
						mProcess.put(byProd.getKey(), mProcess.getOrDefault(byProd.getKey(), 0) + byProd.getValue() * pAmt);
					}
				} else {
					mProcess.put(piece.getKey(), mProcess.getOrDefault(piece.getKey(), 0) + pAmt * Dwarf.UNIT);
				}
			}
			PROXY.faces3(oreSet.name, best);
			characters.putIfAbsent(oreSet.name, best);
			return mProcess;
		}
/*		@SideOnly(Side.CLIENT)
		private static void faces3(String oreName, short best) {
			faces.putIfAbsent(oreName, OP.dust.mat(OreDictMaterial.MATERIAL_ARRAY[best], 1).getIconIndex());
		}
		@SideOnly(Side.CLIENT)
		private static void faces2(String oreName, short iMat) {
//			faces.put(oreName, ((net.minecraft.item.Item)OP.crushed.mRegisteredPrefixItems.get(0)).getIconFromDamage(iMat));
			faces.put(oreName, OP.crushed.mat(OreDictMaterial.MATERIAL_ARRAY[iMat], 1).getIconIndex());
		}
		@SideOnly(Side.CLIENT)
		private static void faces1(MineralMix oreSet, int iMat) {
			faces.put(oreSet.name, oreSet.oreOutput[iMat].getIconIndex());
		}
*/
		/**
		 * Selects a mineral icon for the vein based on the name and weighted drop list.
		 * @param mix Name of a blusunrize.immersiveengineering.api.tool.ExcavatorHandler.MineralMix looking for an icon
		 * @return icon of the characteristic drop from that vein type
		 */
		@SideOnly(Side.CLIENT)
		public static IIcon getIcon(String aMix) {
			if(aMix == null | aMix.isEmpty())
				return Textures.ItemIcons.VOID.getIcon(0);
			if(MD.IE.mLoaded) {
				if(PROXY.faces.containsKey(aMix)) 
					return PROXY.faces.get(aMix);
				MineralMix tMix = IEHandler.getByName(aMix);
				if(tMix == null) return Textures.ItemIcons.RENDERING_ERROR.getIcon(0);
				readManual(tMix);
			} else {
				OreDictMaterial tODM = OreDictMaterial.get(aMix);
				if (tODM == null | tODM.mID < 0) {
					PROXY.faces.put(aMix, Textures.ItemIcons.VOID.getIcon(0));
				} else if (! tODM.contains(TD.Properties.STONE) && tODM.contains(TD.ItemGenerator.ORES)) {
					PROXY.faces2(aMix, tODM.mID);
				} else {
					PROXY.faces3(aMix, tODM.mID);
				}
			}
			return PROXY.faces.getOrDefault(aMix, Textures.ItemIcons.RENDERING_ERROR.getIcon(0));
		}
//		@Deprecated
//		@SideOnly(Side.CLIENT)
//		public static IIcon getIcon(MineralMix mix) {
//			IIcon fId = null;
//			if(mix == null)
//				return null;
//			if(faces.containsKey(mix.name)) {
//				fId = faces.get(mix.name);
//			} else {
//				readManual(mix);
//				fId = faces.getOrDefault(mix.name, null);
//			}
//			return fId;
//		}

		/**
		 * Attempts to find the closest ore type to the content of this MineralMix.
		 * @param oreSet to search for similarities
		 * @return the gregapi OreDictMaterialID most characteristic of this ore vein
		 */
		public static short getMajor(String oreName) {
			if(oreName == null | oreName.isEmpty() ) 
				return 0;
			if(characters.containsKey(oreName))
				return characters.get(oreName);
			if(MD.IE.mLoaded) {
//				return getMajor(IEHandler.getByName(oreName));
				MineralMix tMix = IEHandler.getByName(oreName);
				if(tMix == null)	return 0;
				readManual(tMix);
				return characters.getOrDefault(oreName, (short) 0);
			}
			short tId = OreDictMaterial.get(oreName).mID;
			characters.put(oreName, tId);
			return tId > 0 ? tId : 0;
		}
//		protected static short getMajor(MineralMix oreSet) {
//			short sBest;
//			if(oreSet == null)
//				return 0;
//			if(characters.containsKey(oreSet.name)) {
//				sBest = characters.get(oreSet.name);
//			} else { 
//				readManual(oreSet);
//				sBest = characters.getOrDefault(oreSet.name, (short) 0);
//			}
//			return sBest;
//		}
		
	}