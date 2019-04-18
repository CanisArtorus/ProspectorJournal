package com.github.canisartorus.prospectorjournal.lib;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.github.canisartorus.prospectorjournal.ProspectorJournal;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class Utils {

	public final static String GT_FILE = "GT6OreVeins.json",
			GT_BED_FILE = "GT6BedrockSpots.json",
			DWARF_FILE = "GT6_Geochemistry.json",
			IE_VOID_FILE = "IE_Excavations.json"
			;
	public final static byte ROCK = 0, FLOWER = 1, ORE_VEIN = 2, BEDROCK = 3, EXCAVATOR = 4
			;
	
	public final static int WHITE = 0xFFFFFF, RED = 0xCA1E04, GREEN = 0x26AA30, GRAY = 0xAAAA99
			;
	public static final short DISTANCE = -1; 

	public final static java.util.regex.Pattern patternInvalidChars = java.util.regex.Pattern.compile("[^a-zA-Z0-9_]");
	
	public static String invalidChars(String s) {
		return patternInvalidChars.matcher(s).replaceAll("_");
	}

    public static void writeJson(String name) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = null;
        switch(name) {
        case GT_FILE:
        	json = gson.toJson(ProspectorJournal.rockSurvey);
        	break;
        case GT_BED_FILE:
        	json = gson.toJson(ProspectorJournal.bedrockFault);
        	break;
        case DWARF_FILE:
        	json = gson.toJson(com.github.canisartorus.prospectorjournal.lib.Dwarf.knowledge);
        	break;
        case IE_VOID_FILE:
        	json = gson.toJson(ProspectorJournal.voidVeins);
        }

        if (json == null) throw new java.lang.IllegalArgumentException(ProspectorJournal.MOD_ID + ": " + name + " is not a recognized data file.");
        try {
			if(com.github.canisartorus.prospectorjournal.ConfigHandler.debug) {
				System.out.println("Attempting to write to "+ ProspectorJournal.hostName+"/"+ name);
			}
            FileWriter fw = new FileWriter(ProspectorJournal.hostName + "/" + name);
            fw.write(json);
            fw.close();
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println(ProspectorJournal.MOD_ID + ": Could not write to " + name + "!");
        }
    }

    public static void readJson(String name) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(ProspectorJournal.hostName + "/" + name));
            Gson gson = new Gson();
            //ProspectorJournal.rockSurvey = gson.fromJson(br, ProspectorJournal.rockSurvey.getClass());
            switch(name) {
            case GT_FILE:
            	ProspectorJournal.rockSurvey = gson.fromJson(br, new TypeToken<java.util.List<RockMatter>>(){}.getType());
            	break;
            case GT_BED_FILE:
            	ProspectorJournal.bedrockFault = gson.fromJson(br, new TypeToken<java.util.List<GeoTag>>(){}.getType());
            	break;
			case IE_VOID_FILE:
				ProspectorJournal.voidVeins = gson.fromJson(br, new TypeToken<java.util.List<VoidMine>>(){}.getType());
            }
            br.close();
        } catch ( IOException e) {
            //e.printStackTrace();
            System.out.println(ProspectorJournal.MOD_ID + ": No " + name + " file found.");
        }

    }
    
    public static boolean inBounds(int t, int a, int b) {
    	return t <= b && t >= a;
    }

    /**
     * Expresses an integer in Engineering notation with 3 significant figures
     */
	public static String approx(int multiple) {
		String rNum = Integer.toString(multiple);
		if(rNum.length() > 3) {
			String[] prefix = new String[] {"", "k", "M", "G", "T"};
			switch(rNum.length() % 3) {
			case 0:
				return rNum.substring(0, 3) + prefix[(rNum.length() / 3)-1];
			case 1:
				return rNum.charAt(0) + "." + rNum.substring(1, 3) +prefix[rNum.length() /3 ];
			case 2:
				return rNum.substring(0, 2) + "." +rNum.charAt(2) +prefix[rNum.length() /3];
			}
		}
		return rNum;
	}
}