package com.github.canisartorus.prospectorjournal.lib;

//import java.util.Comparator;

public class GeoTag extends MineralMine {
	public boolean sample = true;
	public final short ore;
	
	public GeoTag(int ore, int dim, int x, int z, boolean flower) {
		super((short)dim, x, z);
		this.ore = (short) ore;
		this.x = x;
		this.z = z;
		this.sample = flower;
	}
	
//	public static class Display extends MineralMine.Display{
//		public final boolean sample;
//		public final short ore;
//		
//		public Display(GeoTag datum, int atX, int atY){
//			super(datum, atX, atY);
//			this.ore = datum.ore;
//			this.sample = datum.sample;
//		}
//		
//		public Comparator<GeoTag.Display> getQualityComparator(final short material) {
//			return new Comparator<GeoTag.Display>() {
//				@Override
//				public int compare(GeoTag.Display o1, GeoTag.Display o2) {
//					if(o1.dead && !o2.dead) {
//						return 0 - Dwarf.getFraction(material, o2.ore);
//					} else if (o2.dead && ! o1.dead) {
//						return Dwarf.getFraction(material, o1.ore);
//					} else
//						return Integer.compare( Dwarf.getFraction(material, o1.ore) , Dwarf.getFraction(material, o2.ore) ) ;
//				}
//			};
//		}
//		
//	}
	
}
