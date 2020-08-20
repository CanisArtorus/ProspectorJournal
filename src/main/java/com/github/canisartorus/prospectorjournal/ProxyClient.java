package com.github.canisartorus.prospectorjournal;

import gregapi.api.Abstract_Proxy;


/**
 * @author Alexander James
 * 
 * An example implementation for a Clientside Proxy using my System.
 */
public final class ProxyClient extends Abstract_Proxy {
	// Insert your Clientside-only implementation of Stuff here
    static Map<String, IIcon> faces = new HashMap<>();

    static void openGuiMain(){
        net.minecraft.client.Minecraft.getMinecraft().displayGuiScreen(new com.github.canisartorus.prospectorjournal.gui.GuiMain());
    }

	static void faces3(String oreName, short best) {
		faces.putIfAbsent(oreName, OP.dust.mat(OreDictMaterial.MATERIAL_ARRAY[best], 1).getIconIndex());
	}
	
	static void faces2(String oreName, short iMat) {
		faces.put(oreName, OP.crushed.mat(OreDictMaterial.MATERIAL_ARRAY[iMat], 1).getIconIndex());
	}
	
	static void faces1(String oreName, net.minecraft.item.ItemStack oreOutput) {
		faces.put(oreName, oreOutput.getIconIndex());
	}

}