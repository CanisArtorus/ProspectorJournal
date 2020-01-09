package com.github.canisartorus.prospectorjournal.gui;

import gregapi.gui.ContainerCommonChest;
import gregapi.tileentity.ITileEntityInventoryGUI;
import net.minecraft.entity.player.InventoryPlayer;

public class SampleCrateContainerCommon extends ContainerCommonChest {

	public SampleCrateContainerCommon(InventoryPlayer aInventoryPlayer, ITileEntityInventoryGUI aTileEntity) {
		super(aInventoryPlayer, aTileEntity);
		// TODO Auto-generated constructor stub
	}

	public SampleCrateContainerCommon(InventoryPlayer aInventoryPlayer, ITileEntityInventoryGUI aTileEntity,
			int aOffset, int aSlotCount) {
		super(aInventoryPlayer, aTileEntity, aOffset, aSlotCount);
		// TODO Auto-generated constructor stub
	}

}
