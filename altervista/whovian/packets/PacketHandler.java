package org.altervista.whovian.packets;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.INetHandlerPlayServer;

import org.altervista.whovian.events.EventHandler;
import org.altervista.whovian.xyreloaded.XYreloaded;

import codechicken.lib.packet.PacketCustom;
import codechicken.lib.packet.PacketCustom.IServerPacketHandler;

public class PacketHandler implements IServerPacketHandler {

	public static Object channel = XYreloaded.MODID;
	
	@Override
	public void handlePacket(PacketCustom packet, EntityPlayerMP sender, INetHandlerPlayServer netHandler) {
		switch (packet.getType()) {
        case 1://part placing
            EventHandler.place(sender, sender.worldObj);
            break;
		}
	}

}
