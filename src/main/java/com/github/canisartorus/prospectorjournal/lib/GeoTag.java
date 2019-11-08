package com.github.canisartorus.prospectorjournal.lib;

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
	
	public int getFraction(short material) {
		int f = Dwarf.singOf(ore).getOrDefault(material, 0);
		return f == 0 ? 0 : Integer.max(f / Dwarf.weightsMeasure[0] /32 , 1);
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
//		public Comparator<Display<? extends MineralMine>> getQualityComparator(final short material) {
//			return new Comparator<Display<? extends MineralMine>>() {
//				@Override
//				public int compare(Display<? extends MineralMine> o1, Display<? extends MineralMine> o2) {
//					if((o1.datum instanceof GeoTag) && (o2.datum instanceof GeoTag)) {
//						final int m1 = Dwarf.singOf(((GeoTag) o1.datum).ore).getOrDefault(material, 0);
//						final int m2 = Dwarf.singOf(((GeoTag) o2.datum).ore).getOrDefault(material, 0);
//						if(o1.datum.dead && !o2.datum.dead) {
//							return m2;
//						} else if (o2.datum.dead && ! o1.datum.dead) {
//							return - m1;
//						} else
//							return Integer.compare( m2 , m1 ) ;
//					}
//					return 0;
//				}
//			};
//		}
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
