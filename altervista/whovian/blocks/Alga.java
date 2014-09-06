package org.altervista.whovian.blocks;

import org.altervista.whovian.client.proxy.ClientProxy;
import org.altervista.whovian.xyreloaded.XYreloaded;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class Alga extends Block {

	public Alga() {
		super(Material.water);
		this.setBlockName("alga");
        this.setBlockTextureName(XYreloaded.MODID+":alga");
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		if (world.getBlockMetadata(x, y, z)==0)
			setBlockBounds(0f, 0f, 0.4f, 1f, 1f, 0.6f);
		else
			setBlockBounds(0.4f, 0f, 0f, 0.6f, 1f, 1f);
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return world.getBlock(x, y, z).getMaterial()==Material.water&&world.getBlock(x, y+1, z).getMaterial()==Material.water&&world.getBlock(x, y+2, z).getMaterial()==Material.water&&world.getBlock(x, y+3, z).getMaterial()==Material.water&&world.getBlock(x, y+4, z).getMaterial()==Material.water&&world.getBlock(x, y-1, z).getMaterial().isSolid();
    }
	
	@Override
	public int getRenderType() {
		return ClientProxy.AlgaRenderType;
	}
	
	@Override
	public boolean isOpaqueCube() {
        return false;
    }
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

}
