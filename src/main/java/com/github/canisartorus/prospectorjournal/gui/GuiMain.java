package com.github.canisartorus.prospectorjournal.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.github.canisartorus.prospectorjournal.KeyBindings;
import com.github.canisartorus.prospectorjournal.ProspectorJournal;
//import com.github.canisartorus.prospectorjournal.compat.IEHandler;
import com.github.canisartorus.prospectorjournal.lib.DimTag;
import com.github.canisartorus.prospectorjournal.lib.Dwarf;
import com.github.canisartorus.prospectorjournal.lib.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.DimensionManager;

// @author Alexander James
// @author Dyonovan

@cpw.mods.fml.relauncher.SideOnly(cpw.mods.fml.relauncher.Side.CLIENT)
public class GuiMain extends net.minecraft.client.gui.GuiScreen {
	private static final ResourceLocation smallArrow = new ResourceLocation("prospectorjournal:textures/gui/arrows_small.png");
	protected static final int NUM_ROWS = 10;
	
//	private List<Display<RockMatter>> oreVeins =	new ArrayList<>();
//	private List<Display<GeoTag>> rockSpots =	new ArrayList<>();
//	private List<Display<VoidMine>> zonesIE =	new ArrayList<>();
	
	private int display = 425, start, 
		low = 0, high, max,
		dimID = 0, dimIndex,
		wCol
		;
	private static short lastSort = Utils.DISTANCE;
//	private static byte lastData = Utils.ORE_VEIN;
	private static AbstractMenuData CurrentData;
	private String dimName;
	private SearchBox oSearchBox;
	
	public GuiMain() {
		oSearchBox = new SearchBox(this);
	}
	
	private static void allClear() {
//		oreVeins.clear();
//		rockSpots.clear();
//		zonesIE.clear();
		CurrentData.forget();
//		for( AbstractMenuData aMenu : ProspectorJournal.AVAILABLE_TRACKERS) {
//			aMenu.forget();
//		}
	}
	
	/**
	 * Switches dimensions 
	 */
	public void portal() {
		if(ProspectorJournal.dims.size() == 0) astralSearch();
		for(int x =0; x < ProspectorJournal.dims.size(); x++) {
			if(ProspectorJournal.dims.get(x).dimID == this.dimID) {
				dimName = ProspectorJournal.dims.get(x).dimName;
				dimIndex = x;
				sorted(lastSort);
				return;
			}
		}
		// on failure, display would be meaningless, so close it
		astralSearch();
		this.mc.displayGuiScreen(null);
	}
	
	/**
	 * Discovers dimensions that exist in the world
	 */
	public void astralSearch() {
		ProspectorJournal.dims.clear();
		for (int i : DimensionManager.getIDs()) {
			if(DimensionManager.getWorld(i) != null) {
				try {
					ProspectorJournal.dims.add(new DimTag(i, DimensionManager.getProvider(i).getDimensionName()));
				} catch (Throwable t) {
					ProspectorJournal.dims.add(new DimTag(i, Integer.toString(i)));
				}
			}
		}
		Collections.sort(ProspectorJournal.dims, DimTag.astralOrder);
	}
	
	@Override
	public void initGui() {
//		display = 425;
		start = (this.width - display) /2;
//		wCol = this.fontRendererObj.getStringWidth("Charged Certus Quartz: 8.88M");	// 150pixels, in screen units
		wCol = this.fontRendererObj.getStringWidth("Certus Quartz: 8.88M");
		
		CurrentData = ProspectorJournal.AVAILABLE_TRACKERS.get(0);
		
		dimID = Minecraft.getMinecraft().theWorld.provider.dimensionId;
		portal();
//		guiButtons();
	}
	
