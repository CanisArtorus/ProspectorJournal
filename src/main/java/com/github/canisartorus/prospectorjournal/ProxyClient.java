package com.github.canisartorus.prospectorjournal;

import com.github.canisartorus.prospectorjournal.gui.GuiMain;

import gregapi.oredict.OreDictMaterial;
import gregapi.data.OP;

import net.minecraft.util.IIcon;
import net.minecraft.item.ItemStack;
import net.minecraft.client.Minecraft;

import java.util.Map;
import java.util.HashMap;


/**
 * @author Alexander James
 * 
 * An example implementation for a Clientside Proxy using my System.
 */
public final class ProxyClient extends ProxyServer {
	// Insert your Clientside-only implementation of Stuff here
    Map<String, IIcon> faces = new HashMap<>();

	@Override
    void openGuiMain(){
        Minecraft.getMinecraft().displayGuiScreen(new GuiMain());
    }

	@Override
	void faces3(String oreName, short best) {
		faces.putIfAbsent(oreName, OP.dust.mat(OreDictMaterial.MATERIAL_ARRAY[best], 1).getIconIndex());
	}
	
	@Override
	void faces2(String oreName, short iMat) {
		faces.put(oreName, OP.crushed.mat(OreDictMaterial.MATERIAL_ARRAY[iMat], 1).getIconIndex());
	}
	
	@Override
	void faces1(String oreName, ItemStack oreOutput) {
		faces.put(oreName, oreOutput.getIconIndex());
	}

}