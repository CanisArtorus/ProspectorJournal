package com.github.canisartorus.prospectorjournal.lib;

//import java.lang.reflect.InvocationTargetException;

//import com.github.canisartorus.prospectorjournal.compat.IEHandler;

//import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;

public class VoidMine extends MineralMine {
//	protected final ExcavatorHandler.MineralMix oreSet;
//	public int multiple;
	protected final String oreName;
//	static java.lang.reflect.Constructor<VoidMine> factory = VoidMine.VMProxy.class.getConstructor(Short.class, Integer.class, Integer.class, String.class);
	
//	protected VoidMine(int dim, int x, int z, ExcavatorHandler.MineralWorldInfo voidSample) {
//		super((short)dim, x, z);
//		if( voidSample.mineralOverride != null) 
//			oreSet = voidSample.mineralOverride;
//		else
//			oreSet = voidSample.mineral;
////		this.multiple = voidSample.depletion;
//		oreName = oreSet.name;
//	}
	
//	protected VoidMine(int dim, int x, int z, ExcavatorHandler.MineralMix vSample) {
//		super((short)dim, x, z);
//		oreSet = vSample;
//		oreName = oreSet == null ? IEHandler.DEPLETED : oreSet.name;
//	}
	public VoidMine(short dim, int x, int z, String aName) {
		super(dim, x, z);
		oreName = aName;
	}
	
//	public static VoidMine make(int mDim, int x, int z, String mName) {
//		return new VoidMine(mDim, x, z, IEHandler.getByName(mName));
//		try {
//			return factory.newInstance(mDim, x, z, mName);
//		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
//				| InvocationTargetException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}

	public int getFraction(short material) { 
		int f = IEDwarf.singOf(oreName).getOrDefault(material, 0);
		return f == 0 ? 0 : Integer.max(f / 1000 , 1);
//		return 0;
		// allows data to be read from json even when IE is uninstalled.
	}

	public boolean isValid() {
//		return oreSet != null;
		return ! oreName.isEmpty();
	}
	
	public String getOreName() {	return oreName;	}
	

//	public static class VMProxy extends VoidMine {
//		public VMProxy(short dim, int x, int z) {
//			super(dim, x, z, "");
//		}
//	}

	
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