	/**
	 * Makes all the button widgets for this screen configuration
	 */
	@SuppressWarnings("unchecked")
	protected void guiButtons() {
		final int x = 47;
		this.buttonList.clear();
		final int tEnd = Math.min( (high-low)*2, NUM_ROWS * 2);
		for (int j = 0; j< tEnd; j+=2) {
//			if(lastData == Utils.EXCAVATOR) {
//				this.buttonList.add(new GuiButton(j  , start +400, x +8*j, 20, 10, StatCollector.translateToLocal("btn.delete.name")));
//			} else {
//				this.buttonList.add(new GuiButton(j  , start +400, x +8*j, 20, 10, StatCollector.translateToLocal("btn.exhaust.name")));
//			}
			this.buttonList.add(new GuiButton(j  , start +400, x +8*j, 20, 10, CurrentData.getExhaustName()));
			this.buttonList.add(new GuiButton(j+1, start +370, x +8*j, 30, 10, StatCollector.translateToLocal("btn.mark.name")));
		}
		this.buttonList.add(new GuiButton(buttonList.size(), start    , 5, 80, 20, StatCollector.translateToLocal("btn.oreveins.name")));
		this.buttonList.add(new GuiButton(buttonList.size(), start + 85, 5, 80, 20, StatCollector.translateToLocal("btn.bedrock.name")));
		this.buttonList.add(new GuiButton(buttonList.size(), start + 170, 5, 80, 20, StatCollector.translateToLocal("btn.excavator.name")));
		this.buttonList.add(new GuiButton(buttonList.size(), start + 265, 5, 80, 20, StatCollector.translateToLocal("btn.map.name")));
		this.buttonList.add(new GuiButton(buttonList.size(), start + 350, 5, 70, 20, StatCollector.translateToLocal("btn.stoptrack.name")));
		this.updateScreen();
	}
	
	/**
	 * Handles the buttons actually doing things.
	 */
	@Override
	protected void actionPerformed(GuiButton button) {
		if(button.id == this.buttonList.size() -1) {
			// the Stop Tracking button
			ProspectorJournal.doGui = false;
			ProspectorJournal.yMarker = -1;
			this.mc.displayGuiScreen(null);
			allClear();
		} else if(button.id == this.buttonList.size() -2) {
			// the map screen button.
			// TODO a map
		} else if(button.id == this.buttonList.size() -3) {
			// the excavator tracking select
			sorted(Utils.EXCAVATOR, lastSort);
		} else if(button.id == this.buttonList.size() - 4) {
			// select bedrock data
			sorted(Utils.BEDROCK, lastSort);
		} else if(button.id == this.buttonList.size() - 5) {
			// select ore veins data
			sorted(Utils.ORE_VEIN, lastSort);
		} else if(button.id % 2 == 0) {
			//exhaustion buttons
			final boolean tChange = CurrentData.exhaust(low + button.id / 2) ;
			if(tChange)
				max--;
			updateScreen();
//			switch(lastData) {
//			case Utils.ORE_VEIN:
//				final RockMatter o = oreVeins.get(low + (button.id / 2) ).datum;
//				for(RockMatter e : ProspectorJournal.rockSurvey) {
//					if(e.dim == o.dim && e.x == o.x && e.y == o.y && e.z == o.z && e.ore == o.ore) {
//						if(ProspectorJournal.doGui && ProspectorJournal.xMarker == o.x && ProspectorJournal.yMarker == o.y && ProspectorJournal.zMarker == o.z) {
//							ProspectorJournal.doGui = false;
//							ProspectorJournal.yMarker = -1;
//						}
//						if(e.sample) {
//							ProspectorJournal.rockSurvey.remove(e);
//							Utils.writeJson(Utils.GT_FILE);
//							sorted(Utils.ORE_VEIN, lastSort);
//							return;
//						}
//						e.dead = ! e.dead;
//						e.multiple = 0;
//						Utils.writeJson(Utils.GT_FILE);
//						sorted(Utils.ORE_VEIN, lastSort);
//						return;
//					}
//				}
//				break;
//			case Utils.BEDROCK:
//				final GeoTag p = rockSpots.get(low + (button.id / 2)).datum;
//				for(GeoTag e : ProspectorJournal.bedrockFault) {
//					if(e.dim == p.dim && e.x == p.x && e.z == p.z && e.ore == p.ore) {
//						if(ProspectorJournal.doGui && ProspectorJournal.xMarker == p.x && ProspectorJournal.yMarker <= 5 && ProspectorJournal.zMarker == p.z) {
//							ProspectorJournal.doGui = false;
//							ProspectorJournal.yMarker = -1;
//						}
//						e.dead = ! e.dead;
//						Utils.writeJson(Utils.GT_BED_FILE);
//						sorted(Utils.BEDROCK, lastSort);
//						return;
//					}
//				}
//				break;
//			case Utils.EXCAVATOR:
//				final VoidMine q = zonesIE.get(low + (button.id/2)).datum;
//				for(VoidMine e : ProspectorJournal.voidVeins) {
//					if(e.dim == q.dim && e.x == q.x && e.z == q.z && 
////							e.oreSet == q.oreSet) {
//							e.getOreName().equalsIgnoreCase(q.getOreName()) ) {
//						if(ProspectorJournal.doGui && ProspectorJournal.xMarker == q.x && ProspectorJournal.yMarker == 255 && ProspectorJournal.zMarker == q.z) {
//							ProspectorJournal.doGui = false;
//							ProspectorJournal.yMarker = -1;
//						}
////						e.dead = ! e.dead;
//						ProspectorJournal.voidVeins.remove(e);
//						Utils.writeJson(Utils.IE_VOID_FILE);
//						sorted(Utils.EXCAVATOR, lastSort);
//						return;
//					}
//				}
//				break;
//			}
		} else if (button.id % 2 == 1) {
			// the Tracking activation buttons
			final int i = button.id / 2;
			ProspectorJournal.doGui = true;
			CurrentData.trackCoords(low + i);
//			switch (lastData) {
//			case Utils.ORE_VEIN:
//				ProspectorJournal.xMarker = oreVeins.get(low+i).datum.x;
//				ProspectorJournal.yMarker = oreVeins.get(low+i).datum.y;
//				ProspectorJournal.zMarker = oreVeins.get(low+i).datum.z;
//				break;
//			case Utils.BEDROCK:
//				ProspectorJournal.xMarker = rockSpots.get(low+i).datum.x;
//				ProspectorJournal.yMarker = rockSpots.get(low+i).datum.sample ? 4 : 1;
//				ProspectorJournal.zMarker = rockSpots.get(low+i).datum.z;
//				break;
//			case Utils.EXCAVATOR:
//				ProspectorJournal.xMarker = zonesIE.get(low+i).datum.x;
//				ProspectorJournal.yMarker = 255;
//				ProspectorJournal.zMarker = zonesIE.get(low+i).datum.z;
//				break;
//			}
			this.mc.displayGuiScreen(null);
			allClear();
		}
	}
	
