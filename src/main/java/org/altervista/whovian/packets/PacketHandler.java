package org.altervista.whovian.packets;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.network.play.INetHandlerPlayServer;

import org.altervista.whovian.blocks.Fabricator;
import org.altervista.whovian.client.renderer.container.ContainerFabricatorConfig;
import org.altervista.whovian.events.EventHandler;
import org.altervista.whovian.tileentity.TileEntityFabricator;
import org.altervista.whovian.xyreloaded.XYreloaded;

import codechicken.lib.packet.PacketCustom;
import codechicken.lib.packet.PacketCustom.IServerPacketHandler;
import codechicken.lib.vec.BlockCoord;

public class PacketHandler implements IServerPacketHandler {

	public static Object channel = XYreloaded.MODID;
	
	@Override
	public void handlePacket(PacketCustom packet, EntityPlayerMP sender, INetHandlerPlayServer netHandler) {
		BlockCoord pos; int size;
		switch (packet.getType()) {
        case 1://part placing
            EventHandler.place(sender, sender.worldObj);
            break;
        case 2://recipe added
        	pos = packet.readCoord();
        	ItemStack[] required = new ItemStack[9];
        	ItemStack result;
        	
        	InventoryCrafting matrix = new InventoryCrafting(null, 3, 3);
        	for (int i=0;i<9;i++) {
        		required[i] = packet.readItemStack();
        		try {
        			matrix.setInventorySlotContents(i, required[i]);
        		} catch (Exception e) {}//when it gets to container update result skip that
        	}
        	
        	result = CraftingManager.getInstance().findMatchingRecipe(matrix, sender.worldObj);
        	if (null==result) return;
        	
        	TileEntityFabricator tile = (TileEntityFabricator) sender.worldObj.getTileEntity(pos.x, pos.y, pos.z);
        	
        	tile.recipes.add(new ShapedRecipes(3, 3, required, result.copy()));
        	
		}
	}
	
}
