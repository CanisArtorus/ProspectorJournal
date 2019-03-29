package com.canisartorus.prospectorjournal.trans;

import java.util.Comparator;

import com.canisartorus.prospectorjournal.lib.Dwarf;
import com.canisartorus.prospectorjournal.lib.GeoTag;

@cpw.mods.fml.relauncher.SideOnly(cpw.mods.fml.relauncher.Side.CLIENT)
public class DeepShaft extends GeoTag  {
	public final int dist;

	public DeepShaft(GeoTag datum, float atX, float atZ) {
		super(datum.ore, datum.dim, datum.x, datum.z, datum.sample);
		this.dist = (int) Math.round( (datum.x - atX)*(datum.x - atX) + (datum.z - atZ)*(datum.z - atZ) );
	}

	public static Comparator<DeepShaft> getCloseComparator() {
		return new Comparator<DeepShaft>() {
			@Override
			public int compare(DeepShaft o1, DeepShaft o2) {
				return Integer.compare(o2.dist , o1.dist);
			}
		};
	}

	public static Comparator<DeepShaft> getQualityComparator(final short material) {
		return new Comparator<DeepShaft>() {
			@Override
			public int compare(DeepShaft o1, DeepShaft o2) {
				return Integer.compare( Dwarf.getFraction(material, o1.ore) , Dwarf.getFraction(material, o2.ore) ) ;
			}
		};
	}
}
