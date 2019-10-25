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

		public static int getFractionIn(MineralMix oreSet, short material) {
			// TODO Auto-generated method stub
			return 0;
		}

		/**
		 * Searches the weighted list of drops for a vein type, 
		 * and cross-references all the geochemistry for the ultimate list of byproducts
		 * @param oreSet to be checked
		 * @return map of oremat ids to proportional amounts
		 */
		public static Map<Short, Integer> singOf(MineralMix oreSet) {
			// TODO Auto-generated method stub
			return null;
		}

		public static short getMajor(MineralMix mix) {
			// TODO Auto-generated method stub
			return 0;
		}
		
	}

}