	@Override
	protected void keyTyped(char key, int i) {
		// searchbox stuff
		if(oSearchBox.hasFocus){
			if(i == 1) {	//esc key
				this.mc.displayGuiScreen(null);
				allClear();
				return;
			} // else if (i == ) // enter & return keys
		// TODO typing in search
			updateScreen();
		// close on hotkey press, or ESCape key
		} else if(i == KeyBindings.rocksMenu.getKeyCode() || i == 1) {
			this.mc.displayGuiScreen(null);
			allClear();
		}
	}
	
	@Override
	public boolean doesGuiPauseGame() {return false;}
	
	@Override
	public void drawDefaultBackground() {
		super.drawDefaultBackground();
		drawRect(start + 20, 210, start + 100, 225, -9408400);
		drawRect(start + 24, 212, start +  96, 223, 0xFF000000);
//		drawRect(start + 256, 210, start +410, 225, -9408400);
//		drawRect(start + 255, 211, start +409, 223, 0xFF000000);
	}
	
	/**
	 * Sorts the list of Display<> objects,
	 * and populates it from the master database if necessary.
	 * @param dataSet	which type of data
	 * @param sortBy	how to sort and/or filter it
	 */
	private void sorted(final byte dataSet, final short sortBy) {
		final int aX = (int) this.mc.thePlayer.posX, aZ = (int) this.mc.thePlayer.posZ;
		if (dataSet == CurrentData.mType) {
			if(sortBy != lastSort)
				max = CurrentData.sortBy(sortBy, (short) dimID, aX, aZ); 
		} else {
			CurrentData.forget();
			max = 0;
			for(AbstractMenuData aType : ProspectorJournal.AVAILABLE_TRACKERS) {
				if(aType.mType == dataSet) {
					CurrentData = aType;
					max = CurrentData.sortBy(sortBy, (short) dimID, aX, aZ);
					break;
				}
			}
		}
//		switch(dataSet) {
//		case Utils.BEDROCK:
//			rockSpots.clear();
//			for(GeoTag r : ProspectorJournal.bedrockFault) {
//				if(dimID != r.dim) continue;
//				if(sortBy == Utils.DISTANCE || r.ore == 0 || Dwarf.singOf(r.ore).containsKey(sortBy) )
//					rockSpots.add(new Display<GeoTag>(r, aX, aZ));
//			}
//			if(rockSpots.isEmpty()) {
//				rockSpots.add(new Display<GeoTag>(new GeoTag(0, dimID, aX, aZ, true), aX, aZ));
//				break;
//			}
//			Collections.sort(rockSpots, sortBy == Utils.DISTANCE ? rockSpots.get(0).getCloseComparator() : rockSpots.get(0).getQualityComparator(sortBy));
//			high = (rockSpots.size() > 10) ? 10 : rockSpots.size();
//			break;
//		case Utils.ORE_VEIN:
//			oreVeins.clear();
//			for(RockMatter r : ProspectorJournal.rockSurvey) {
//				if(dimID != r.dim) continue;
//				if(sortBy == Utils.DISTANCE || r.ore == 0 || Dwarf.getFractionIn(r.ore, sortBy) != 0)
//					oreVeins.add(new Display<RockMatter>(r, aX, aZ));
//			}
//			if(oreVeins.isEmpty()) {
//				oreVeins.add(new Display<RockMatter>(new RockMatter(0, dimID, aX, 255, aZ, true), aX, aZ));
//				break;
//			}
//			Collections.sort(oreVeins, sortBy == Utils.DISTANCE ? oreVeins.get(0).getCloseComparator() : oreVeins.get(0).getQualityComparator(sortBy));
//			high = (oreVeins.size() > 10) ? 10 : oreVeins.size();
//			break;
//		case Utils.EXCAVATOR:
//			zonesIE.clear();
//			for(VoidMine r : ProspectorJournal.voidVeins) {
//				if(dimID != r.dim) continue;
//				if(sortBy == Utils.DISTANCE || r.getFraction(sortBy) != 0)
//					zonesIE.add(new Display<VoidMine>(r, aX, aZ));
//			}
//			if(zonesIE.isEmpty()) {
////				zonesIE.add(new Display<VoidMine>(new VoidMine(dimID, aX, aZ, new blusunrize.immersiveengineering.api.tool.ExcavatorHandler.MineralWorldInfo()), aX, aZ));
//				zonesIE.add(new Display<VoidMine>(new VoidMine((short) dimID, aX, aZ, ""), aX, aZ));
//				break;
//			}
//			Collections.sort(zonesIE, sortBy == Utils.DISTANCE ? zonesIE.get(0).getCloseComparator() : zonesIE.get(0).getQualityComparator(sortBy));
//			high = (zonesIE.size() > 10) ? 10 : zonesIE.size();
//			break;
//		}
//		Collections.reverse(active);
		low = 0;
		high = Math.min(max, NUM_ROWS);
//		lastData = dataSet;
		lastSort = sortBy;
		guiButtons();
	}
	
