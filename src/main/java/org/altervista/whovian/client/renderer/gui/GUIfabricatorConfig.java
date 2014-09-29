package org.altervista.whovian.client.renderer.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.ResourceLocation;

import org.altervista.whovian.blocks.Fabricator;
import org.altervista.whovian.client.renderer.container.ContainerFabricatorConfig;
import org.altervista.whovian.packets.PacketHandler;
import org.altervista.whovian.tileentity.TileEntityFabricator;

import codechicken.core.gui.GuiCCButton;
import codechicken.lib.packet.PacketCustom;

public class GUIfabricatorConfig extends CBcontainerGui {

	TileEntityFabricator tile;
	ResourceLocation craftImage = new ResourceLocation("textures/gui/container/crafting_table.png");
	
	public GUIfabricatorConfig(InventoryPlayer inv, TileEntityFabricator t) {
		super(new ContainerFabricatorConfig(inv, t), 175, 165);
		tile=t;
	}
	
	@Override
	public void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		mc.renderEngine.bindTexture(craftImage);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, 175, 165);
		super.drawGuiContainerBackgroundLayer(var1, var2, var3);
	}
	
	@Override
	public void actionPerformed(String ident, Object... params) {
		if (ident=="register") {
			if (((ContainerFabricatorConfig)inventorySlots).craftResult.getStackInSlot(0)==null) return;
			if (tile.recipes.size()>=tile.RecipeLimit) return;
			ItemStack[] required = new ItemStack[9];
			ItemStack r;
			for (int i=0;i<9;i++) {
				r = ((ContainerFabricatorConfig)inventorySlots).craftMatrix.getStackInSlot(i);
				required[i] = (r==null) ? null : r.copy();
			}
			tile.recipes.add(new ShapedRecipes(3, 3, required, ((ContainerFabricatorConfig)inventorySlots).craftResult.getStackInSlot(0).copy()));
			PacketCustom packet = new PacketCustom(PacketHandler.channel, 2);
			if (tile.size>0)
				packet.writeCoord(tile.master);
			else
				packet.writeCoord(tile.xCoord, tile.yCoord, tile.zCoord);
			for (ItemStack i : required)
				packet.writeItemStack(i);
			packet.sendToServer();
		}
	}
	
	@Override
	public void addWidgets() {
		GuiCCButton widget = new GuiCCButton(220, 45, 70, 18, "Register");
		widget.setActionCommand("register");
		this.add(widget);
	}
	
}
