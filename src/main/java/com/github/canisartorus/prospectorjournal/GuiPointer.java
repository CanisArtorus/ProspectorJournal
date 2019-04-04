/**
 * @author Alexander James
 * @author Dyonovan
 */
package com.github.canisartorus.prospectorjournal;

import org.lwjgl.opengl.GL11;

import com.github.canisartorus.prospectorjournal.lib.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

/**
 * @author Dyonovan
 *
 */
public class GuiPointer extends Gui {	//TODO everything
	 private static final ResourceLocation arrow = new ResourceLocation("tcnodetracker:textures/gui/arrow.png");
    private Minecraft mc;


    public GuiPointer(Minecraft mc) {
        super();

        this.mc = mc;
    }

//    @SuppressWarnings("unused")
    @cpw.mods.fml.common.eventhandler.SubscribeEvent
    public void onRender(net.minecraftforge.client.event.RenderGameOverlayEvent event) {

        final int arrowWidth = 64;
        final int arrowHeight= 64;

        if (event.isCancelable() || event.type != net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.EXPERIENCE || !ProspectorJournal.doGui ||
                (ConfigHandler.needHUD && this.mc.thePlayer.inventory.armorItemInSlot(3) == null) ){
            return;
        }

        if (ConfigHandler.needHUD) {
        	boolean wrong=true;
        	for(String test : ConfigHandler.HUDsList) { 
        		if( test == this.mc.thePlayer.inventory.armorItemInSlot(3).getItem().getUnlocalizedName()) {
        			wrong=false;
        			break;
        		}
        	}
            if(wrong) return;
        }

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
        String dirY = mc.thePlayer.posY > ProspectorJournal.yMarker ? StatCollector.translateToLocal("str.below.name") :
                mc.thePlayer.posY == ProspectorJournal.yMarker ? StatCollector.translateToLocal("str.level.name") :
                StatCollector.translateToLocal("str.above.name");
        String blocks = Integer.toString(distancePL) + StatCollector.translateToLocal("str.blocks.name");
        int color = dirY.equals("Below") ? Utils.RED : dirY.equals("Level") ? Utils.WHITE : Utils.GREEN;
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
