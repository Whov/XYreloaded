package org.altervista.whovian.client.renderer;

import org.altervista.whovian.blocks.Fabricator;
import org.altervista.whovian.blocks.Valve;
import org.altervista.whovian.client.proxy.ClientProxy;
import org.altervista.whovian.xyreloaded.XYreloaded;
import org.altervista.whovian.xyreloaded.Tricks.ItankValid;
import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class MultiBlockCrossRenderer implements ISimpleBlockRenderingHandler {
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
        texturemanager.bindTexture(texturemanager.getResourceLocation(0));
        Tessellator tessellator = Tessellator.instance;
        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, 0));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, 0));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, 0));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, 0));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, 0));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, 0));
        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		renderer.renderStandardBlock(block, x, y, z);
		int meta = world.getBlockMetadata(x, y, z);
		IIcon multi;
		if (block instanceof ItankValid&&meta%2==1) multi = Valve.multi;
		else if (block.equals(XYreloaded.fabricator)&&meta>0) multi = ((Fabricator)block).multi;
		else return false;
		renderer.renderFaceXNeg(block, x-0.01, y, z, (IIcon) multi);
		renderer.renderFaceXPos(block, x+0.01, y, z, (IIcon) multi);
		renderer.renderFaceYNeg(block, x, y-0.01, z, (IIcon) multi);
		renderer.renderFaceYPos(block, x, y+0.01, z, (IIcon) multi);
		renderer.renderFaceZNeg(block, x, y, z-0.01, (IIcon) multi);
		renderer.renderFaceZPos(block, x, y, z+0.01, (IIcon) multi);
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return ClientProxy.MultiBlockRenderType;
	}

}
