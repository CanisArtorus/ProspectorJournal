package com.github.canisartorus.prospectorjournal.lib;

import gregapi.oredict.OreDictMaterial;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.github.canisartorus.prospectorjournal.ConfigHandler;
import com.github.canisartorus.prospectorjournal.ProspectorJournal;

/**
 * 
 * @author Alexander James
 *
 * Derived data regarding ore availability and byproducts.
 */
public class Dwarf {
	//	calculated trace ratios for ores to products
	static final int
		N_PURE = 36,		C_PURE = 9*8,
		N_SELF = 44,		C_SELF = 12*8,
		N_BY_ONLY = 8,		C_BY_ONLY = 3*8,
		N_BY_TWO = 4,		C_BYTWO_1 = 2*8,	C_BYTWO_2 = 1*8,
		N_BYTHREE = 3,		N_BYTHREE_3 = 2,	C_BY_THREE = 1*8,
		N_MINOR = 1,		N_MAJOR = 2,		C_MAJOR = 1*8,
		UNIT = N_PURE*C_PURE;
	static java.util.Collection<GeoChemistry> knowledge = new java.util.ArrayList<>();
	
	public static GeoChemistry read(int material) {
		for( GeoChemistry gc : knowledge) {
			if(gc.mID == material) return gc;
		}
		return new GeoChemistry((short) material);
	}
	
	/**
	 * Returns the proportion of one material that can be retrieved from an ore.
	 * @param material the matID for the ore being checked
	 * @param ore the matID for a desired product
	 * @return parts per Dwarf.UNIT available
	 */
	public static int getFractionIn(short material, short ore) {
		GeoChemistry gc = read(material);
		if(gc.mByBy.size() != 0) 
			return gc.mByBy.getOrDefault(ore, 0);
		if (gc.mBy.size() != 0)
			return C_PURE * gc.mBy.getOrDefault(ore,  0);
		if(material == ore)	
			return N_SELF * C_SELF;
		return 0;
	}
	public static int getFractionIn(MineralMine deposit, short product) {
		if (deposit instanceof GeoTag)
			return Dwarf.getFractionIn(((GeoTag)deposit).ore, product);
		if (deposit instanceof VoidMine)
			return com.github.canisartorus.prospectorjournal.compat.IEHandler.Dwarf.getFractionIn(((VoidMine)deposit).oreSet, product);
		return 0;
	}

	
	/**
	 *  Reads the list of materials and byproducts to build a data table
	 */
	public static void readTheStones() {
		System.out.println(ProspectorJournal.MOD_ID + ": Reading all the material dictionaries.");
		for (OreDictMaterial odm : OreDictMaterial.MATERIAL_MAP.values()) {
			if(odm.contains(gregapi.data.TD.ItemGenerator.ORES)) {
				GeoChemistry gc = new GeoChemistry(odm.mID);
				switch (odm.mByProducts.size()) {
				case 0:
					gc.mBy.put(odm.mID, N_SELF);
					gc.mBy2.put(odm.mID, C_SELF);
					break;
				case 1:
					gc.mBy.put(odm.mByProducts.get(0).mID, N_BY_ONLY);
					gc.mBy2.put(odm.mByProducts.get(0).mID, C_BY_ONLY);
					break;
				case 2:
					gc.mBy.put(odm.mByProducts.get(0).mID, N_BY_TWO);
					gc.mBy2.put(odm.mByProducts.get(0).mID, C_BYTWO_1);
					gc.mBy.put(odm.mByProducts.get(1).mID, N_BY_TWO);
					gc.mBy2.put(odm.mByProducts.get(1).mID, C_BYTWO_2);
					break;
				case 3:
					gc.mBy.put(odm.mByProducts.get(0).mID, N_BYTHREE);
					gc.mBy2.put(odm.mByProducts.get(0).mID, C_BY_THREE);
					gc.mBy.put(odm.mByProducts.get(1).mID, N_BYTHREE);
					gc.mBy2.put(odm.mByProducts.get(1).mID, C_BY_THREE);
					gc.mBy.put(odm.mByProducts.get(2).mID, N_BYTHREE_3);
					gc.mBy2.put(odm.mByProducts.get(2).mID, C_BY_THREE);
					break;
				case 7:
					gc.mBy.put(odm.mByProducts.get(1).mID, N_MINOR);
					gc.mBy.put(odm.mByProducts.get(6).mID, N_MINOR);
				case 6:
					gc.mBy.put(odm.mByProducts.get(2).mID, N_MINOR);
					gc.mBy.put(odm.mByProducts.get(5).mID, N_MINOR);
				case 5:
					gc.mBy.put(odm.mByProducts.get(3).mID, N_MINOR);
					gc.mBy.put(odm.mByProducts.get(4).mID, N_MINOR);
				case 4:
					gc.mBy.put(odm.mByProducts.get(0).mID, N_MAJOR);
					gc.mBy2.put(odm.mByProducts.get(0).mID, C_MAJOR);
					gc.mBy.putIfAbsent(odm.mByProducts.get(1).mID, N_MAJOR);
					gc.mBy2.put(odm.mByProducts.get(1).mID, C_MAJOR);
					gc.mBy.putIfAbsent(odm.mByProducts.get(2).mID, N_MAJOR);
					gc.mBy2.put(odm.mByProducts.get(2).mID, C_MAJOR);
					gc.mBy.putIfAbsent(odm.mByProducts.get(3).mID, N_MAJOR);
					break;
				case 8:
				default:
					for (int i = 0; i< 8; i++) {
						gc.mBy.put(odm.mByProducts.get(i).mID, N_MINOR);
					}
					gc.mBy2.put(odm.mByProducts.get(0).mID, C_MAJOR);
					gc.mBy2.put(odm.mByProducts.get(1).mID, C_MAJOR);
					gc.mBy2.put(odm.mByProducts.get(2).mID, C_MAJOR);
				}
				gc.mBy.putIfAbsent(odm.mID, N_PURE);
				gc.mBy2.putIfAbsent(odm.mID, C_PURE);
				if(ConfigHandler.allowSmelt) {
					if(ConfigHandler.debug)
						System.out.println("smelting "+odm.mID);
					final short tID = odm.mTargetSmelting.mMaterial.mID;
					final int purity = (int) (odm.mTargetSmelting.mAmount / gregapi.data.CS.U72);
					if (purity > gc.mBy2.getOrDefault(tID, 0)) 
						gc.mBy2.put(tID,  purity);
				}
				knowledge.add(gc);
			}
		}
		System.out.println(ProspectorJournal.MOD_ID + ": Compiled bydproduct data for " + knowledge.size() + " ore types.");
		for(GeoChemistry gc : knowledge) {
			for(short byID : gc.mBy.keySet()) {
				GeoChemistry byPr = read(byID);
				int traceRate = gc.mBy.get(byID);
				for(short traceID : byPr.mBy2.keySet()) {
					gc.add(traceID, byPr.mBy2.get(traceID) * traceRate);
				}
			}
		}
		System.out.println(ProspectorJournal.MOD_ID + ": Crossreferenced the byproduct data.");
		if(ConfigHandler.exportDwarf) {
	        File fileJson = new File(ProspectorJournal.hostName);
	        if (!fileJson.exists()) {
				System.out.println("Creating new directory "+ ProspectorJournal.hostName);
	            fileJson.mkdirs();
	        }
			Utils.writeJson(Utils.DWARF_FILE);
		}
	}
		
