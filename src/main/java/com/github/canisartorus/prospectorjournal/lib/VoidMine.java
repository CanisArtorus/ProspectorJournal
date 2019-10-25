package com.github.canisartorus.prospectorjournal.lib;

//import java.util.Comparator;

//import com.github.canisartorus.prospectorjournal.compat.IEHandler;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;

public class VoidMine extends MineralMine {
	public final ExcavatorHandler.MineralMix oreSet;
//	public int multiple;
	
	public VoidMine(int dim, int x, int z, ExcavatorHandler.MineralWorldInfo voidSample) {
		super((short)dim, x, z);
		if( voidSample.mineralOverride != null) 
			oreSet = voidSample.mineralOverride;
		else
			oreSet = voidSample.mineral;
//		this.multiple = voidSample.depletion;
	}
	
	public VoidMine(int dim, int x, int z, ExcavatorHandler.MineralMix vSample) {
		super((short)dim, x, z);
		oreSet = vSample;
	}
	
//	public static class Display extends MineralMine.Display {
//		public final ExcavatorHandler.MineralMix oreSet;
//		public final int multiple;
//		
//		public Display(VoidMine datum, int atX, int atZ) {
//			super(datum, atX, atZ);
//			this.oreSet = datum.oreSet;
//			this.multiple = datum.multiple;
//		}
//		
//		public Comparator<VoidMine.Display> getQualityComparator(final short material) {
//			return new Comparator<VoidMine.Display>() {
//				@Override
//				public int compare(VoidMine.Display o1, VoidMine.Display o2) {
//					if(o1.dead && !o2.dead) {
//						return 0 - IEHandler.Dwarf.getFraction(o2.oreSet, material);
//					} else if (o2.dead && ! o1.dead) {
//						return IEHandler.Dwarf.getFraction(o1.oreSet, material);
//					} else
//						return Integer.compare( IEHandler.Dwarf.getFraction(o1.oreSet, material) , IEHandler.Dwarf.getFraction(o2.oreSet, material) ) ;
//				}
//			};
//		}
//
//	}

}
