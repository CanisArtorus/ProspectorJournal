package com.canisartorus.prospectorjournal.lib;

// @Author Dyonovan

import com.canisartorus.prospectorjournal.ProspectorJournal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;

public class Utils {

	public final static String GT_FILE = "GT6OreVeins.json";

	public final static java.util.regex.Pattern patternInvalidChars = java.util.regex.Pattern.compile("[^a-zA-Z0-9_]");
	
	public static String invalidChars(String s) {
		return patternInvalidChars.matcher(s).replaceAll("_");
	}

    public static void writeJson(String name) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(ProspectorJournal.rockSurvey);

        try {
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
            ProspectorJournal.rockSurvey = gson.fromJson(br, new TypeToken<java.util.List<com.canisartorus.prospectorjournal.lib.RockMatter>>(){}.getType());
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            System.out.println(ProspectorJournal.MOD_ID + ": No " + name + " file found.");
        }

    }
}