package com.canisartorus.prospectorjournal;

// @author Alexander James 2019-03-24

import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {
	public static Configuration tMainConfig;
	
	public static boolean bookOnly, needHUD,
		allowSmelt, exportDwarf
		;
	public static double arrowSize
		;
	public static int arrowX, arrowY
		;
	public static String[] HUDsList;
	
	public static void init(java.io.File configFile) {
		boolean bDirty = false;
		if (tMainConfig == null) {
			tMainConfig = new Configuration(configFile);
			bDirty = true;
		}
		tMainConfig.load();
		
		ConfigHandler.bookOnly = tMainConfig.getBoolean("DiageticMode_false", "general", false, "If true only the Notebook can add sample locations, navigation only works while you are holding the Notebook, and the SuveyData screen can only be accessed by reading the Notebook.");
		ConfigHandler.needHUD = tMainConfig.getBoolean("HelmetHUD_true", "general", true, "If the tracking arrow should only be visible when wearing headgear with an integrated HUD.");
		ConfigHandler.HUDsList = tMainConfig.getStringList("Available HUDs", "general", new String[] {"glasses", "item.BiblioGlasses", "sonicglasses", "item.logisticHUDGlasses", "reactorcraft_item_goggles", "rotarycraft_item_iogoggles", "armor.goggles", "ItemGoggles", "magitechGoggles", "pneumaticHelmet", "naturalistHelmet"}, "The internal names of every piece of headgear that is considered have a HUD.");

		ConfigHandler.allowSmelt = tMainConfig.getBoolean("Include smelting transform_true", "Ore Helper", true, "Chose if it should detect melting in a crucible as an allowed method to get a product from the ore. Disable to get only sluice / sifter / centrifuge by products.");
		ConfigHandler.exportDwarf = tMainConfig.getBoolean("ExportFile_false", "Ore Helper", false, "Set to true to export ore by product data to $save$/ProspectorJournal/GT6_Geochemistry.json. \n I suggest excluding Smelting transforms for this.");
		
		ConfigHandler.arrowX = tMainConfig.getInt("Arrow X Coord_0", "Pointer", 0, -512, 512, "Horizontal offset from screen centre of the navigation pointer.");
		ConfigHandler.arrowY = tMainConfig.getInt("Arrow Y Coord_0", "Pointer", 0, -512, 512, "Vertical offset from screen centre of the navigation pointer.");
		ConfigHandler.arrowSize = tMainConfig.get("Pointer", "Arrow Scale_1", 1.0D, "Relative size of the navigation overlay pointer.", 0.01D, 4.0D).getDouble(1.0D);
		
		// XXX additional configs go inside here
		
		if (bDirty)
			tMainConfig.save();
	}

}