	private void sorted(short sortBy){
		final int aX = (int) this.mc.thePlayer.posX, aZ = (int) this.mc.thePlayer.posZ;
		max = CurrentData.sortBy(sortBy, (short) dimID, aX, aZ);
		high = Math.min(max, NUM_ROWS);
		low = 0;
		lastSort = sortBy;
		guiButtons();
	}
	
	@Override
//	@cpw.mods.fml.relauncher.SideOnly(cpw.mods.fml.relauncher.Side.CLIENT)
	public void drawScreen(int x, int y, float f) {
		if (high > max) {
			high = max;
			low = Math.max(0, high - NUM_ROWS);
			if (max < NUM_ROWS) {
				guiButtons();
				return;
			}
		}

		int l = 50;
		drawDefaultBackground();
		
//		drawRect(start + 20, 210, start + 100, 225, -9408400);
		
		this.fontRendererObj.drawString(dimName, start + 20 + (80-this.fontRendererObj.getStringWidth(dimName))/2, 214, Utils.WHITE);
		
		oSearchBox.draw(this.fontRendererObj);
		
		drawRect(start, 30, start+display, 32, -9408400);	// 0xff707070
		drawRect(start, 44, start+display, 46, -9408400);
		
		this.fontRendererObj.drawString(StatCollector.translateToLocal("str.distance.name"), start + 2, 35, Utils.WHITE);
        this.fontRendererObj.drawString("X", start + 50, 35, Utils.WHITE);
        this.fontRendererObj.drawString("Y", start + 80, 35, Utils.WHITE);
        this.fontRendererObj.drawString("Z", start + 110, 35, Utils.WHITE);
		this.fontRendererObj.drawString(StatCollector.translateToLocal("str.num.name"), start + 140, 35, Utils.WHITE);
		if(lastSort == Utils.DISTANCE)
			this.fontRendererObj.drawString(StatCollector.translateToLocal("str.material.name"), start +190, 35, Utils.WHITE);
		else
			this.fontRendererObj.drawString(Dwarf.name(lastSort) + StatCollector.translateToLocal("str.content.name"), start +190, 35, Utils.WHITE);

//        ArrayList<MineralMine.Display> active = getActive(lastData);
//        for(MineralMine.Display e : active.subList(low, high)) {
//		int j;
//		switch(lastData) {
//		case Utils.ORE_VEIN:
//			j = oreVeins.size();
//			break;
//		case Utils.BEDROCK:
//			j = rockSpots.size();
//			break;
//		case Utils.EXCAVATOR:
//			j = zonesIE.size();
//			break;
//		default:
//			j = 0;
//		}
		for(int i = low; i<high; i++) {
			CurrentData.drawDataRow(i, start, l, this, fontRendererObj, lastSort);
//        	int colour, w;
//        	String ts;
//        	Display<? extends MineralMine> e;
//        	switch(lastData) {
//        	case Utils.ORE_VEIN:
//        		Display<RockMatter> r = oreVeins.get(i);
//        		e =  r;
//        		if(ProspectorJournal.xMarker == e.datum.x && ProspectorJournal.zMarker == e.datum.z && ProspectorJournal.yMarker == r.datum.y) {
//        			colour = Utils.GREEN;
//        		} else if(e.datum.dead)
//        			colour = Utils.GRAY;
//        		else colour = Utils.WHITE;
//        		
//        		ts = r.datum.sample ? ("<"+ Integer.toString(r.datum.y)+ "?" ) : Integer.toString(r.datum.y);
//           		this.fontRendererObj.drawString(ts, start + (83 -(this.fontRendererObj.getStringWidth(ts)/2)), l, colour);
//        		ts = r.datum.multiple + StatCollector.translateToLocal("sym.x.name");
//           		this.fontRendererObj.drawString(ts, start + (145 -(this.fontRendererObj.getStringWidth(ts)/2)), l, colour);
//    			ts = lastSort == Utils.DISTANCE ? Dwarf.name(r.datum.ore) : StatCollector.translateToLocal("str.value.name") + " " + Integer.toString(Dwarf.getFractionIn(r.datum.ore, lastSort));
//    			this.fontRendererObj.drawString(ts, start + 190, l, colour);
//    			w = r.datum.ore;
//    			if(lastSort == Utils.DISTANCE || lastSort == w) {
//    				this.drawTexturedModelRectFromIcon(start + 172, l, ((Item)OP.dust.mRegisteredPrefixItems.get(0)).getIconFromDamage(w), 16, 16);
//    			} else {
//    				this.drawTexturedModelRectFromIcon(start + 172, l, ((Item)OP.crushedPurified.mRegisteredPrefixItems.get(0)).getIconFromDamage(w), 16, 16);
//    			}
//        		break;
//        	case Utils.BEDROCK:
//        		Display<GeoTag> q = rockSpots.get(i);
//        		e = q;
//        		if(ProspectorJournal.xMarker == e.datum.x && ProspectorJournal.zMarker == e.datum.z && ProspectorJournal.yMarker < 5) {
//        			colour = Utils.GREEN;
//        		} else if(e.datum.dead)
//        			colour = Utils.GRAY;
//        		else colour = Utils.WHITE;
//        		
//        		ts = q.datum.sample ? StatCollector.translateToLocal("str.floor.name") : "0";
//           		this.fontRendererObj.drawString(ts, start + (83 -(this.fontRendererObj.getStringWidth(ts)/2)), l, colour);
//           		ts = StatCollector.translateToLocal("sym.inf.name");
//           		this.fontRendererObj.drawString(ts, start + (145 -(this.fontRendererObj.getStringWidth(ts)/2)), l, colour);
//    			ts = lastSort == Utils.DISTANCE ? Dwarf.name(q.datum.ore) : StatCollector.translateToLocal("str.value.name") + " " + Utils.approx(Dwarf.singOf(q.datum.ore).get(lastSort));
//    			this.fontRendererObj.drawString(ts, start + 190, l, colour);
//    			w = q.datum.ore;
//    			if(lastSort == Utils.DISTANCE || lastSort == w) {
//    				this.drawTexturedModelRectFromIcon(start + 172, l, ((Item)OP.dust.mRegisteredPrefixItems.get(0)).getIconFromDamage(w), 16, 16);
//    			} else {
//    				this.drawTexturedModelRectFromIcon(start + 172, l, ((Item)OP.crushedPurified.mRegisteredPrefixItems.get(0)).getIconFromDamage(w), 16, 16);
//    			}
//        		break;
//        	case Utils.EXCAVATOR:
//        		Display<VoidMine> p = zonesIE.get(i);
//        		e =  p;
//        		if(ProspectorJournal.xMarker == e.datum.x && ProspectorJournal.zMarker == e.datum.z && ProspectorJournal.yMarker > 200) {
//        			colour = Utils.GREEN;
//        		} else colour = Utils.WHITE;
//        		
//        		ts = StatCollector.translateToLocal("str.any.name");
//           		this.fontRendererObj.drawString(ts, start + (83 -(this.fontRendererObj.getStringWidth(ts)/2)), l, colour);
////           		ts = Utils.approx(p.datum.multiple) + StatCollector.translateToLocal("sym.x.name");
//           		ts = Utils.approx(blusunrize.immersiveengineering.api.tool.ExcavatorHandler.mineralVeinCapacity) + StatCollector.translateToLocal("sym.x.name");
//           		this.fontRendererObj.drawString(ts, start + (145 -(this.fontRendererObj.getStringWidth(ts)/2)), l, colour);
//    			ts = lastSort == Utils.DISTANCE ? p.datum.isValid() ? p.datum.getOreName() : "Nil" : StatCollector.translateToLocal("str.value.name") + " " + Utils.approx(IEDwarf.singOf(p.datum.getOreName()).get(lastSort));
//    			this.fontRendererObj.drawString(ts, start + 190, l, colour);
//    			this.drawTexturedModelRectFromIcon(start + 172, l, IEDwarf.getIcon(p.datum.getOreName()), 16, 16);
//    			break;
//			default:
//				e = new Display<RockMatter>(new RockMatter(0, dimID, 0, 255, 0, true), 0, 0);
//				colour = Utils.RED;
//        	}
//        	ts = Integer.toString(e.dist);
//        	this.fontRendererObj.drawString(ts, start + (11 -(this.fontRendererObj.getStringWidth(ts)/2)), l, colour);
//        	ts = Integer.toString(e.datum.x);
//        	this.fontRendererObj.drawString(ts, start + (52 -(this.fontRendererObj.getStringWidth(ts)/2)), l, colour);
//        	ts = Integer.toString(e.datum.z);
//        	this.fontRendererObj.drawString(ts, start + (112 -(this.fontRendererObj.getStringWidth(ts)/2)), l, colour);
        	
        	GL11.glPushMatrix();
        	GL11.glDisable(GL11.GL_LIGHTING);
        	GL11.glColor3f(1, 1, 1);
        	drawRect(start, l+11, start + display, l + 12, -9408400);
        	GL11.glPopMatrix();
        	
        	// would be z-layered by lower lines here
        	l += 16;
		}
		l = 50;
		for(int i = low; i<high; i++) {
        	if(Utils.inBounds(x, start +172, start +350) && Utils.inBounds(y, l-5, l+8)) {
        		List<String> toolTip = new ArrayList<>();
        		java.util.Map <Short, Integer> longChant;
        		toolTip.add("\u00a7"+Integer.toHexString(15)+StatCollector.translateToLocal("str.listparts.name"));
        		longChant = CurrentData.getSong(i);
//        		switch(lastData) {
//        		case Utils.ORE_VEIN:
//	        		longChant = Dwarf.read(oreVeins.get(i).datum.ore).mByBy;
//	        		break;
//        		case Utils.BEDROCK:
//        			longChant = Dwarf.singOf(rockSpots.get(i).datum.ore);
//        			break;
//        		case Utils.EXCAVATOR:
//        			longChant = IEDwarf.singOf(zonesIE.get(i).datum.getOreName());
//        			break;
//        		default:
//        			longChant = new java.util.HashMap<>(0);
//        		}
        		List<java.util.Map.Entry<Short, Integer>> verses = new ArrayList<java.util.Map.Entry<Short, Integer>>(longChant.entrySet());
        		Collections.sort(verses, Dwarf.FractionSorter);
        		for (int g = 0; g < 22 && g < verses.size(); g++) {
        			toolTip.add(Dwarf.name(verses.get(g).getKey())+ ": "+ Utils.approx(verses.get(g).getValue()));
        		}
//        		for(java.util.Map.Entry<Short, Integer> byMat : verses) {
//        			toolTip.add(Dwarf.name(byMat.getKey())+ ": " + Utils.approx(byMat.getValue()));
//        		}
        		drawHoveringText(toolTip, x, y, fontRendererObj);
        		if(verses.size() > 22) {
        			toolTip.clear();
            		for (int g = 0; g < 22 && g < (verses.size() - 22); g++) {
            			toolTip.add(Dwarf.name(verses.get(g+22).getKey())+ ": "+ Utils.approx(verses.get(g+22).getValue()));
            		}
            		drawHoveringText(toolTip, x + 10 + wCol, y, fontRendererObj);
        			if(verses.size() > 44) {
        				toolTip.clear();
                		for (int g = 0; g < 21 && g < (verses.size() - 44); g++) {
                			toolTip.add(Dwarf.name(verses.get(g+44).getKey())+ ": "+ Utils.approx(verses.get(g+44).getValue()));
                		}
                		if(verses.size() == 66) {
                			toolTip.add(Dwarf.name(verses.get(65).getKey())+": "+ Utils.approx(verses.get(65).getValue()));
                		} else if(verses.size() > 66) {
                			int u = 0;
                			for(int g=65; g < verses.size(); g++) {
                				u += verses.get(g).getValue();
                			}
                			toolTip.add(StatCollector.translateToLocal("str.trace.name") + ": " + Utils.approx(u));
                		}
                		drawHoveringText(toolTip, x + 20 + 2 * wCol, y, fontRendererObj);
        			}
        		}
        	}
        	
        	l += 16;
        }
        
        this.mc.getTextureManager().bindTexture(smallArrow);

        RenderHelper.enableGUIStandardItemLighting();
	    GL11.glDisable(GL11.GL_LIGHTING);
	    GL11.glEnable(GL11.GL_ALPHA_TEST);
	    GL11.glEnable(GL11.GL_BLEND);
	    
        if(low > 0) 
        	this.drawTexturedModalRect((this.width -50)/2, 210, 1, 1, 15, 17);
        if(high < max)
        	this.drawTexturedModalRect((this.width +32) /2, 210,  17, 1, 32, 17);
        this.drawTexturedModalRect(start     , 210, 91, 41, 17, 17);
        this.drawTexturedModalRect(start +102, 210, 91, 25, 17, 17);

        GL11.glEnable(GL11.GL_LIGHTING);
	    GL11.glDisable(GL11.GL_ALPHA_TEST);
	    GL11.glDisable(GL11.GL_BLEND);
        RenderHelper.disableStandardItemLighting();
        
        if(com.github.canisartorus.prospectorjournal.ConfigHandler.backupTextArrows) {
        	if (low > 0)	this.fontRendererObj.drawString("^", (this.width -50) /2, 225, Utils.WHITE);
        	if( high < max)	this.fontRendererObj.drawString("v", (this.width +32) /2, 225, Utils.WHITE);
        	this.fontRendererObj.drawString("<", start, 225, Utils.WHITE);
        	this.fontRendererObj.drawString(">", start +102, 225, Utils.WHITE);
        }
        
        super.drawScreen(x, y, f);
	}
	
