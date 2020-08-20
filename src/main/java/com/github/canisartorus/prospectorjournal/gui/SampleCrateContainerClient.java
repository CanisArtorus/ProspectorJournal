package com.github.canisartorus.prospectorjournal.gui;

import gregapi.gui.ContainerClientChest;
import gregapi.gui.ContainerCommonChest;
import gregapi.tileentity.ITileEntityInventoryGUI;
import net.minecraft.entity.player.InventoryPlayer;

public class SampleCrateContainerClient extends ContainerClientChest {

	public SampleCrateContainerClient(ContainerCommonChest aContainer) {
		super(aContainer);
		// TODO Auto-generated constructor stub
	}

	public SampleCrateContainerClient(InventoryPlayer aInventoryPlayer, ITileEntityInventoryGUI aTileEntity) {
		super(aInventoryPlayer, aTileEntity);
		// TODO Auto-generated constructor stub
	}

}
