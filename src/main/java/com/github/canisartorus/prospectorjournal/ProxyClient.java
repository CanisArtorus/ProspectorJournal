package com.github.canisartorus.prospectorjournal;

import com.github.canisartorus.prospectorjournal.gui.*;

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
    public Map<String, IIcon> faces = new HashMap<>();

	@Override
    public void openGuiMain(){
        Minecraft.getMinecraft().displayGuiScreen(new GuiMain());
    }

	@Override
	public void faces3(String oreName, short best) {
		faces.putIfAbsent(oreName, OP.dust.mat(OreDictMaterial.MATERIAL_ARRAY[best], 1).getIconIndex());
	}
	
	@Override
	public void faces2(String oreName, short iMat) {
		faces.put(oreName, OP.crushed.mat(OreDictMaterial.MATERIAL_ARRAY[iMat], 1).getIconIndex());
	}
	
	@Override
	public void faces1(String oreName, ItemStack oreOutput) {
		faces.put(oreName, oreOutput.getIconIndex());
	}
	
	@Override
	public void registerKeybindings(){
		cpw.mods.fml.common.FMLCommonHandler.instance().bus().register(new KeyBindings());
	}

	@Override
	public void initKeybinds(){ KeyBindings.init(); }
	
	@Override
	public void registerPointer(){
		net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(
			new GuiPointer(net.minecraft.client.Minecraft.getMinecraft())
		);
	}
}