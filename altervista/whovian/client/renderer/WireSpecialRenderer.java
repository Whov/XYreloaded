package org.altervista.whovian.client.renderer;

import org.altervista.whovian.multipart.WirePart;
import org.altervista.whovian.tileentity.TileEntityWire;
import org.altervista.whovian.xyreloaded.XYreloaded;
import org.lwjgl.opengl.GL11;

import codechicken.multipart.TileMultipart;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class WireSpecialRenderer extends TileEntitySpecialRenderer {

	private final ResourceLocation srcLine = new ResourceLocation(XYreloaded.MODID, "textures/model/wire.png");
	
	@Override
	public void renderTileEntityAt(TileEntity tile2, double x, double y, double z, float f) {
		World world = tile2.getWorldObj();
		int X = tile2.xCoord; int Y = tile2.yCoord; int Z = tile2.zCoord;
		int rd;
		int[] connections;
		int side;
		if (f>=1) {
			f-=1;
			TileMultipart tile = (TileMultipart) tile2;
			WirePart w = (WirePart) tile.partMap((int)f);
			rd = w.rdLevel;
			connections= w.connections;
			side = w.sideForMeta(w.meta);
		} else {
			rd = ((TileEntityWire)tile2).rdLevel;
			connections = ((TileEntityWire)tile2).connections;
			side = ForgeDirection.OPPOSITES[tile2.getWorldObj().getBlockMetadata(tile2.xCoord, tile2.yCoord, tile2.zCoord)];
		}
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
	    GL11.glTranslated(x, y, z);
	    Tessellator tessellator = Tessellator.instance;
	    tessellator.startDrawingQuads();
	    tessellator.setBrightness(15728880);
	    this.bindTexture(srcLine);
	    double u = 1.0/3;
	    double u2 = 2.0/3;
	    float rdScaled = 1- (float) (rd/16.0);
	    tessellator.setColorOpaque_F(1, rdScaled, rdScaled);
	    ForgeDirection dir = ForgeDirection.getOrientation(world.getBlockMetadata(X, Y, Z)).getOpposite();
	    if (world.getBlock(X+dir.offsetX, Y+dir.offsetY, Z+dir.offsetZ).equals(XYreloaded.blockWire)) {
	    	switch (dir.ordinal()) {
	    	case 0:
	    		renderSide0(0, u/2, u, u2, u/2);
	    		renderSide4(-u, u/2, u, u2, u/2);
	    		renderSide2(0, u/2, -u, u/2, u2);
	    		renderSide3(0, u/2, -u, u/2, u);
	    		break;
	    	case 1:
	    		renderSide1(0, u/2, u, u2, 2.5*u);
	    		renderSide4(2.5*u, u*4, u, u2, u/2);
	    		renderSide2(0, u/2, 2.5*u, u*4, u2);
	    		renderSide3(0, u/2, 2.5*u, u*4, u);
	    		break;
	    	}
	    } else {
		    switch (side) {
		    case 0:
		    	renderSide0(u, 1-u, u, 1-u, u/2);
		    	if (connections[2]==1) {
		    		renderSide0(u, 1-u, 0, u, u/2);
		    		renderSide4(0, u/2, 0, u, u2);
		    		renderSide5(0, u/2, 0, u, u);
		    	} else renderSide3(u, u2, 0, u/2, u);
		    	if (connections[3]==1) {
		    		renderSide0(u, 1-u, u2, 1, u/2);
		    		renderSide4(0, u/2, u2, 1, u2);
		    		renderSide5(0, u/2, u2, 1, u);
		    	} else renderSide2(u, u2, 0, u/2, u2);
		    	if (connections[4]==1) {
		    		renderSide0(0, u, u, 1-u, u/2);
		    		renderSide2(0, u, 0, u/2, u2);
		    		renderSide3(0, u, 0, u/2, u);
		    	} else renderSide5(0, u/2, u, u2, u);
		    	if (connections[5]==1) {
		    		renderSide0(u2, 1, u, 1-u, u/2);
		    		renderSide2(u2, 1, 0, u/2, u2);
		    		renderSide3(u2, 1, 0, u/2, u);
		    	} else renderSide4(0, u/2, u, u2, u2);
		    	break;	
		    case 1:
		    	renderSide1(u, 1-u, u, 1-u, 1-u/2);
		    	if (connections[2]==1) {
		    		renderSide1(u, 1-u, 0, u, 1-u/2);
		    		renderSide4(2.5*u, 1, 0, u, u2);
		    		renderSide5(2.5*u, 1, 0, u, u);
		    	} else renderSide3(u, u2, 2.5*u, 1, u);
		    	if (connections[3]==1) {
		    		renderSide1(u, 1-u, u2, 1, 1-u/2);
		    		renderSide4(2.5*u, 1, u2, 1, u2);
		    		renderSide5(2.5*u, 1, u2, 1, u);
		    	} else renderSide2(u, u2, 2.5*u, 1, u2);
		    	if (connections[4]==1) {
		    		renderSide1(0, u, u, 1-u, 1-u/2);
		    		renderSide2(0, u, 2.5*u, 1, u2);
		    		renderSide3(0, u, 2.5*u, 1, u);
		    	} else renderSide5(2.5*u, 1, u, u2, u);
		    	if (connections[5]==1) {
		    		renderSide1(u2, 1, u, 1-u, 1-u/2);
		    		renderSide2(u2, 1, 2.5*u, 1, u2);
		    		renderSide3(u2, 1, 2.5*u, 1, u);
		    	} else renderSide4(2.5*u, 1, u, u2, u2);
		    	break;
		    case 2:
		    	renderSide2(u, 1-u, u, 1-u, u/2);
		    	if (connections[4]==1) {
		    		renderSide2(0, u, u, 1-u, u/2);
		    		renderSide0(0, u, 0, u/2, u2);
		    		renderSide1(0, u, 0, u/2, u);
		    	} else renderSide5(u, u2, 0, u/2, u);
		    	if (connections[5]==1) {
		    		renderSide2(u2, 1, u, 1-u, u/2);
		    		renderSide0(u2, 1, 0, u/2, u2);
		    		renderSide1(u2, 1, 0, u/2, u);
		    	} else renderSide4(u, u2, 0, u/2, u2);
		    	if (connections[0]==1) {
		    		renderSide2(u, u2, 0, u, u/2);
		    		renderSide4(0, u, 0, u/2, u2);
		    		renderSide5(0, u, 0, u/2, u);
		    	} else renderSide1(u, u2, 0, u/2, u);
		    	if (connections[1]==1) {
		    		renderSide2(u, u2, u2, 1, u/2);
		    		renderSide4(u2, 1, 0, u/2, u2);
		    		renderSide5(u2, 1, 0, u/2, u);
		    	} else renderSide0(u, u2, 0, u/2, u2);
		    	break;
		    case 3:
		    	renderSide3(u, 1-u, u, 1-u, 1-u/2);
		    	if (connections[4]==1) {
		    		renderSide3(0, u, u, 1-u, 1-u/2);
		    		renderSide0(0, u, 2.5*u, 1, u2);
		    		renderSide1(0, u, 2.5*u, 1, u);
		    	} else renderSide5(u, u2, 2.5*u, 1, u);
		    	if (connections[5]==1) {
		    		renderSide3(u2, 1, u, 1-u, 1-u/2);
		    		renderSide0(u2, 1, 2.5*u, 1, u2);
		    		renderSide1(u2, 1, 2.5*u, 1, u);
		    	} else renderSide4(u, u2, 2.5*u, 1, u2);
		    	if (connections[0]==1) {
		    		renderSide3(u, u2, 0, u, 1-u/2);
		    		renderSide4(0, u, 2.5*u, 1, u2);
		    		renderSide5(0, u, 2.5*u, 1, u);
		    	} else renderSide1(u, u2, 2.5*u, 1, u);
		    	if (connections[1]==1) {
		    		renderSide3(u, u2, u2, 1, 1-u/2);
		    		renderSide4(u2, 1, 2.5*u, 1, u2);
		    		renderSide5(u2, 1, 2.5*u, 1, u);
		    	} else renderSide0(u, u2, 2.5*u, 1, u2);
		    	break;
		    case 4:
		    	renderSide4(u, 1-u, u, 1-u, u/2);
		    	if (connections[2]==1) {
		    		renderSide4(u, u2, 0, u, u/2);
		    		renderSide0(0, u/2, 0, u, u2);
		    		renderSide1(0, u/2, 0, u, u);
		    	} else renderSide3(0, u/2, u, u2, u);
		    	if (connections[3]==1) {
		    		renderSide4(u, u2, u2, 1, u/2);
		    		renderSide0(0, u/2, u2, 1, u2);
		    		renderSide1(0, u/2, u2, 1, u);
		    	} else renderSide2(0, u/2, u, u2, u2);
		    	if (connections[0]==1) {
		    		renderSide4(0, u, u, u2, u/2);
		    		renderSide2(0, u/2, 0, u, u2);
		    		renderSide3(0, u/2, 0, u, u);
		    	} else renderSide1(0, u/2, u, u2, u);
		    	if (connections[1]==1) {
		    		renderSide4(u2, 1, u, u2, u/2);
		    		renderSide2(0, u/2, u2, 1, u2);
		    		renderSide3(0, u/2, u2, 1, u);
		    	} else renderSide0(0, u/2, u, u2, u2);
		    	break;
		    case 5:
		    	renderSide5(u, 1-u, u, 1-u, 1-u/2);
		    	if (connections[2]==1) {
		    		renderSide5(u, 1-u, 0, u, 1-u/2);
		    		renderSide0(2.5*u, 1, 0, u, u2);
		    		renderSide1(2.5*u, 1, 0, u, u);
		    	} else renderSide3(2.5*u, 1, u, u2, u);
		    	if (connections[3]==1) {
		    		renderSide5(u, 1-u, u2, 1, 1-u/2);
		    		renderSide0(2.5*u, 1, u2, 1, u2);
		    		renderSide1(2.5*u, 1, u2, 1, u);
		    	} else renderSide2(2.5*u, 1, u, u2, u2);
		    	if (connections[0]==1) {
		    		renderSide5(0, u, u, 1-u, 1-u/2);
		    		renderSide2(2.5*u, 1, 0, u, u2);
		    		renderSide3(2.5*u, 1, 0, u, u);
		    	} else renderSide1(2.5*u, 1, u, u2, u);
		    	if (connections[1]==1) {
		    		renderSide5(u2, 1, u, 1-u, 1-u/2);
		    		renderSide2(2.5*u, 1, u2, 1, u2);
		    		renderSide3(2.5*u, 1, u2, 1, u);
		    	} else renderSide0(2.5*u, 1, u, u2, u2);
		    	break;
		    }
	    }
	    tessellator.draw();
		GL11.glEnable(GL11.GL_LIGHTING);
	    GL11.glPopMatrix();
	}

	private void renderSide0(double x0, double x1, double z0, double z1, double y) {
		Tessellator tessellator = Tessellator.instance;
	    tessellator.addVertexWithUV(x1, y, z1, 1, 1);
	    tessellator.addVertexWithUV(x1, y, z0, 1, 0);
	    tessellator.addVertexWithUV(x0, y, z0, 0, 0);
		tessellator.addVertexWithUV(x0, y, z1, 0, 1);
	}

	private void renderSide1(double x0, double x1, double z0, double z1, double y) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.addVertexWithUV(x0, y, z1, 0, 1);
	    tessellator.addVertexWithUV(x0, y, z0, 0, 0);
	    tessellator.addVertexWithUV(x1, y, z0, 1, 0);
	    tessellator.addVertexWithUV(x1, y, z1, 1, 1);
	}

	private void renderSide2(double x0, double x1, double y0, double y1, double z) {
		Tessellator tessellator = Tessellator.instance;
	    tessellator.addVertexWithUV(x0, y1, z, 0, 1);
	    tessellator.addVertexWithUV(x0, y0, z, 0, 0);
	    tessellator.addVertexWithUV(x1, y0, z, 1, 0);
		tessellator.addVertexWithUV(x1, y1, z, 1, 1);
	}

	private void renderSide3(double x0, double x1, double y0, double y1, double z) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.addVertexWithUV(x1, y1, z, 1, 1);
	    tessellator.addVertexWithUV(x1, y0, z, 1, 0);
	    tessellator.addVertexWithUV(x0, y0, z, 0, 0);
	    tessellator.addVertexWithUV(x0, y1, z, 0, 1);
	}

	private void renderSide4(double y0, double y1, double z0, double z1, double x) {
		Tessellator tessellator = Tessellator.instance;
	    tessellator.addVertexWithUV(x, y0, z1, 0, 1);
	    tessellator.addVertexWithUV(x, y0, z0, 0, 0);
	    tessellator.addVertexWithUV(x, y1, z0, 1, 0);
		tessellator.addVertexWithUV(x, y1, z1, 1, 1);
	}

	private void renderSide5(double y0, double y1, double z0, double z1, double x) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.addVertexWithUV(x, y1, z1, 1, 1);
	    tessellator.addVertexWithUV(x, y1, z0, 1, 0);
	    tessellator.addVertexWithUV(x, y0, z0, 0, 0);
	    tessellator.addVertexWithUV(x, y0, z1, 0, 1);
	}
}
