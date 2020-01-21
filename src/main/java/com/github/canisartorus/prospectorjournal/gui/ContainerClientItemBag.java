package com.github.canisartorus.prospectorjournal.gui;

import com.github.canisartorus.prospectorjournal.inventory.SlotGhost;

import gregapi.data.CS;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

@cpw.mods.fml.relauncher.SideOnly(cpw.mods.fml.relauncher.Side.CLIENT)
public class ContainerClientItemBag extends GuiContainer {
	
	public ContainerClientItemBag(Container p_i1072_1_) {
		super(p_i1072_1_);
		if(p_i1072_1_ instanceof IOversizeContainer) {
			xSize = ((IOversizeContainer)p_i1072_1_).getSizeX();
			ySize = ((IOversizeContainer)p_i1072_1_).getSizeY();
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		int l =(width - xSize) /2, t = (height - ySize)/2;
		drawRect( l, t, l+xSize, t+ySize, 0xEEFFDD);	// default is -2130706433
		
		/* @author Vazkii
		 * adapted from vazkii.botania.client.gui.bag.GuiFlowerBag
		 */
		@SuppressWarnings("unchecked")
		java.util.List<Slot> slotList = inventorySlots.inventorySlots;
		for( Slot tSlot : slotList) {
			if( tSlot instanceof SlotGhost) {
				SlotGhost fSlot = (SlotGhost) tSlot;
				if(!fSlot.getHasStack()) {
					int x = guiLeft + fSlot.xDisplayPosition, y = guiTop + fSlot.yDisplayPosition;
					net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
					net.minecraft.client.renderer.entity.RenderItem.getInstance().renderItemIntoGUI(fontRendererObj, mc.renderEngine, fSlot.getEmptyItem(), x, y);
					net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
					fontRendererObj.drawStringWithShadow("0", x+11, y+9, 0xFF6666);
				}
			}
		}
	}

	// From gregapi.gui.ContainerClient
	// By Gregorios Techneticies
	@Override
	public void drawScreen(int aX, int aY, float par3) {
		try {
			super.drawScreen(aX, aY, par3);
//			for (int i = 0; i < inventorySlots.inventorySlots.size(); ++i) {
//				Slot tSlot = (Slot)inventorySlots.inventorySlots.get(i);
//				if (gregapi.util.ST.invalid(tSlot.getStack()) && isMouseOverSlot(tSlot, aX, aY) && tSlot instanceof Slot_Normal) {
//					drawHoveringText(((Slot_Normal)tSlot).getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips), aX, aY, fontRendererObj);
//				}
//			}
		} catch (Throwable e) {
			e.printStackTrace(CS.ERR);
			try {
				net.minecraft.client.renderer.Tessellator.instance.draw();
			} catch (Throwable f) {
				f.printStackTrace(CS.ERR);
			}
		}
	}
	
	protected boolean isMouseOverSlot(Slot aSlot, int aX, int aY) {return func_146978_c(aSlot.xDisplayPosition, aSlot.yDisplayPosition, 16, 16, aX, aY);}
	

}
