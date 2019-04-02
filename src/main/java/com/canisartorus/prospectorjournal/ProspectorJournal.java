package com.canisartorus.prospectorjournal;

/**
 * @author Max Mustermann
 * @author Dyonovan
 * @author Alexander James
 * 
 * 
 * Core file for the Prospector's Journal mod.
 * This keeps track of ore bearing rocks, and indicator flowers,
 * in order to make waypointing everywhere unnecessary 
 * to find the large ore Veins as are present in GT6.
 * 
 *  Built primarily off of TCNodeTracker at https://github.com/Dyonovan/TCNodeTracker
 *  and thus remains under Creative Commons CC-BY-NC-SA4.0 (attribution, non-commercial, share-alike)
 */
 
 import cpw.mods.fml.common.Mod.EventHandler;
 import cpw.mods.fml.common.event.*;
 //import cpw.mods.fml.common.registry.GameRegistry;
 
 import java.util.ArrayList;
 import java.util.List;
  
@cpw.mods.fml.common.Mod(modid=ProspectorJournal.MOD_ID, name=ProspectorJournal.MOD_NAME, version=ProspectorJournal.VERSION, dependencies="required-after:gregapi_post; after:gregtech; after:immersiveengineering")
public final class ProspectorJournal extends gregapi.api.Abstract_Mod {
	/** Your Mod-ID has to be LOWERCASE and without Spaces. Uppercase Chars and Spaces can create problems with Resource Packs. This is a vanilla forge "Issue". */
	public static final String MOD_ID = "prospectorjournal"; 
	/** This is your Mods Name */
	public static final String MOD_NAME = "ProspectorJournal"; 
	/** This is your Mods Version */
	public static final String VERSION = "ProspectorJournal-MC1710-0.0.0"; 
	/** Contains a ModData Object for ID and Name. Doesn't have to be changed. */
	public static gregapi.code.ModData MOD_DATA = new gregapi.code.ModData(MOD_ID, MOD_NAME);

	@cpw.mods.fml.common.SidedProxy(modId = MOD_ID, clientSide = "com.canisartorus.prospectorjournal.ProxyClient", serverSide = "com.canisartorus.prospectorjournal.ProxyServer")
    public static gregapi.api.Abstract_Proxy PROXY;
	
	/*
	@cpw.mods.fml.common.Mod.Instance(MOD_ID)
	public static ProspectorJournal instance;
	*/
	
	public static String hostName;
	public static boolean doGui = false;
	public static int xMarker, yMarker, zMarker;
	public static List<com.canisartorus.prospectorjournal.lib.DimTag> dims 			= new ArrayList<>();
	public static List<com.canisartorus.prospectorjournal.lib.RockMatter> rockSurvey = new ArrayList<>();
	public static List<com.canisartorus.prospectorjournal.lib.GeoTag> bedrockFault 	= new ArrayList<>();
	public static List<com.canisartorus.prospectorjournal.lib.VoidMine> voidVeins	= new ArrayList<>(); 

	@Override public String getModID() {return MOD_ID;}
	@Override public String getModName() {return MOD_NAME;}
	@Override public String getModNameForLog() {return "Prospector_Journal";}
	@Override public gregapi.api.Abstract_Proxy getProxy() {return PROXY;}

	// Do not change these 7 Functions. Just keep them this way.
	@EventHandler public final void onPreLoad           (FMLPreInitializationEvent    aEvent) {onModPreInit(aEvent);}
	@EventHandler public final void onLoad              (FMLInitializationEvent       aEvent) {onModInit(aEvent);}
	@EventHandler public final void onPostLoad          (FMLPostInitializationEvent   aEvent) {onModPostInit(aEvent);}
	@EventHandler public final void onServerStarting    (FMLServerStartingEvent       aEvent) {onModServerStarting(aEvent);}
	@EventHandler public final void onServerStarted     (FMLServerStartedEvent        aEvent) {onModServerStarted(aEvent);}
	@EventHandler public final void onServerStopping    (FMLServerStoppingEvent       aEvent) {onModServerStopping(aEvent);}
	@EventHandler public final void onServerStopped     (FMLServerStoppedEvent        aEvent) {onModServerStopped(aEvent);}

	@Override
	public void onModPreInit2(FMLPreInitializationEvent aEvent) {
		// Make new items, add them to OreDicts, and do recipes using only internal items.
		com.canisartorus.prospectorjournal.ConfigHandler.init(aEvent.getSuggestedConfigurationFile());
		
		Items.RegisterItems();
		net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new com.canisartorus.prospectorjournal.RightClickEvent());
	}

	@Override
	public void onModInit2(FMLInitializationEvent aEvent) {
		// Init gets the recipes that took oredict entries, or otherwise things from other mods to build.
		Items.RegisterRecipes();
		com.canisartorus.prospectorjournal.KeyBindings.init();
		com.canisartorus.prospectorjournal.lib.Dwarf.readTheStones();
	}
	
	@Override
	public void onModPostInit2(FMLPostInitializationEvent aEvent) {
		// Insert your PostInit Code here and not above
	// }
	
	// @cpw.mods.fml.relauncher.SideOnly(cpw.mods.fml.relauncher.Side.CLIENT)
	// @EventHandler
	// public void postInit(FMLPostInitializationEvent event) {
//		 net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new com.canisartorus.prospectorjournal.GuiPointer(net.minecraft.client.Minecraft.getMinecraft()));
	}
	
	@Override
	public void onModServerStarting2(FMLServerStartingEvent aEvent) {
		// Insert your ServerStarting Code here and not above
	}
	
	@Override
	public void onModServerStarted2(FMLServerStartedEvent aEvent) {
		// Insert your ServerStarted Code here and not above
	}
	
	@Override
	public void onModServerStopping2(FMLServerStoppingEvent aEvent) {
		// Insert your ServerStopping Code here and not above
	}
	
	@Override
	public void onModServerStopped2(FMLServerStoppedEvent aEvent) {
		// Insert your ServerStopped Code here and not above
	}
}