	/**
	 * 
	 * @param mat	a Greg material System ID number
	 * @return The localized display name
	 */
	public static String name(short mat) {
		return OreDictMaterial.MATERIAL_ARRAY[mat].mNameLocal;
	}
	
	/**
	 * 
	 * @author Alexander James
	 *
	 * Data structure holding the byproducts table for a material.
	 */
	public static class GeoChemistry {
		public final short mID;
		//Direct Byproducts
		public final Map<Short, Integer> mBy = new HashMap<>(4);
		//Direct Byproducts in centrifuge + smelting conversion.
		public final Map<Short, Integer> mBy2 = new HashMap<>(6, 0.99f);
		// Total, including indirect, byproducts
		public final Map<Short, Integer> mByBy = new HashMap<>(15, 0.9f);
		
		public GeoChemistry(short aID) {
			mID = aID;
		}
		
		public void add(short mat, int amount) {
			int old = mByBy.getOrDefault(mat, 0);
			mByBy.put(mat, old + amount);
		}
	}
	
	static private final int[] weightsMeasure = new int[] {
			8*3*5*7, 0, 24*35, 12*35, 8*35, 6*35, 24*7, 4*35, 24*5, 3*35
	};

	/**
	 * For Bedrock veins; includes the chance to generate byproduct ore blocks.
	 * @param ore	the major ore type ID
	 * @return	the extended byproduct table
	 */
	public static Map<Short, Integer> singOf(short ore) {
		Map<Short, Integer> rMap = new HashMap<>();
		final Set<Short> allByprod = Dwarf.read(ore).mBy.keySet();
		for(short tID : allByprod) {
			for(Entry<Short, Integer> tPair : Dwarf.read(tID).mByBy.entrySet()) {
				int tI = rMap.getOrDefault(tPair.getKey(), 0);
				if(tID == ore) {
					if(allByprod.size() ==1)
						rMap.put(tPair.getKey(), tPair.getValue() *32*weightsMeasure[0] + tI);
					else
						rMap.put(tPair.getKey(), tPair.getValue() *31*weightsMeasure[0] + tI);
				} else {
					rMap.put(tPair.getKey(), tPair.getValue() *weightsMeasure[allByprod.size()] + tI);
				}
			}
		}
		return rMap;
	}

}