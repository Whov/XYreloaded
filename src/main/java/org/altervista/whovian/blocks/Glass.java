package org.altervista.whovian.blocks;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import org.altervista.whovian.client.proxy.ClientProxy;
import org.altervista.whovian.tileentity.TileEntityDummyTank;
import org.altervista.whovian.tileentity.TileEntityTank;
import org.altervista.whovian.xyreloaded.XYreloaded;
import org.altervista.whovian.xyreloaded.Tricks.ItankValid;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Glass extends ConnectedTextures implements ItankValid {

	public Glass(String name, String path) {
		super(Material.rock, path, XYreloaded.MODID);
		this.setBlockName(name);
		this.setCreativeTab(XYreloaded.XYcraftTab);
		setLightOpacity(0);
		this.Falone=0;
		this.Fjust1=1;
		this.Fcorner=5;
		this.FinLine=9;
	}

	@Override
	public int getRenderType() {
		return ClientProxy.MultiBlockRenderType;
	}
	
	public void BreakMultiBlockStructure(World world, int x, int y, int z) {
		Block block;
		for (int x2=-1;x2<2;x2++) {for (int y2=-1;y2<2;y2++) {for (int z2=-1;z2<2;z2++) {//check the 3x3
			block = world.getBlock(x+x2, y+y2, z+z2);
			if (block.equals(XYreloaded.blockInTank)) {
				((blockInTank)block).BreakMultiBlockStructure(world, x+x2, y+y2, z+z2, new ArrayList());
				return;
			}
		}}}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xx, float yy, float zz) {
		System.out.println(world.getBlockMetadata(x, y, z));
		if (world.getBlockMetadata(x, y, z)%2==0||player.isSneaking()) return false;
		if (world.isRemote) return true;
		TileEntityTank tile = null;
		search:
		for (int x2=-1;x2<2;x2++) {for (int y2=-1;y2<2;y2++) {for (int z2=-1;z2<2;z2++) {//controlla nel 3x3
			if (world.getBlock(x+x2, y+y2, z+z2).equals(XYreloaded.blockInTank)&&world.getBlockMetadata(x+x2, y+y2, z+z2)>0) {
				tile = ((TileEntityTank)((TileEntityDummyTank)world.getTileEntity(x+x2, y+y2, z+z2)).tile);
				break search;
			}
		}}}
		if (tile==null) return false;
		FMLNetworkHandler.openGui(player, XYreloaded.instance, XYreloaded.guiTank, world, tile.xCoord, tile.yCoord, tile.zCoord);
		return true;
	}

	@Override
	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta) {
		if (world.isRemote) return;
		if (meta%2==1) BreakMultiBlockStructure(world, x, y, z);
	}
	
	@Override
	public boolean isOpaqueCube() {return false;}
	
	@Override
	public boolean renderAsNormalBlock() {
        return false;
    }	
	
}
