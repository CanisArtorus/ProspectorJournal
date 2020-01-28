package com.github.canisartorus.prospectorjournal.compat;

import com.github.canisartorus.prospectorjournal.ProspectorJournal;
import com.github.canisartorus.prospectorjournal.lib.Utils;
import com.github.canisartorus.prospectorjournal.lib.Utils.ChatString;
import com.github.canisartorus.prospectorjournal.network.PacketVoidVein;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler.MineralMix;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityExcavator;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntitySampleDrill;
import gregapi.data.MD;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public class IEHandler {

	public static final String DEPLETED = "";
	
	public static MineralMix getByName(String sMineral) {
		if (sMineral.equals(DEPLETED))
			return null;
		for (MineralMix variant : ExcavatorHandler.mineralList.keySet()) {
			if(variant.name.equalsIgnoreCase(sMineral))
				return variant;
		}
		System.out.println(ProspectorJournal.MOD_NAME+"[WARNING] Failed to identify MineralMix: "+sMineral);
		return null;
	}

//	@cpw.mods.fml.relauncher.SideOnly(cpw.mods.fml.relauncher.Side.SERVER)
	public static boolean takeExcavatorSample(World aWorld, int x, int y, int z, EntityPlayer aPlayer, final net.minecraft.tileentity.TileEntity i) {
		if( ! MD.IE.mLoaded || aWorld.isRemote )	return false;
		if(i instanceof TileEntityExcavator) {
			TileEntityExcavator ti = ((TileEntityExcavator) i).master();
			if(ti == null) ti = (TileEntityExcavator) i;
			if(!ti.formed) return false;
			final int[] wheelAxis = {ti.xCoord+(ti.facing==5?-4:ti.facing==4?4:0),ti.yCoord,ti.zCoord+(ti.facing==3?-4:ti.facing==2?4:0)};
			// server-side-only info, again
			final ExcavatorHandler.MineralWorldInfo mineral = ExcavatorHandler.getMineralWorldInfo(aWorld, wheelAxis[0]>>4, wheelAxis[2]>>4);
			takeSampleServer(aWorld, aPlayer, mineral, x>>4, z>>4);
			return true;
		} else if(i instanceof TileEntitySampleDrill) {
			TileEntitySampleDrill ti = (TileEntitySampleDrill) i;
			// Only the core has progress
			if(ti.pos != 0) {
				net.minecraft.tileentity.TileEntity t2 = aWorld.getTileEntity(x, y-ti.pos, z);
				if(t2 instanceof TileEntitySampleDrill) {
					ti = (TileEntitySampleDrill) t2;
				} else {
//					System.out.println
					Utils.chatAt(aPlayer, ChatString.DRILL_FAIL);// "Incorrect search for sample drill core. Report this error!");
					return false;
				}
			}
			if(ti.isSamplingFinished()) {
//				final ExcavatorHandler.MineralMix mineral = ExcavatorHandler.getRandomMineral(aWorld, (ti.xCoord>>4), (ti.zCoord>>4));
				final ExcavatorHandler.MineralWorldInfo info = ExcavatorHandler.getMineralWorldInfo(aWorld, (ti.xCoord>>4), (ti.zCoord>>4));
				takeSampleServer(aWorld, aPlayer, info, x>>4, z>>4);
				return true;
			}
			Utils.chatAt(aPlayer, ChatString.CORE_WAIT);// "Wait for the core sample to be extracted.");
			return true;
		}
		return false;
	}

	//NB: always server-side
	protected static void takeSampleServer(final World aWorld, final EntityPlayer aPlayer, final ExcavatorHandler.MineralWorldInfo mInfo, int cx, int cz) {
		if(mInfo == null) return;
		ExcavatorHandler.MineralMix mineral = mInfo.mineralOverride;
		if(mineral == null) mineral = mInfo.mineral;
		if(mineral == null) return;
		PacketVoidVein msg;
		if(ExcavatorHandler.mineralVeinCapacity != -1 && mInfo.depletion > ExcavatorHandler.mineralVeinCapacity) {
			msg = new PacketVoidVein(cx/16,cz/16, DEPLETED);
		} else
			msg = new PacketVoidVein(cx/16,cz/16, mineral.name);
		
		Utils.NW_PJ.sendToPlayer(msg, (EntityPlayerMP) aPlayer);
	}

//	public class VoidMineActive extends VoidMine {
//		protected final ExcavatorHandler.MineralMix oreSet;
////		public final String oreName;
//
//		public VoidMineActive(int dim, int x, int z, ExcavatorHandler.MineralMix vSample) {
//			super((short)dim, x, z, vSample == null ? DEPLETED : vSample.name);
//			oreSet = vSample;
////			oreName = oreSet == null ? IEHandler.DEPLETED : oreSet.name;
//		}
//		
//		@Override
//		public int getFraction(short material) {
//			int f = IEDwarf.singOf(oreSet).getOrDefault(material, 0);
//			return f == 0 ? 0 : Integer.max(f / 1000 , 1);
//		}
//
//		@Override
//		public boolean isValid() {
//			return oreSet != null;
////			return ! oreName.isEmpty();
//		}
//	}

	
}
