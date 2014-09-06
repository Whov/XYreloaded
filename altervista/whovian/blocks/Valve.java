package org.altervista.whovian.blocks;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.altervista.whovian.tileentity.TileEntityTank;
import org.altervista.whovian.xyreloaded.XYreloaded;

public class Valve extends BlockContainer implements ITileEntityProvider {
	
	public Valve() {
		super(Material.iron);
		this.setBlockName("blockValve");
		this.setCreativeTab(XYreloaded.XYcraftTab);
		this.setHardness(5.0f);
		this.setBlockTextureName(XYreloaded.MODID+":"+"valve");
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float x2, float y2, float z2) {
		if (!world.isRemote&&world.getBlockMetadata(x, y, z)>0) {
			int[] loc = ((TileEntityTank)world.getTileEntity(x, y, z)).active;
			FMLNetworkHandler.openGui(player, XYreloaded.instance, XYreloaded.guiTank, world, loc[0], loc[1], loc[2]);
			return true;
		}
		if (world.getBlockMetadata(x, y, z)!=0) return true;
        switch (side) {
        	case 0:
        		if (!(world.getBlock(x, y+1, z) instanceof blockInTank)) return false;
        		if (((blockInTank)world.getBlock(x, y+1, z)).FormMultiBlockStructure(world, x, y+1, z, ((TileEntityTank)world.getTileEntity(x, y, z)))) return true; break;
        	case 1:
        		if (!(world.getBlock(x, y-1, z)==XYreloaded.blockInTank)) return false;
        		if (((blockInTank)world.getBlock(x, y-1, z)).FormMultiBlockStructure(world, x, y-1, z, ((TileEntityTank)world.getTileEntity(x, y, z)))) return true; break;
        	case 2:
        		if (!(world.getBlock(x, y, z+1)==XYreloaded.blockInTank)) return false;
        		if (((blockInTank)world.getBlock(x, y, z+1)).FormMultiBlockStructure(world, x, y, z+1, ((TileEntityTank)world.getTileEntity(x, y, z)))) return true; break;
        	case 3:
        		if (!(world.getBlock(x, y, z-1)==XYreloaded.blockInTank)) return false;
        		if (((blockInTank)world.getBlock(x, y, z-1)).FormMultiBlockStructure(world, x, y, z-1, ((TileEntityTank)world.getTileEntity(x, y, z)))) return true; break;
        	case 4:
        		if (!(world.getBlock(x+1, y, z)==XYreloaded.blockInTank)) return false;
        		if (((blockInTank)world.getBlock(x+1, y, z)).FormMultiBlockStructure(world, x+1, y, z, ((TileEntityTank)world.getTileEntity(x, y, z)))) return true; break;
        	case 5:
        		if (!(world.getBlock(x-1, y, z)==XYreloaded.blockInTank)) return false;
        		if (((blockInTank)world.getBlock(x-1, y, z)).FormMultiBlockStructure(world, x-1, y, z, ((TileEntityTank)world.getTileEntity(x, y, z)))) return true; break;
        }
        return true;
    }

	@Override
	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta) {
		if (world.isRemote) return;
		if (meta>0) {
			for (int x2=-1;x2<2;x2++) {for (int y2=-1;y2<2;y2++) {for (int z2=-1;z2<2;z2++) {//controlla nel 3x3
				Block block = world.getBlock(x+x2, y+y2, z+z2);
				if (block.equals(XYreloaded.blockInTank)) {
					((blockInTank)block).BreakMultiBlockStructure(world, x+x2, y+y2, z+z2);
					return;
				}
			}}}
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileEntityTank();
    }
	
}
