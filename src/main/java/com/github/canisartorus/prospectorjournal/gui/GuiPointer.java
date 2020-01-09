/**
 * @author Alexander James
 * @author Dyonovan
 */
package com.github.canisartorus.prospectorjournal.gui;

import org.lwjgl.opengl.GL11;

import com.github.canisartorus.prospectorjournal.ConfigHandler;
import com.github.canisartorus.prospectorjournal.ProspectorJournal;
import com.github.canisartorus.prospectorjournal.lib.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

/**
 * @author Dyonovan
 *
 */
public class GuiPointer extends Gui {	
	private static final ResourceLocation arrow = new ResourceLocation("prospectorjournal:textures/gui/NavArrow.png");
    private Minecraft mc;


    public GuiPointer(Minecraft mc) {
        super();

        this.mc = mc;
    }

//    @SuppressWarnings("unused")
    @cpw.mods.fml.common.eventhandler.SubscribeEvent
    public void onRender(net.minecraftforge.client.event.RenderGameOverlayEvent event) {

        if (event.isCancelable() || event.type != net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.EXPERIENCE || !ProspectorJournal.doGui) {
        	return;
        }
    	boolean wrong=true;
        if (ConfigHandler.needHUD) {
        	if(this.mc.thePlayer.inventory.armorItemInSlot(3) != null) {
	        	for(String test : ConfigHandler.HUDsList) { 
	        		if( test == this.mc.thePlayer.inventory.armorItemInSlot(3).getItem().getUnlocalizedName()) {
	        			wrong=false;
	        			break;
	        		}
	        	}
        	}
        } else if(this.mc.thePlayer.getCurrentEquippedItem() != null)
			if(this.mc.thePlayer.getCurrentEquippedItem().getUnlocalizedName().equalsIgnoreCase("ca.prospectorjournal.notebook"))
				wrong =false;
		if(wrong) return;

		final int arrowWidth = 64;
        final int arrowHeight= 64;

        double direction = (Math.toDegrees(Math.atan2(ProspectorJournal.xMarker - this.mc.thePlayer.posX,
                ProspectorJournal.zMarker - this.mc.thePlayer.posZ))) + this.mc.thePlayer.rotationYaw;

        this.mc.getTextureManager().bindTexture(arrow);
        net.minecraft.client.gui.ScaledResolution scaledresolution = new net.minecraft.client.gui.ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        double width = scaledresolution.getScaledWidth();

        GL11.glPushMatrix();

        GL11.glTranslated(width / 2 + ConfigHandler.arrowX, arrowHeight / 2 + 5 + ConfigHandler.arrowY, 0);
        GL11.glRotatef((float) -direction, 0, 0, 1);
        GL11.glScaled(ConfigHandler.arrowSize, ConfigHandler.arrowSize, 1F);
        GL11.glTranslatef(-arrowWidth / 2, -arrowHeight / 2, 0);

        net.minecraft.client.renderer.Tessellator tl = net.minecraft.client.renderer.Tessellator.instance;
        tl.startDrawingQuads();
        tl.addVertexWithUV(0, 0, 0, 0, 0);
        tl.addVertexWithUV(0, arrowHeight, 0, 0, 1);
        tl.addVertexWithUV(arrowWidth, arrowHeight, 0, 1, 1);
        tl.addVertexWithUV(arrowWidth, 0, 0, 1, 0);
        tl.draw();
        GL11.glPopMatrix();

        int distancePL = (int) Math.round(this.mc.thePlayer.getDistance(ProspectorJournal.xMarker, mc.thePlayer.posY, ProspectorJournal.zMarker));
        String dirY;
		int color;
		if(mc.thePlayer.posY > ProspectorJournal.yMarker) {
			dirY = StatCollector.translateToLocal("str.below.name");
			color = Utils.RED;
		} else if(mc.thePlayer.posY == ProspectorJournal.yMarker) {
			dirY = StatCollector.translateToLocal("str.level.name");
			color = Utils.WHITE;
		} else {
            dirY = StatCollector.translateToLocal("str.above.name");
			color = Utils.GREEN;
		}
        String blocks = Integer.toString(distancePL) + StatCollector.translateToLocal("str.blocks.name");
        net.minecraft.client.gui.FontRenderer fr = this.mc.fontRenderer;

        GL11.glPushMatrix();
        GL11.glTranslated(width / 2 + ConfigHandler.arrowX,arrowHeight + (5 * ConfigHandler.arrowSize) + ConfigHandler.arrowY, 0);
        GL11.glScaled(ConfigHandler.arrowSize, ConfigHandler.arrowSize, 1F);
        GL11.glTranslatef(-fr.getStringWidth(blocks + " " + dirY), 0, 0);

        fr.drawString(blocks + " - " + dirY, fr.getStringWidth(blocks + " " + dirY) / 2,
                0, color);
        GL11.glPopMatrix();
	}
}
