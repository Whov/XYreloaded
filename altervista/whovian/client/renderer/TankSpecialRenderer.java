package org.altervista.whovian.client.renderer;

import java.util.Iterator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.altervista.whovian.xyreloaded.XYreloaded;
import org.altervista.whovian.tileentity.TileEntityTank;
import org.lwjgl.opengl.GL11;

public class TankSpecialRenderer extends TileEntitySpecialRenderer {

	private final ResourceLocation srcLine = new ResourceLocation(XYreloaded.MODID, "textures/model/tank.png");
	
	private int TextureWidth = 64;
	private int TextureHeight = 64;
	
	@Override
	public void renderTileEntityAt(TileEntity tile2, double x, double y, double z, float f) {
		TileEntityTank tile = (TileEntityTank)tile2;
		if (tile.tank==null) return;
		if (tile.tank.getCapacity()==0) return;
		int amount = tile.tank.getFluidAmount();
		double remainingFluid = (double) amount;
		int L = tile.layers.get(-1);
		while (remainingFluid-tile.layers.get(L)*8000>0) {
			remainingFluid-=tile.layers.get(L)*8000;
			L++;
		}
		remainingFluid = remainingFluid/tile.layers.get(L)/8000;
		int dx; int dy; int dz;
		int[] curr;
		for (int times=0;times<tile.checked.size();times++) {
			curr = ((int[])tile.checked.get(times));
			dx = curr[0]-tile.xCoord;
			dy = curr[1]-tile.yCoord;
			dz = curr[2]-tile.zCoord;
		    GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_LIGHTING);
		    GL11.glTranslated(x+dx, y+dy, z+dz);
		    Tessellator tessellator = Tessellator.instance;
		    tessellator.startDrawingQuads();
		    tessellator.setBrightness(15728880);
		    if (tile.tank.getFluidAmount()>0&&tile2.getWorldObj().getBlock(curr[0], curr[1], curr[2]).equals(XYreloaded.blockInTank)&&tile2.getWorldObj().getBlockMetadata(curr[0], curr[1], curr[2])==3) {
			    if (curr[1]==L) drawFluid(tessellator, tile, remainingFluid);
			    else if (curr[1]<L) drawFluid(tessellator, tile, 1); 
		    }
		    else drawLines(tessellator);
		    tessellator.draw();
			GL11.glEnable(GL11.GL_LIGHTING);
		    GL11.glPopMatrix();
		}
	}
	
	private void drawFluid(Tessellator tessellator, TileEntityTank tank, double height) {
		IIcon icon = tank.tank.getFluid().getFluid().getIcon();
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		
        tessellator.addVertexWithUV(0, height, 1, icon.getMaxU(), icon.getMaxV());
        tessellator.addVertexWithUV(1, height, 1, icon.getMinU(), icon.getMaxV());
        tessellator.addVertexWithUV(1, height, 0, icon.getMinU(), icon.getMinV());
        tessellator.addVertexWithUV(0, height, 0, icon.getMaxU(), icon.getMinV());
        
        tessellator.addVertexWithUV(0, height, 0, icon.getMaxU(), icon.getMaxV());
        tessellator.addVertexWithUV(1, height, 0, icon.getMinU(), icon.getMaxV());
        tessellator.addVertexWithUV(1, 0, 0, icon.getMinU(), icon.getMinV());
        tessellator.addVertexWithUV(0, 0, 0, icon.getMaxU(), icon.getMinV());

        tessellator.addVertexWithUV(1, height, 1, icon.getMaxU(), icon.getMaxV());
        tessellator.addVertexWithUV(0, height, 1, icon.getMinU(), icon.getMaxV());
        tessellator.addVertexWithUV(0, 0, 1, icon.getMinU(), icon.getMinV());
        tessellator.addVertexWithUV(1, 0, 1, icon.getMaxU(), icon.getMinV());

        tessellator.addVertexWithUV(1, height, 0, icon.getMaxU(), icon.getMaxV());
        tessellator.addVertexWithUV(1, height, 1, icon.getMinU(), icon.getMaxV());
        tessellator.addVertexWithUV(1, 0, 1, icon.getMinU(), icon.getMinV());
        tessellator.addVertexWithUV(1, 0, 0, icon.getMaxU(), icon.getMinV());

        tessellator.addVertexWithUV(0, height, 1, icon.getMaxU(), icon.getMaxV());
        tessellator.addVertexWithUV(0, height, 0, icon.getMinU(), icon.getMaxV());
        tessellator.addVertexWithUV(0, 0, 0, icon.getMinU(), icon.getMinV());
        tessellator.addVertexWithUV(0, 0, 1, icon.getMaxU(), icon.getMinV());

	}

	private void drawLines(Tessellator tessellator) {
	    this.bindTexture(srcLine);
		tessellator.addVertexWithUV(0, 0, -0.01, 0, 0);
	    tessellator.addVertexWithUV(0, 1, -0.01, 0, 1);
	    tessellator.addVertexWithUV(1, 1, -0.01, 1, 1);
	    tessellator.addVertexWithUV(1, 0, -0.01, 1, 0);

	    tessellator.addVertexWithUV(-0.01, 0, 1, 0, 0);
	    tessellator.addVertexWithUV(-0.01, 1, 1, 0, 1);
	    tessellator.addVertexWithUV(-0.01, 1, 0, 1, 1);
	    tessellator.addVertexWithUV(-0.01, 0, 0, 1, 0);

	    tessellator.addVertexWithUV(1, 0, 1.01, 0, 0);
	    tessellator.addVertexWithUV(1, 1, 1.01, 0, 1);
	    tessellator.addVertexWithUV(0, 1, 1.01, 1, 1);
	    tessellator.addVertexWithUV(0, 0, 1.01, 1, 0);

	    tessellator.addVertexWithUV(1.01, 0, 0, 0, 0);
	    tessellator.addVertexWithUV(1.01, 1, 0, 0, 1);
	    tessellator.addVertexWithUV(1.01, 1, 1, 1, 1);
	    tessellator.addVertexWithUV(1.01, 0, 1, 1, 0);

	    tessellator.addVertexWithUV(0, -0.01, 0, 0, 0);
	    tessellator.addVertexWithUV(1, -0.01, 0, 0, 1);
	    tessellator.addVertexWithUV(1, -0.01, 1, 1, 1);
	    tessellator.addVertexWithUV(0, -0.01, 1, 1, 0);

	    tessellator.addVertexWithUV(0, 1.01, 1, 0, 0);
	    tessellator.addVertexWithUV(1, 1.01, 1, 0, 1);
	    tessellator.addVertexWithUV(1, 1.01, 0, 1, 1);
	    tessellator.addVertexWithUV(0, 1.01, 0, 1, 0);
	}
	
}
