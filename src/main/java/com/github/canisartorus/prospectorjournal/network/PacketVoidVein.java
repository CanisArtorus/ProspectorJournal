package com.github.canisartorus.prospectorjournal.network;

import com.github.canisartorus.prospectorjournal.ProspectorJournal;
import com.github.canisartorus.prospectorjournal.compat.IEHandler;
import com.github.canisartorus.prospectorjournal.lib.Utils;
import com.github.canisartorus.prospectorjournal.lib.VoidMine;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler.MineralMix;
import gregapi.network.INetworkHandler;
import gregapi.network.IPacket;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.world.IBlockAccess;

/**
 * @author Gregorius Techneticies
 * @author Alexander James
 * 
 * optimized to have a smaller Packet Size within XZ[-524288;+524287]
 * Because most people don't go farther, they shouldn't be punished with Bandwidth waste-age, if they are not that far away.
 */
public class PacketVoidVein implements IPacket {
	public int mX;
	public int mZ;
	private byte mDecoderType = 0;
	public String mName;
	
	/** @param aDecoderType has to be a Number between [0 and 3] */
	public PacketVoidVein(int aDecoderType) {
		mDecoderType = (byte)(aDecoderType & 3);
		mX = mZ = 0;
	}
	
	/** The Super-Constructor for the Packet to be sent. */
	public PacketVoidVein(int aX, int aZ, String aName) {
		mX = aX;
		mZ = aZ;
		mName = aName;
		mDecoderType = (byte)((mX>=Short.MIN_VALUE&&mX<=Short.MAX_VALUE?1:0)|(mZ>=Short.MIN_VALUE&&mZ<=Short.MAX_VALUE?2:0));
	}
	
	@Override
	public final byte getPacketID() {
		return (byte)(24 + mDecoderType); // 4 Packet Handlers need to be registered to receive the possibilities of the 0-3 Range of mDecoderType.
	}
	
	@Override
	public final ByteArrayDataOutput encode() {
		ByteArrayDataOutput rOut = ByteStreams.newDataOutput(16);
		if ((mDecoderType&1)!=0) rOut.writeShort(mX); else rOut.writeInt  (mX);
		if ((mDecoderType&2)!=0) rOut.writeShort(mZ); else rOut.writeInt  (mZ);
		rOut.writeUTF(mName);
		return rOut;
	}
	
	@Override
	public final IPacket decode(ByteArrayDataInput aData) {
		return new PacketVoidVein((mDecoderType&1)!=0?aData.readShort():aData.readInt(), (mDecoderType&2)!=0?aData.readShort():aData.readInt(), aData.readUTF());
	}

	@Override
	public void process(IBlockAccess aWorld, INetworkHandler aNetworkHandler) {
		if(null == aWorld) {
			//server-side
			return;
		}
		final EntityClientPlayerMP mPlayer = net.minecraft.client.Minecraft.getMinecraft().thePlayer;
		final int mDim = mPlayer.getEntityWorld().provider.dimensionId;
		for(VoidMine t : ProspectorJournal.voidVeins) {
			if(t.dim == mDim && t.cx() == mX && t.cz() == mZ) {
				// chunk entries overwrite
				if(t.oreSet.name == mName) {
					Utils.chatAt(mPlayer, Utils.ChatString.DUPE);// "Still more vein to mine.");
					return;
				} else if(mName == IEHandler.DEPLETED) {
					Utils.chatAt(mPlayer, Utils.ChatString.DEPLETED);// "This vein is played out.");
					ProspectorJournal.voidVeins.remove(t);
					Utils.writeJson(Utils.IE_VOID_FILE);
					return;
				} else {
					Utils.chatAt(mPlayer, Utils.ChatString.CHANGED);// "Wow, something is different under here!");
					ProspectorJournal.voidVeins.remove(t);
					break;
				}
			}
		}
		final MineralMix mMix = IEHandler.getByName(mName);
		if (mMix != null) {
			ProspectorJournal.voidVeins.add(new VoidMine(mDim, mX*16 + 8, mZ*16 + 8, mMix));
			Utils.writeJson(Utils.IE_VOID_FILE);
		}
	}
}
