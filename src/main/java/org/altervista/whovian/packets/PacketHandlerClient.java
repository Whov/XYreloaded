package org.altervista.whovian.packets;

import org.altervista.whovian.blocks.Fabricator;
import org.altervista.whovian.tileentity.TileEntityFabricator;
import org.altervista.whovian.xyreloaded.XYreloaded;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.INetHandlerPlayClient;
import codechicken.lib.packet.PacketCustom;
import codechicken.lib.packet.PacketCustom.IClientPacketHandler;
import codechicken.lib.vec.BlockCoord;

public class PacketHandlerClient implements IClientPacketHandler {

	public static Object channel = XYreloaded.MODID;
	
	@Override
	public void handlePacket(PacketCustom packet, Minecraft mc, INetHandlerPlayClient netHandler) {
		switch (packet.getType()) {
		case 1:
			BlockCoord pos = packet.readCoord();
			TileEntityFabricator tile = (TileEntityFabricator)mc.theWorld.getTileEntity(pos.x, pos.y, pos.z);
			int size = packet.readInt();
			if (size==0) {
				tile.Slots = null;
				tile.size = 0;
				tile.master = packet.readCoord();
				return;
			}
			tile.Slots = new ItemStack[size];
			for (int i=0;i<size;i++) {
				tile.Slots[i] = packet.readItemStack();
			}
			break;
		}
	}

}
