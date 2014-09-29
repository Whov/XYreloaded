package org.altervista.whovian.blocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.altervista.whovian.client.proxy.ClientProxy;
import org.altervista.whovian.tileentity.TileEntityDummyTank;
import org.altervista.whovian.tileentity.TileEntityTank;
import org.altervista.whovian.xyreloaded.XYreloaded;
import org.altervista.whovian.xyreloaded.Tricks.ItankValid;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
/*
fancyblock metadata usage:
0 e pari: normali (con luce in varie gradazioni da 2 in sù)
dispari: multitank con livello di luce pari a disp-1
*/
public class FancyBlock extends Block implements ItankValid {
	
	public FancyBlock(String name) {
		super(Material.rock);
		this.setBlockName(name);
		this.setCreativeTab(XYreloaded.XYcraftTab);
		this.setHardness(3.0f);
		this.setBlockTextureName(XYreloaded.MODID+":"+name);
		//getBlockBrightness
	}
	
	@Override
	public int getRenderType() {
		return ClientProxy.MultiBlockRenderType;
	}
	
	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z);
		meta = meta-meta%2;
		this.setLightLevel(meta/14);
	    return meta;
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
		if (player.isSneaking()||world.getBlockMetadata(x, y, z)%2==0) return false;
		if (world.isRemote) return true;
		TileEntityTank tile = null;
		search:
		for (int x2=-1;x2<2;x2++) {for (int y2=-1;y2<2;y2++) {for (int z2=-1;z2<2;z2++) {//checks the 3x3
			if (world.getBlock(x+x2, y+y2, z+z2).equals(XYreloaded.blockInTank)&&world.getBlockMetadata(x+x2, y+y2, z+z2)>0) {
				tile = ((TileEntityTank)((TileEntityDummyTank)world.getTileEntity(x+x2, y+y2, z+z2)).tile);
				break search;
			}
		}}}
		if (tile==null) System.out.println("AA");
		if (tile==null) return false;
		FMLNetworkHandler.openGui(player, XYreloaded.instance, XYreloaded.guiTank, world, tile.xCoord, tile.yCoord, tile.zCoord);
		return true;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		if (world.isRemote) return;
		if (meta%2==1) BreakMultiBlockStructure(world, x, y, z);
	}
	
	@Override
	public int damageDropped(int meta)
    {
        return meta-meta%2;
    }
	
}
