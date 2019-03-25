package com.canisartorus.prospectorjournal;

// @author Alexander James 2019-03-24

import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {
	public static Configuration tMainConfig;
	
	public static boolean bookOnly;
	
	public void init(java.io.File configFile) {
		boolean bDirty = false;
		if (tMainConfig == null) {
			tMainConfig = new Configuration(configFile);
			bDirty = true;
		}
		tMainConfig.load();
		
		this.bookOnly = tMainConfig.get("general", "DiageticMode_false", false, "If true only the Notebook can add sample locations, navigation only works while you are holding the Notebook, and the SuveyData screen can only be accessed by reading the Notebook.").setShowInGui(true).getBoolean(false);
		
		if (bDirty)
			tMainConfig.save();
	}

}