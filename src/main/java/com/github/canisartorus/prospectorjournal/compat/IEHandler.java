package com.github.canisartorus.prospectorjournal.compat;

import java.util.Map;

import com.github.canisartorus.prospectorjournal.ProspectorJournal;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler.MineralMix;

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
		static Map<String, Short> faces = new java.util.HashMap<>();
		
		public static int getFractionIn(MineralMix oreSet, short material) {
			// TODO Auto-generated method stub
			return 0;
		}

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
		 * @param oreSet to be checked
		 * @return map of oremat ids to proportional amounts
		 */
		static Map<Short, Integer> readManual(MineralMix oreSet) {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * Selects a mineral icon for the vein based on the name and weighted drop list.
		 * @param mix The blusunrize.immersiveengineering.api.tool.ExcavatorHandler.MineralMix looking for an icon
		 * @return the gregapi material id of the characteristic material in that vein type
		 */
		public static short getMajor(MineralMix mix) {
			// TODO Auto-generated method stub
			return 0;
		}
		
	}

}
