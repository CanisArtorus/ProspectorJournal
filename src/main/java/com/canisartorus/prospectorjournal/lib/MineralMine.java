package com.canisartorus.prospectorjournal.lib;

import java.util.Comparator;

public abstract class MineralMine {
	public final short dim;
	public int x, z;
	public boolean dead = false;

	public int cx() {return x / 16;}
	public int cz() {return z / 16;}

	public MineralMine(short dim, int x, int z) {
		this.dim = dim;
		this.x = x;
		this.z = z;
	}

	abstract public static class Display extends MineralMine {
		public final int dist;

		public Display(MineralMine datum, int atX, int atZ){
			super(datum.dim, datum.x, datum.z);
			this.dist = (int) Math.round( (datum.x - atX)*(datum.x - atX) + (datum.z - atZ)*(datum.z - atZ) );
			this.dead = datum.dead;
		}

		public Comparator<Display> getCloseComparator() {
			return new Comparator<Display>() {
				@Override
				public int compare(Display o1, Display o2) {
					if(o1.dead && ! o2.dead) {
						return -50000;
					} else if (o2.dead && ! o1.dead) {
						return 50000;
					} else
						return Integer.compare(o2.dist , o1.dist);
				}
			};
		}
	
//		public abstract Comparator<Display> getQualityComparator(final short material);
//		throws Exception{
//			throw new Exception("raw abstract static method used: MineralMine.getQualityComparator(short)");
////			return getCloseComparator();
//		}
		
	}
}
