package com.github.canisartorus.prospectorjournal.lib;

// @Author Alexander James

public class RockMatter extends GeoTag {
	public short y, multiple = 1;
	
	public RockMatter(int ore, int dim, int x, int y, int z, boolean rock) {
		super(ore, dim, x, z, rock);
		this.y = (short) y;
	}
	
	@Override
	public int getFraction(short material) {
		return Dwarf.getFractionIn(ore, material);
	}
//	public Comparator<Display<? extends MineralMine>> getQualityComparator(final short material) {
//		return new Comparator<Display<? extends MineralMine>>() {
//			@Override
//			public int compare(Display<? extends MineralMine> o1, Display<? extends MineralMine> o2) {
//				if((o1.datum instanceof GeoTag) && (o2.datum instanceof GeoTag)) {
//					final short m1 = ((GeoTag) o1.datum).ore;
//					final short m2 = ((GeoTag) o2.datum).ore;
//					if(o1.datum.dead && !o2.datum.dead) {
//						return Dwarf.getFractionIn(m2, material);
//					} else if (o2.datum.dead && ! o1.datum.dead) {
//						return 0 - Dwarf.getFractionIn(m1, material);
//					} else
//						return Integer.compare( Dwarf.getFractionIn(m2, material) , Dwarf.getFractionIn(m1, material) ) ;
//				}
//				return 0;
//			}
//		};
//	}
//	public class Display extends GeoTag.Display {
//		public final short y, multiple;
//		public Display(RockMatter datum, int atX, int atZ) {
//			super(datum, atX, atZ);
//			this.y = datum.y;
//			this.multiple = datum.multiple;
//		}
//	}
}