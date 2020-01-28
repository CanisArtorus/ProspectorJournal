package com.github.canisartorus.prospectorjournal.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.github.canisartorus.prospectorjournal.ProspectorJournal;
import com.github.canisartorus.prospectorjournal.lib.Display;
import com.github.canisartorus.prospectorjournal.lib.Dwarf;
import com.github.canisartorus.prospectorjournal.lib.RockMatter;
import com.github.canisartorus.prospectorjournal.lib.Utils;

import gregapi.data.OP;
import gregapi.old.Textures;
import gregapi.oredict.OreDictMaterial;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;

public class OreMenuData extends AbstractMenuData {

	private List<Display<RockMatter>> oreVeins = new ArrayList<>();

	public OreMenuData() {
		super(StatCollector.translateToLocal("btn.oreveins.name"),
				Utils.ORE_VEIN
				);
	}

	@Override
	public void forget() {	oreVeins.clear();	}

	@Override
	public int sortBy(short sortBy, short dimID, int aX, int aZ) {
		oreVeins.clear();
		for(RockMatter r : ProspectorJournal.rockSurvey) {
			if(dimID != r.dim) continue;
			if(sortBy == Utils.DISTANCE || r.ore == 0 || Dwarf.getFractionIn(r.ore, sortBy) != 0)
				oreVeins.add(new Display<RockMatter>(r, aX, aZ));
		}
		if(oreVeins.isEmpty()) {
//			oreVeins.add(new Display<RockMatter>(new RockMatter(0, dimID, aX, 255, aZ, true), aX, aZ));
//			return 1;
			return 0;
		}
		Collections.sort(oreVeins, sortBy == Utils.DISTANCE ? oreVeins.get(0).getCloseComparator() : oreVeins.get(0).getQualityComparator(sortBy));
		return oreVeins.size() ;
	}

	@Override
	boolean exhaust(int iEntry) {
		final RockMatter o = oreVeins.get(iEntry).datum;
		for(RockMatter e : ProspectorJournal.rockSurvey) {
			if(e.dim == o.dim && e.x == o.x && e.y == o.y && e.z == o.z && e.ore == o.ore) {
				if(ProspectorJournal.doGui && ProspectorJournal.xMarker == o.x && ProspectorJournal.yMarker == o.y && ProspectorJournal.zMarker == o.z) {
					ProspectorJournal.doGui = false;
					ProspectorJournal.yMarker = -1;
				}
				if(e.sample) {
					ProspectorJournal.rockSurvey.remove(e);
					Utils.writeJson(Utils.GT_FILE);
//					sorted(Utils.ORE_VEIN, lastSort);
					oreVeins.remove(iEntry);
					return true;
				}
				e.dead = ! e.dead;
				e.multiple = 0;
				Utils.writeJson(Utils.GT_FILE);
//				sorted(Utils.ORE_VEIN, lastSort);
				if( e.dead) {
					oreVeins.remove(iEntry);
					return true;
				}
				return false;
			}
		}
		return false;
	}

	@Override
	public void trackCoords(int i) {
		ProspectorJournal.xMarker = oreVeins.get(i).datum.x;
		ProspectorJournal.yMarker = oreVeins.get(i).datum.y;
		ProspectorJournal.zMarker = oreVeins.get(i).datum.z;
	}

	@Override
	void drawDataRow(int aEntry, int aStart, int l, GuiScreen aMenu, FontRenderer FRO, short lastSort) {
		Display<RockMatter> r = oreVeins.get(aEntry);
		final int colour;
		String ts;
		if(ProspectorJournal.xMarker == r.datum.x && ProspectorJournal.zMarker == r.datum.z && ProspectorJournal.yMarker == r.datum.y) {
			colour = Utils.GREEN;
		} else if(r.datum.dead)
			colour = Utils.GRAY;
		else colour = Utils.WHITE;
		
		ts = r.datum.sample ? ("<"+ Integer.toString(r.datum.y)+ "?" ) : Integer.toString(r.datum.y);
   		FRO.drawString(ts, aStart + (83 -(FRO.getStringWidth(ts)/2)), l, colour);
		ts = r.datum.multiple + StatCollector.translateToLocal("sym.x.name");
   		FRO.drawString(ts, aStart + (145 -(FRO.getStringWidth(ts)/2)), l, colour);
		final short tOre = r.datum.ore;
		ts = lastSort == Utils.DISTANCE ? Dwarf.name(tOre) : StatCollector.translateToLocal("str.value.name") + " " + Integer.toString(Dwarf.getFractionIn(tOre, lastSort));
		FRO.drawString(ts, aStart + 190, l, colour);
		if(tOre == 0)	{	;
		} else if(lastSort == Utils.DISTANCE || lastSort == tOre) {
			try {
			aMenu.drawTexturedModelRectFromIcon(aStart + 172, l,  OP.blockDust.mat(OreDictMaterial.MATERIAL_ARRAY[tOre], 16).getIconIndex(), 16, 16);
			} catch (Throwable e) {
				aMenu.drawTexturedModelRectFromIcon(aStart + 172, l, Textures.ItemIcons.RENDERING_ERROR.getIcon(0), 16, 16);
				e.printStackTrace();
			}
		} else {
			aMenu.drawTexturedModelRectFromIcon(aStart + 172, l, OP.crushedPurified.mat(OreDictMaterial.MATERIAL_ARRAY[tOre], 1).getIconIndex(), 16, 16);
		}

    	ts = Integer.toString(r.dist);
    	FRO.drawString(ts, aStart + (11 -(FRO.getStringWidth(ts)/2)), l, colour);
    	ts = Integer.toString(r.datum.x);
    	FRO.drawString(ts, aStart + (52 -(FRO.getStringWidth(ts)/2)), l, colour);
    	ts = Integer.toString(r.datum.z);
    	FRO.drawString(ts, aStart + (112 -(FRO.getStringWidth(ts)/2)), l, colour);

	}

	@Override
	Map<Short, Integer> getSong(int aEntry) {
		return Dwarf.read(oreVeins.get(aEntry).datum.ore).mByBy;
	}

	@Override
	short getMajor(int aEntry) {
		return oreVeins.get(aEntry).datum.ore;
	}

}
