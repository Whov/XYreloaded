package org.altervista.whovian.client.renderer.gui;

import org.altervista.whovian.client.renderer.container.ContainerFabricator;
import org.altervista.whovian.tileentity.TileEntityFabricator;
import org.altervista.whovian.xyreloaded.XYreloaded;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GUIfabricator extends GuiContainer {

	public final ResourceLocation texture = new ResourceLocation("textures/gui/container/generic_54.png");
	public final ResourceLocation misc = new ResourceLocation(XYreloaded.MODID, "textures/gui/misc.png");
	private int inventoryRows;
	private TileEntityFabricator fab;
	
	public GUIfabricator(InventoryPlayer invP, TileEntityFabricator fab) {
		super(new ContainerFabricator(invP, fab.getMaster()));
		fab = fab.getMaster();
        this.inventoryRows = fab.getSizeInventory() / 9;
        this.xSize=176; this.ySize=222;
	}

	@Override
	public void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, this.ySize);
		Minecraft.getMinecraft().getTextureManager().bindTexture(misc);
		//closes the inaccessible slots
		for (int y=6;y>inventoryRows;y--) { for (int x=0;x<9;x++) {
			this.drawTexturedModalRect(guiLeft+9+18*x, guiTop+18*y+1, 0, 0, 14, 14);
		}}
    }		
}
