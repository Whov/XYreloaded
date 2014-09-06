package org.altervista.whovian.client.renderer.gui;

import net.minecraft.util.ResourceLocation;

import org.altervista.whovian.blocks.Fabricator;
import org.altervista.whovian.tileentity.TileEntityFabricator;

import codechicken.core.gui.GuiCCButton;
import codechicken.core.gui.GuiScreenWidget;

public class GUIfabricatorConfig extends GuiScreenWidget {

	TileEntityFabricator tile;
	ResourceLocation background = new ResourceLocation("textures/gui/demo_background.png");
	
	public GUIfabricatorConfig(TileEntityFabricator t) {
		super(247, 165);
		tile=t;
	}
	
	@Override
	public void drawBackground() {
		mc.renderEngine.bindTexture(background);
		drawTexturedModalRect(0, 0, 0, 0, 247, 165);
	}
	
	@Override
	public void actionPerformed(String ident, Object... params) {
		if (ident=="dismantle") {
			((Fabricator)tile.getWorldObj().getBlock(tile.xCoord, tile.yCoord, tile.zCoord)).breakMultiBlock(tile.getWorldObj(), tile);
			this.mc.thePlayer.closeScreen();
		}
	}
	
	@Override
	public void addWidgets() {
		GuiCCButton widget = new GuiCCButton(30, (height-ySize)/2, 199, 20, "Dismantle multiblock");
		widget.setActionCommand("dismantle");
		this.add(widget);
	}
	
}
