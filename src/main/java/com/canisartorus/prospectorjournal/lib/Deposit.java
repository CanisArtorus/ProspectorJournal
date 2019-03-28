package com.canisartorus.prospectorjournal.lib;

// @Author Alexander James

import com.canisartorus.prospectorjournal.lib.Dwarf;

import java.util.Comparator;

@cpw.mods.fml.relauncher.SideOnly(cpw.mods.fml.relauncher.Side.CLIENT)
public class Deposit extends RockMatter {
	public int dist;

	public Deposit(RockMatter datum, float atX, float atZ) {
		super(datum.ore, datum.dim, datum.x, datum.y, datum.z, datum.sample);
		this.dist = (int) Math.round( (datum.x - atX)*(datum.x - atX) + (datum.z - atZ)*(datum.z - atZ) );
		// this.exhausted = datum.exhausted;
	}

	public static Comparator<Deposit> getCloseComparator() {
		return new Comparator<Deposit>() {
			@Override
			public int compare(Deposit o1, Deposit o2) {
				// if(o1.exhausted && !o2.exhausted) {
				if(o1.multiple == 0 && o2.multiple > 0) {
					return 50000;
				// } else if(o2.exhausted && !o1.exhausted) {
				} else if( o1.multiple > 0 && o2.multiple == 0) {
					return -50000;
				} else {
					return (o1.dist - o2.dist);
				}
			}
		};
	}

	public static Comparator<Deposit> getQualityComparator(final int material) {
		// check that this shim method works.
		return new Comparator<Deposit>() {
			@Override
			public int compare(Deposit o1, Deposit o2) {
				// if(o1.exhausted && !o2.exhausted) {
				if( o1.multiple == 0 && o2.multiple > 0) {
					return ( 0 - Dwarf.getFraction(material, o2.ore) );
				// } else if(o2.exhausted && !o1.exhausted) {
				} else if( o1.multiple > 0 && o2.multiple == 0) {
					return Dwarf.getFraction(material, o1.ore);
				} else {
					return ( Dwarf.getFraction(material, o1.ore) - Dwarf.getFraction(material, o2.ore) ) ;
				}
			}
		};
	}
}