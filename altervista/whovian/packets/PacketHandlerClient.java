package org.altervista.whovian.packets;

import org.altervista.whovian.events.EventHandler;
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
		
	}

}
