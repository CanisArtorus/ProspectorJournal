package com.canisartorus.prospectorjournal.compat;

import java.util.Map;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler.MineralMix;

public class IEHandler {

	
	
	public static class Dwarf extends com.canisartorus.prospectorjournal.lib.Dwarf {

		public static int getFraction(MineralMix oreSet, short material) {
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
