package org.altervista.whovian.client.renderer;

import org.altervista.whovian.client.proxy.ClientProxy;
import org.altervista.whovian.xyreloaded.XYreloaded;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class AlgaRenderer implements ISimpleBlockRenderingHandler {

	private static IIcon iiconWater = BlockLiquid.getLiquidIcon("water_still");
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		boolean renderFromInside = renderer.renderFromInside;
		renderer.renderFromInside=true;
		if (world.getBlockMetadata(x, y, z)==1) {
			renderer.renderFaceXNeg(XYreloaded.algaBlock, x+0.1, y, z, block.getIcon(0, 0));
			renderer.renderFaceXPos(XYreloaded.algaBlock, x-0.1, y, z, block.getIcon(0, 0));
		} else {
			renderer.renderFaceZNeg(XYreloaded.algaBlock, x, y, z+0.1, block.getIcon(0, 0));
			renderer.renderFaceZPos(XYreloaded.algaBlock, x, y, z-0.1, block.getIcon(0, 0));
		}
		renderer.renderFromInside = renderFromInside;
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}

	@Override
	public int getRenderId() {
		return ClientProxy.AlgaRenderType;
	}

}
