package com.canisartorus.prospectorjournal.lib;

public class GeoTag {
	public final short dim, ore;
	public int x, z; //, cx, cz;
	public boolean sample = true;
	
	public GeoTag(int ore, int dim, int x, int z, boolean flower) {
		this.dim = (short) dim;
		this.x = x;
		this.z = z;
		this.ore = (short) ore;
		this.sample = flower;
//		this.cx = x / 16;
//		this.cz = z / 16;
	}
	
	public int cx() {return x / 16;}
	public int cz() {return z / 16;}
}
