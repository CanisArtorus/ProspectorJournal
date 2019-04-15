package com.github.canisartorus.prospectorjournal;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.server.integrated.IntegratedServer;

import java.io.File;
import java.net.InetSocketAddress;

import com.github.canisartorus.prospectorjournal.ProspectorJournal;
import com.github.canisartorus.prospectorjournal.lib.Utils;

public class ClientConnectionEvent {

    @SubscribeEvent
    public void onConnected(FMLNetworkEvent.ClientConnectedToServerEvent event) {

        String hostname;

        if (!event.isLocal) {

            InetSocketAddress address = (InetSocketAddress) event.manager.getSocketAddress();
            hostname = address.getHostName() + "_" + address.getPort();

        } else {

            IntegratedServer server = Minecraft.getMinecraft().getIntegratedServer();
            hostname = (server != null) ? server.getFolderName() : "sp_world";
        }

        hostname = Utils.invalidChars(hostname);
        hostname = "ProspectorJournal/" + hostname;

        File fileJson = new File(hostname);
        if (!fileJson.exists()) {
			System.out.println("Creating new directory "+ hostname);
            fileJson.mkdirs();
        }

        ProspectorJournal.hostName = hostname;

        ProspectorJournal.rockSurvey.clear();
        ProspectorJournal.bedrockFault.clear();
		ProspectorJournal.voidVeins.clear();

        Utils.readJson(Utils.GT_FILE);
        Utils.readJson(Utils.GT_BED_FILE);
		Utils.readJson(Utils.IE_VOID_FILE);

    }

}