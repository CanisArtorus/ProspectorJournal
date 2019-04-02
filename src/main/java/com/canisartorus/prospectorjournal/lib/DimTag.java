package com.canisartorus.prospectorjournal.lib;

// @Author Dyonovan

public class DimTag {
	public final int dimID;
	public final String dimName;
	
	public DimTag(int dimID, String dimName) {
		this.dimID = dimID;
		this.dimName = dimName;
	}
	
	public static final java.util.Comparator<DimTag> astralOrder = new java.util.Comparator<DimTag>() {
		@Override
		public int compare(DimTag o1, DimTag o2) {
			return Integer.compare(o1.dimID, o2.dimID);
		}
	};
}