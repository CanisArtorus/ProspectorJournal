package com.github.canisartorus.prospectorjournal;

// @Author Dyonovan

public class KeyBindings {
	public static net.minecraft.client.settings.KeyBinding rocksMenu;
	
	public static void init() {
	  rocksMenu = new net.minecraft.client.settings.KeyBinding("key.geoSurveyMenu", org.lwjgl.input.Keyboard.KEY_O, "key.cat.prospectorjournal");
	  cpw.mods.fml.client.registry.ClientRegistry.registerKeyBinding(rocksMenu);
	}
	
	@cpw.mods.fml.common.eventhandler.SubscribeEvent
	public void onKeyInput(cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent event) {
		if (rocksMenu.isPressed() && !com.github.canisartorus.prospectorjournal.ConfigHandler.bookOnly && 
				! (net.minecraft.client.Minecraft.getMinecraft().currentScreen instanceof com.github.canisartorus.prospectorjournal.GuiMain) ) {
			net.minecraft.client.Minecraft.getMinecraft().displayGuiScreen(new com.github.canisartorus.prospectorjournal.GuiMain());
		}
	}
}