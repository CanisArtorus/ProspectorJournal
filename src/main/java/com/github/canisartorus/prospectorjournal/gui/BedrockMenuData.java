package com.github.canisartorus.prospectorjournal.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.github.canisartorus.prospectorjournal.ProspectorJournal;
import com.github.canisartorus.prospectorjournal.lib.Display;
import com.github.canisartorus.prospectorjournal.lib.Dwarf;
import com.github.canisartorus.prospectorjournal.lib.GeoTag;
import com.github.canisartorus.prospectorjournal.lib.Utils;

import gregapi.data.OP;
import gregapi.old.Textures;
import gregapi.oredict.OreDictMaterial;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;

public class BedrockMenuData extends AbstractMenuData {

	private List<Display<GeoTag>> rockSpots = new ArrayList<>();

	public BedrockMenuData() {
		super(StatCollector.translateToLocal("btn.bedrock.name"), 
				Utils.BEDROCK 
				);
	}

	@Override
	public void forget() {	rockSpots.clear();	}

	@Override
	public int sortBy(short sortBy, short dimID, int aX, int aZ) {
		rockSpots.clear();
		for(GeoTag r : ProspectorJournal.bedrockFault) {
			if(dimID != r.dim) continue;
			if(sortBy == Utils.DISTANCE || r.ore == 0 || Dwarf.singOf(r.ore).containsKey(sortBy) )
				rockSpots.add(new Display<GeoTag>(r, aX, aZ));
		}
		if(rockSpots.isEmpty()) {
//			rockSpots.add(new Display<GeoTag>(new GeoTag(0, dimID, aX, aZ, true), aX, aZ));
//			return 1;
			return 0;
		}
		Collections.sort(rockSpots, sortBy == Utils.DISTANCE ? rockSpots.get(0).getCloseComparator() : rockSpots.get(0).getQualityComparator(sortBy));
		return rockSpots.size();
	}

	@Override
	boolean exhaust(int iEntry) {
		final GeoTag p = rockSpots.get(iEntry).datum;
		for(GeoTag e : ProspectorJournal.bedrockFault) {
			if(e.dim == p.dim && e.x == p.x && e.z == p.z && e.ore == p.ore) {
				if(ProspectorJournal.doGui && ProspectorJournal.xMarker == p.x && ProspectorJournal.yMarker <= 5 && ProspectorJournal.zMarker == p.z) {
					ProspectorJournal.doGui = false;
					ProspectorJournal.yMarker = -1;
				}
				e.dead = ! e.dead;
				Utils.writeJson(Utils.GT_BED_FILE);
//				sorted(Utils.BEDROCK, lastSort);
				if(e.dead ) {
					rockSpots.remove(iEntry);
					return true;
				}
				return false;
			}
		}
		return false;
	}

	@Override
	public void trackCoords(int i) {
		ProspectorJournal.xMarker = rockSpots.get(i).datum.x;
		ProspectorJournal.yMarker = rockSpots.get(i).datum.sample ? 4 : 1;
		ProspectorJournal.zMarker = rockSpots.get(i).datum.z;
	}

	@Override
	void drawDataRow(int aEntry, int aStart, int l, GuiScreen aMenu, FontRenderer FRO, short lastSort) {
		Display<GeoTag> q = rockSpots.get(aEntry);
		String ts;
		final int colour;
		if(ProspectorJournal.xMarker == q.datum.x && ProspectorJournal.zMarker == q.datum.z && ProspectorJournal.yMarker < 5) {
			colour = Utils.GREEN;
		} else if(q.datum.dead)
			colour = Utils.GRAY;
		else colour = Utils.WHITE;
		
		ts = q.datum.sample ? StatCollector.translateToLocal("str.floor.name") : "0";
   		FRO.drawString(ts, aStart + (83 -(FRO.getStringWidth(ts)/2)), l, colour);
   		ts = StatCollector.translateToLocal("sym.inf.name");
   		FRO.drawString(ts, aStart + (145 -(FRO.getStringWidth(ts)/2)), l, colour);
		final short tOre = q.datum.ore;
		ts = lastSort == Utils.DISTANCE ? Dwarf.name(tOre) : StatCollector.translateToLocal("str.value.name") + " " + Utils.approx(Dwarf.singOf(tOre).get(lastSort));
		FRO.drawString(ts, aStart + 190, l, colour);
		if(tOre == 0)	{	;
		} else if(lastSort == Utils.DISTANCE || lastSort == tOre) {
			try {
			aMenu.drawTexturedModelRectFromIcon(aStart + 172, l, OP.dust.mat(OreDictMaterial.MATERIAL_ARRAY[tOre], 16).getIconIndex(), 16, 16);
			} catch (Throwable e) {
				e.printStackTrace();
				aMenu.drawTexturedModelRectFromIcon(aStart + 172, l, Textures.ItemIcons.RENDERING_ERROR.getIcon(0), 16, 16);
			}
		} else {
			aMenu.drawTexturedModelRectFromIcon(aStart + 172, l, OP.crushedPurified.mat(OreDictMaterial.MATERIAL_ARRAY[tOre], 1).getIconIndex(), 16, 16);
		}

    	ts = Integer.toString(q.dist);
    	FRO.drawString(ts, aStart + (11 -(FRO.getStringWidth(ts)/2)), l, colour);
    	ts = Integer.toString(q.datum.x);
    	FRO.drawString(ts, aStart + (52 -(FRO.getStringWidth(ts)/2)), l, colour);
    	ts = Integer.toString(q.datum.z);
    	FRO.drawString(ts, aStart + (112 -(FRO.getStringWidth(ts)/2)), l, colour);
		
	}

	@Override
	Map<Short, Integer> getSong(int aEntry) {
		return Dwarf.singOf(rockSpots.get(aEntry).datum.ore);
	}

	@Override
	short getMajor(int aEntry) {
		return rockSpots.get(aEntry).datum.ore;
	}

}
