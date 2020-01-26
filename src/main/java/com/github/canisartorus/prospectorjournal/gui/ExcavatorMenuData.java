package com.github.canisartorus.prospectorjournal.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.github.canisartorus.prospectorjournal.ProspectorJournal;
import com.github.canisartorus.prospectorjournal.lib.Display;
import com.github.canisartorus.prospectorjournal.lib.IEDwarf;
import com.github.canisartorus.prospectorjournal.lib.Utils;
import com.github.canisartorus.prospectorjournal.lib.VoidMine;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;

public final class ExcavatorMenuData extends AbstractMenuData {

	private List<Display<VoidMine>> zonesIE = new ArrayList<>();
	
	public ExcavatorMenuData(){
		super(StatCollector.translateToLocal("btn.excavator.name"),
				Utils.EXCAVATOR
				);
		obviousEnd = true;
	}
	
	@Override
	public void forget() {	zonesIE.clear();	}

	@Override
	public int sortBy(short sortBy, short dimID, int aX, int aZ) {
		zonesIE.clear();
		for(VoidMine r : ProspectorJournal.voidVeins) {
			if(dimID != r.dim) continue;
			if(sortBy == Utils.DISTANCE || r.getFraction(sortBy) != 0)
				zonesIE.add(new Display<VoidMine>(r, aX, aZ));
		}
		if(zonesIE.isEmpty()) {
//			zonesIE.add(new Display<VoidMine>(new VoidMine(dimID, aX, aZ, new blusunrize.immersiveengineering.api.tool.ExcavatorHandler.MineralWorldInfo()), aX, aZ));
			zonesIE.add(new Display<VoidMine>(new VoidMine( dimID, aX, aZ, ""), aX, aZ));
			return 1;
		}
		Collections.sort(zonesIE, sortBy == Utils.DISTANCE ? zonesIE.get(0).getCloseComparator() : zonesIE.get(0).getQualityComparator(sortBy));
		return zonesIE.size() ;
	}
	
	@Override
	boolean exhaust(int iEntry) {
		final VoidMine q = zonesIE.get(iEntry).datum;
		for(VoidMine e : ProspectorJournal.voidVeins) {
			if(e.dim == q.dim && e.x == q.x && e.z == q.z && 
//					e.oreSet == q.oreSet) {
					e.getOreName().equalsIgnoreCase(q.getOreName()) ) {
				if(ProspectorJournal.doGui && ProspectorJournal.xMarker == q.x && ProspectorJournal.zMarker == q.z) {
					ProspectorJournal.doGui = false;
					ProspectorJournal.yMarker = -1;
				}
//				e.dead = ! e.dead;
				ProspectorJournal.voidVeins.remove(e);
				Utils.writeJson(Utils.IE_VOID_FILE);
//				sorted(Utils.EXCAVATOR, lastSort);
				zonesIE.remove(iEntry);
				return true;
			}
		}
		return false;
	}

	@Override
	public void trackCoords(int i) {
		ProspectorJournal.xMarker = zonesIE.get(i).datum.x;
		ProspectorJournal.yMarker = 80;
		ProspectorJournal.zMarker = zonesIE.get(i).datum.z;
	}

	@Override
	void drawDataRow(int aEntry, int aStart, int l, GuiScreen aMenu, FontRenderer FRO, short lastSort) {
		Display<VoidMine> p = zonesIE.get(aEntry);
		String ts;
		int colour;
		if(ProspectorJournal.xMarker == p.datum.x && ProspectorJournal.zMarker == p.datum.z ) {
			colour = Utils.GREEN;
		} else colour = Utils.WHITE;
		
		ts = StatCollector.translateToLocal("str.any.name");
   		FRO.drawString(ts, aStart + (83 -(FRO.getStringWidth(ts)/2)), l, colour);
//   		ts = Utils.approx(p.datum.multiple) + StatCollector.translateToLocal("sym.x.name");
   		if (gregapi.data.MD.IE.mLoaded) {
   			ts = Utils.approx(blusunrize.immersiveengineering.api.tool.ExcavatorHandler.mineralVeinCapacity) + StatCollector.translateToLocal("sym.x.name");
   		} else {
   			ts = Utils.approx(Integer.MAX_VALUE) + StatCollector.translateToLocal("sym.x.name");
   		}
   		FRO.drawString(ts, aStart + (145 -(FRO.getStringWidth(ts)/2)), l, colour);
		ts = lastSort == Utils.DISTANCE ? p.datum.isValid() ? p.datum.getOreName() : "Nil" : StatCollector.translateToLocal("str.value.name") + " " + Utils.approx(IEDwarf.singOf(p.datum.getOreName()).get(lastSort));
		FRO.drawString(ts, aStart + 190, l, colour);
		aMenu.drawTexturedModelRectFromIcon(aStart + 172, l, IEDwarf.getIcon(p.datum.getOreName()), 16, 16);

    	ts = Integer.toString(p.dist);
    	FRO.drawString(ts, aStart + (11 -(FRO.getStringWidth(ts)/2)), l, colour);
    	ts = Integer.toString(p.datum.x);
    	FRO.drawString(ts, aStart + (52 -(FRO.getStringWidth(ts)/2)), l, colour);
    	ts = Integer.toString(p.datum.z);
    	FRO.drawString(ts, aStart + (112 -(FRO.getStringWidth(ts)/2)), l, colour);
		
	}

	@Override
	Map<Short, Integer> getSong(int aEntry) {
		return IEDwarf.singOf(zonesIE.get(aEntry).datum.getOreName());
	}

	@Override
	short getMajor(int aEntry) {
		return IEDwarf.getMajor(zonesIE.get(aEntry).datum.getOreName());
	}

}
