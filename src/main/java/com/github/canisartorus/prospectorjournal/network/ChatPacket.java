package com.github.canisartorus.prospectorjournal.network;

import com.github.canisartorus.prospectorjournal.lib.Utils;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import gregapi.network.INetworkHandler;
import gregapi.network.IPacket;
import net.minecraft.world.IBlockAccess;

public class ChatPacket implements IPacket {

	Utils.ChatString msg;
	private byte decodeType = 0;
	
	/*
	 * Master constructor for hyper-compressed predefined chat messages over the network
	 */
	public ChatPacket(Utils.ChatString sMsg) {
		final int msgID = sMsg.ordinal();
		if(msgID <= Byte.MAX_VALUE)
			decodeType = 1;
		else if(msgID <=Short.MAX_VALUE)
			decodeType = 2;
//		else if(msgID <=Integer.MAX_VALUE)
//			decodeType = 0;
		msg = sMsg;
	}
	
	public ChatPacket(int type) {
		decodeType = (byte)type;
	}

	@Override
	public byte getPacketID() {
		return (byte) (decodeType + 0);
	}

	@Override
	public ByteArrayDataOutput encode() {
		ByteArrayDataOutput rOut = ByteStreams.newDataOutput(2);
		switch(decodeType) {
		case 1:
			rOut.writeByte(msg.ordinal());
			break;
		case 2:
			rOut.writeShort(msg.ordinal());
			break;
		default:
			rOut.writeInt(msg.ordinal());
		}
		return rOut;
	}

	@Override
	public IPacket decode(ByteArrayDataInput aData) {
		int i;
		if(decodeType == 1)
			i = aData.readByte();
		else if(decodeType == 2)
			i = aData.readShort();
		else
			i = aData.readInt();
		return new ChatPacket(Utils.ChatString.values()[i]);
	}

	@cpw.mods.fml.relauncher.SideOnly(cpw.mods.fml.relauncher.Side.CLIENT)
	@Override
	public void process(IBlockAccess aWorld, INetworkHandler aNetworkHandler) {
//		if (aWorld != null) {
			// client-side
			net.minecraft.client.Minecraft.getMinecraft().thePlayer.addChatMessage(new net.minecraft.util.ChatComponentText(
					msg.toString()));
//		}
		// server-side no-op	need non-compile server side
	}
}