	@Override
	public void drawTexturedModelRectFromIcon(int aX, int aY, IIcon aIcon, int aWide, int aHeight) {
        RenderHelper.enableGUIStandardItemLighting();
	    GL11.glDisable(GL11.GL_LIGHTING);
	    GL11.glEnable(GL11.GL_ALPHA_TEST);
	    GL11.glEnable(GL11.GL_BLEND);
	
//	    net.minecraft.client.renderer.entity.RenderItem.getInstance().renderIcon(aX, aY, aIcon, 16, 16);
	    super.drawTexturedModelRectFromIcon(aX, aY, aIcon, aWide, aHeight);
	
	    GL11.glEnable(GL11.GL_LIGHTING);
	    GL11.glDisable(GL11.GL_ALPHA_TEST);
	    GL11.glDisable(GL11.GL_BLEND);
        RenderHelper.disableStandardItemLighting();
	}
	
	@Override
	public void mouseMovedOrUp(int mouseX, int mouseY, int button) {
		// For click on areas
		if(button >= 0) {
			if(Utils.inBounds(mouseX, (this.width-50)/2, (this.width-50)/2 +15) &&Utils.inBounds(mouseY, 210, 227) && low > 0) {
				low -= 1;
				high -=1;
				updateScreen();
			} else if(Utils.inBounds(mouseX, (this.width+32)/2, (this.width+32)/2 +15) &&Utils.inBounds(mouseY, 210, 227) && high < max ) {
//				switch(lastData) {
//				case Utils.ORE_VEIN:
//					if(high == oreVeins.size())
//						return;
//					break;
//				case Utils.BEDROCK:
//					if(high == rockSpots.size())
//						return;
//					break;
//				case Utils.EXCAVATOR:
//					if(high == zonesIE.size())
//						return;
//					break;
//				}
				low += 1;
				high += 1;
				updateScreen();
			} else if(Utils.inBounds(mouseX, start, start +17) &&Utils.inBounds(mouseY, 210, 226) ) {
				if(dimIndex == 0) {
					dimIndex = ProspectorJournal.dims.size()-1;
				} else {
					dimIndex -= 1;
				}
				dimID = ProspectorJournal.dims.get(dimIndex).dimID;
				dimName = ProspectorJournal.dims.get(dimIndex).dimName;
				sorted(lastSort);
			} else if(Utils.inBounds(mouseX, start +102, start +119) &&Utils.inBounds(mouseY, 210, 226) ) {
				if(dimIndex < ProspectorJournal.dims.size() -1) {
					dimIndex += 1;
				} else {
					dimIndex = 0;
				}
				dimID = ProspectorJournal.dims.get(dimIndex).dimID;
				dimName = ProspectorJournal.dims.get(dimIndex).dimName;
				sorted(lastSort);
			} else if(Utils.inBounds(mouseX, start, start +40) &&Utils.inBounds(mouseY, 35, 205) ) {
				sorted(CurrentData.mType, Utils.DISTANCE);
//			} else if(max != 0 && Utils.inBounds(mouseX, 170, 186) && Utils.inBounds(mouseY, 50, 210)){
			} else if(max != 0 && Utils.inBounds(mouseX, start + 170, start + 190) && Utils.inBounds(mouseY, 50, 210)){
				int k = (mouseY - 50) / 16;
				if (low + k <= max)
				sorted(CurrentData.mType, CurrentData.getMajor(low + k));
//				switch(lastData) {
//				case Utils.ORE_VEIN:
//					sorted(lastData, oreVeins.get(low + k).datum.ore);
//					break;
//				case Utils.BEDROCK:
//					sorted(lastData, rockSpots.get(low + k).datum.ore);
//					break;
//				case Utils.EXCAVATOR:
//					sorted(lastData, IEDwarf.getMajor(zonesIE.get(low + k).datum.getOreName()));
//					break;
//				}
				
			} else if(Utils.inBounds(mouseX, start + 260, start + 410) && Utils.inBounds(mouseY, 210, 225)) {
				oSearchBox.activate();
				updateScreen();
			} else if(Utils.inBounds(mouseX, start + 410, start + 425) && Utils.inBounds(mouseY, 210, 225)) {
				oSearchBox.confirm(this);
			}
		}
	}
	
	class SearchBox {
		boolean hasFocus = false;
		private java.util.List<Character> request = new ArrayList<>();
		private String suggestion = StatCollector.translateToLocal("str.search.name");
		private net.minecraft.client.gui.GuiScreen mParent;
		
		SearchBox(net.minecraft.client.gui.GuiScreen aParent){
			mParent = aParent;
		}
		
		void activate() {
			if(hasFocus){
				suggestion = StatCollector.translateToLocal("str.search.name");
				request.clear();
			} else hasFocus = true;
		}
		
		void confirm(GuiMain gm) {
			if(hasFocus) {
				hasFocus = false;
				short match = 0;
				// TODO
				gm.sorted(CurrentData.mType, match);
			} else {
				request.clear();
				activate();
				gm.updateScreen();
			}
		}
		
		void draw(net.minecraft.client.gui.FontRenderer fr) {
			// TODO
		}
	}
}
