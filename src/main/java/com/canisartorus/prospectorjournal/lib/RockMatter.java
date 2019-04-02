package com.canisartorus.prospectorjournal.lib;

// @Author Alexander James

public class RockMatter extends GeoTag {
	public short y, multiple = 1;
	
	public RockMatter(int ore, int dim, int x, int y, int z, boolean rock) {
		super(ore, dim, x, z, rock);
		this.y = (short) y;
	}
	
	public class Display extends GeoTag.Display {
		public final short y, multiple;
		public Display(RockMatter datum, int atX, int atZ) {
			super(datum, atX, atZ);
			this.y = datum.y;
			this.multiple = datum.multiple;
		}
	}
}