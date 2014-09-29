package org.altervista.whovian.client.renderer.gui;

import org.altervista.whovian.client.renderer.container.ContainerTank;
import org.altervista.whovian.tileentity.TileEntityTank;
import org.altervista.whovian.xyreloaded.XYreloaded;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GUItank extends GuiContainer {
	
	public final ResourceLocation texture = new ResourceLocation(XYreloaded.MODID, "textures/gui/tank.png");
	private TileEntityTank tank;
	
	public GUItank(InventoryPlayer invP, TileEntityTank valve) {
		super(new ContainerTank(invP, valve));
		this.xSize = 176;
		this.ySize = 166;
		this.tank = valve;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		drawTexturedModalRect(guiLeft+23, guiTop+11, 0, 166, 54, 60);
		int filled = (int)Math.floor((double)tank.tank.getFluidAmount()*59/tank.tank.getCapacity());
		if (tank.tank.getFluid()!=null) {
			IIcon icon = tank.tank.getFluid().getFluid().getIcon();
			Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
			double x=guiLeft+24; double y=guiTop+11+60-filled; double width=52; double height=filled; double z=this.zLevel;
		    double iconWidthStep = (icon.getMaxU() - icon.getMinU()) / 16.0D;
		    double iconHeightStep = (icon.getMaxV() - icon.getMinV()) / 16.0D;
		    
		    Tessellator tessellator = Tessellator.instance;
		    tessellator.startDrawingQuads();
		    for (double cy = y; cy < y + height; cy += 16.0D)
		    {
		      double quadHeight = Math.min(16.0D, height + y - cy);
		      double maxY = cy + quadHeight;
		      double maxV = icon.getMinV() + iconHeightStep * quadHeight;
		      for (double cx = x; cx < x + width; cx += 16.0D)
		      {
		        double quadWidth = Math.min(16.0D, width + x - cx);
		        double maxX = cx + quadWidth;
		        double maxU = icon.getMinU() + iconWidthStep * quadWidth;
		        
		        tessellator.addVertexWithUV(cx, maxY, z, icon.getMinU(), maxV);
		        tessellator.addVertexWithUV(maxX, maxY, z, maxU, maxV);
		        tessellator.addVertexWithUV(maxX, cy, z, maxU, icon.getMinV());
		        tessellator.addVertexWithUV(cx, cy, z, icon.getMinU(), icon.getMinV());
		      }
		    }
		    tessellator.draw();
		}
	    
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		
		drawTexturedModalRect(guiLeft+24, guiTop+11, 54, 166, 54, 60);
		//GuiTooltip.drawAreaTooltip(200, 200, "Test", 73, 23, 83, 71);
	}

}
