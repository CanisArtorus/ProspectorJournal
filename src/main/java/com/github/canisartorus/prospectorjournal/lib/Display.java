package com.github.canisartorus.prospectorjournal.lib;

import java.util.Comparator;

public class Display<T extends MineralMine> {
	public final int dist;
	public final T datum;

	public Display(T datum, int atX, int atZ){
//		super(datum.dim, datum.x, datum.z);
		this.dist = (int)  Math.round( Math.sqrt( (datum.x - atX)*(datum.x - atX) + (datum.z - atZ)*(datum.z - atZ) ) );
//		this.dead = datum.dead;
		this.datum = datum;
	}

	public Comparator<Display<T>> getCloseComparator() {
		return new Comparator<Display<T>>() {
			@Override
			public int compare(Display<T> o1, Display<T> o2) {
				if(o1.datum.dead && ! o2.datum.dead) {
					return 50000;
				} else if (o2.datum.dead && ! o1.datum.dead) {
					return -50000;
				} else
					return Integer.compare(o1.dist , o2.dist);
			}
		};
	}

	public Comparator<Display<T>> getQualityComparator(final short material) {
		return new Comparator<Display<T>>() {
			@Override
			public int compare(Display<T> o1, Display<T> o2) {
				if(o1.datum.dead && !o2.datum.dead) {
					return o2.datum.getFraction(material);
//					return Dwarf.getFractionIn(o2.datum, material);
				} else if (o2.datum.dead && ! o1.datum.dead) {
//					return 0 - Dwarf.getFractionIn(o1.datum, material);
					return 0 - o1.datum.getFraction(material);
				} else
					return Integer.compare( o2.datum.getFraction(material) , o1.datum.getFraction(material) ) ;
			}
		};
	}

	
}