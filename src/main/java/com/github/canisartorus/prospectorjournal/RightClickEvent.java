package com.github.canisartorus.prospectorjournal;

/**	@author Alexander James
	@author Dyonovan
**/

import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

//@cpw.mods.fml.relauncher.SideOnly(cpw.mods.fml.relauncher.Side.CLIENT)
public class RightClickEvent {

	@cpw.mods.fml.common.eventhandler.SubscribeEvent
	public void playerRightClick(PlayerInteractEvent event) {

		final World aWorld = event.entityPlayer.worldObj;
		if (event.isCanceled() || //!aWorld.isRemote ||
				event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
			return;
		} else if(com.github.canisartorus.prospectorjournal.ConfigHandler.bookOnly) {
//			if(event.entityPlayer.inventory.getCurrentItem() == null)
//				return;
//
//			ItemStack heldItem = event.entityPlayer.inventory.getCurrentItem();
//			if (!heldItem.getUnlocalizedName().equalsIgnoreCase("ca.prospectorjournal.notebook"))
				return;
		}
//		if(com.github.canisartorus.prospectorjournal.ConfigHandler.debug)
//			System.out.println("Right-Click event");
		JournalBehaviour.lookForSample(aWorld, event.x, event.y, event.z, event.entityPlayer);
	}
}