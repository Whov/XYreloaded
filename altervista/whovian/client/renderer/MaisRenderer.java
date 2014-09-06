package org.altervista.whovian.client.renderer;

import org.altervista.whovian.client.proxy.ClientProxy;
import org.altervista.whovian.xyreloaded.XYreloaded;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class MaisRenderer implements ISimpleBlockRenderingHandler {

	private static IIcon iiconWater = BlockLiquid.getLiquidIcon("water_still");
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		if (ClientProxy.renderPass==0)
			renderer.renderCrossedSquares(XYreloaded.riceBlock, x, y, z);
		else {
			renderer.renderFaceYPos(XYreloaded.riceBlock, x, y+0.2, z, iiconWater);
		}
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}

	@Override
	public int getRenderId() {
		return ClientProxy.MaisRenderType;
	}

}
