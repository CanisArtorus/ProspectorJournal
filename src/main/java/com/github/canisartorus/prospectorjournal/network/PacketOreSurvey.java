package com.github.canisartorus.prospectorjournal.network;

import com.github.canisartorus.prospectorjournal.ProspectorJournal;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import gregapi.network.INetworkHandler;
import gregapi.network.IPacket;
import gregapi.network.packets.PacketCoordinates;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.IBlockAccess;

public class PacketOreSurvey extends PacketCoordinates implements IPacket {

	public short meta = 0;
	public byte type;

	public PacketOreSurvey(int aDecoderType) {
		super(aDecoderType);
	}

	public PacketOreSurvey(int aX, int aY, int aZ, short aMeta, byte aType) {
		super(aX, aY, aZ);
		meta = aMeta;
		type = aType;
	}

	@Override
	@cpw.mods.fml.relauncher.SideOnly(cpw.mods.fml.relauncher.Side.CLIENT)
	public void process(IBlockAccess aWorld, INetworkHandler aNetworkHandler) {
//		if(null == aWorld) {
//			//Server-side
//			return;
//		}
		try {
			if(ConfigHandler.debug) System.out.println(ProspectorJournal.MOD_ID +"[INFO] : Client recieved sample of material "+ meta +".");
			com.github.canisartorus.prospectorjournal.JournalBehaviour.TakeSample((net.minecraft.world.World)aWorld, mX, mY, mZ, meta, type, net.minecraft.client.Minecraft.getMinecraft().thePlayer);
		} catch (Exception e) {
			System.out.println(ProspectorJournal.MOD_NAME + "[WARNING] : Packet processing failure "+e.toString());
			EntityPlayer localPC = net.minecraft.client.Minecraft.getMinecraft().thePlayer;
			com.github.canisartorus.prospectorjournal.JournalBehaviour.TakeSample(localPC.getEntityWorld(), mX, mY, mZ, meta, type, localPC);
		}

	}

	@Override
	public byte getPacketIDOffset() {
		return 8;
	}

	@Override
	public ByteArrayDataOutput encode2(ByteArrayDataOutput aData) {
		aData.writeShort(meta);
		aData.writeByte(type);
		return aData;
	}

	@Override
	public PacketOreSurvey decode2(int aX, int aY, int aZ, ByteArrayDataInput aData) {
		return new PacketOreSurvey(aX, aY, aZ, aData.readShort(), aData.readByte());
	}

}
