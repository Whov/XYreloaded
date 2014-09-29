package org.altervista.whovian.blocks;

import java.util.Random;

import org.altervista.whovian.client.proxy.ClientProxy;
import org.altervista.whovian.tileentity.TileEntityFabricator;
import org.altervista.whovian.xyreloaded.XYreloaded;

import codechicken.lib.vec.BlockCoord;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class Fabricator extends ConnectedTextures implements ITileEntityProvider {

	@SideOnly(Side.CLIENT)
	private IIcon[] icons;
	public static IIcon multi;
	
	public Fabricator() {
		super(Material.iron, "fabricator/fabricator_", XYreloaded.MODID);
		this.setBlockName("fabricator");
		this.setCreativeTab(XYreloaded.XYcraftTab);
		this.setHardness(2.0f);
		this.Falone=0;
		this.Fedge3=1;
		this.Fcorner=5;
		this.Fsurrounded=9;
	}
	
	@Override
	public int getRenderType() {
		return ClientProxy.MultiBlockRenderType;
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float x2, float y2, float z2) {
		TileEntityFabricator tile = getTile(world, x, y, z, true);
		if (player.isSneaking()) {
			FMLNetworkHandler.openGui(player, XYreloaded.instance, XYreloaded.guiFab2, world, x, y, z);
		} else {
			FMLNetworkHandler.openGui(player, XYreloaded.instance, XYreloaded.guiFab, world, tile.xCoord, tile.yCoord, tile.zCoord);
		}
		return true;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		Random rand = new Random();
		TileEntityFabricator tile = getTile(world, x, y, z, true);
		if (tile==null) return;
		if (tile.Slots!=null) {
			for (ItemStack itemstack:tile.Slots) {
				if (itemstack!=null) {
					float f = rand.nextFloat() * 0.8F + 0.1F;
		            float f1 = rand.nextFloat() * 0.8F + 0.1F;
		            EntityItem entityitem;
		            for (float f2 = rand.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; world.spawnEntityInWorld(entityitem)) {
		                int j1 = rand.nextInt(21) + 10;
		                if (j1 > itemstack.stackSize) {
		                    j1 = itemstack.stackSize;
		                }
		                itemstack.stackSize -= j1;
		                entityitem = new EntityItem(world, (double)((float)tile.xCoord + f), (double)((float)tile.yCoord+tile.size + f1), (double)((float)tile.zCoord + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
		                float f3 = 0.05F;
		                entityitem.motionX = (double)((float)rand.nextGaussian() * f3);
		                entityitem.motionY = (double)((float)rand.nextGaussian() * f3 + 0.2F);
		                entityitem.motionZ = (double)((float)rand.nextGaussian() * f3);
		                if (itemstack.hasTagCompound()) {
		                    entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
		                }
		            }
				}
			}
		}
		if (tile.size==0) {
			super.breakBlock(world, x, y, z, block, meta);
			return;
		}
		breakMultiBlock(world, tile);
	}
	
	@Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		if (world.getBlockMetadata(x, y, z)>0||world.isRemote) {
			return;
		}
		int size = 0;
        for (int a=-6;a<=0;a++) { for (int b=-6;b<=0;b++) { for (int c=-6;c<=0;c++) {
        	if (world.getBlock(x+a, y+b, z+c).equals(this)) {
        		size = 2;
        		search:
    			for (int a1=-size;a1<=0;a1++) { for (int b1=-size;b1<=0;b1++) { for (int c1=-size;c1<=0;c1++) {
        			if (!world.getBlock(x+a-a1, y+b-b1, z+c-c1).equals(this)) {
        				size=0; break search;
        			}
        		}}}
        		if (size>0) {
        			makeMultiBlock(world, x+a, y+b, z+c, size);
        		}
        	}
        }}}
    }
	
	public static void makeMultiBlock(World world, int x, int y, int z, int size) {
		BlockCoord co = new BlockCoord(x, y, z);
		BlockCoord master= new BlockCoord(co.x+size/2, co.y+size/2, co.z+size/2);
		int rows = (size==0) ? 1 : size;
		for (int a1=-size;a1<=0;a1++) { for (int b1=-size;b1<=0;b1++) { for (int c1=-size;c1<=0;c1++) {
			getTile(world, co.x-a1, co.y-b1, co.z-c1, false).master = master;
			getTile(world, co.x-a1, co.y-b1, co.z-c1, false).Slots=null;
			getTile(world, co.x-a1, co.y-b1, co.z-c1, false).sendDescUpdate();
			world.setBlockMetadataWithNotify(co.x-a1, co.y-b1, co.z-c1, 1, 2);
		}}}
		getTile(world, master.x, master.y, master.z, true).Slots = new ItemStack[rows*9];
		getTile(world, master.x, master.y, master.z, true).size=size;
		getTile(world, master.x, master.y, master.z, true).sendDescUpdate();
	}
	
	public static void breakMultiBlock(World world, TileEntityFabricator tile) {
		tile = tile.getMaster();
		int hSize = tile.size/2;
		for (int a = tile.xCoord-hSize ; a<=tile.xCoord+hSize;a++) { for (int b = tile.yCoord-hSize; b<=tile.yCoord+hSize;b++) { for (int c = tile.zCoord-hSize; c<=tile.zCoord+hSize;c++) {
			world.setBlockMetadataWithNotify(a, b, c, 0, 2);
			getTile(world, a, b, c, false).Slots = new ItemStack[9];
			getTile(world, a, b, c, false).size=0;
			getTile(world, a, b, c, false).master=null;
			getTile(world, a, b, c, false).sendDescUpdate();
		}}}
	}

	@Override
	public boolean connectionAllowed(IBlockAccess world, int x, int y, int z, int side) {
		return world.getBlockMetadata(x, y, z)==1;
	}
	
	private static TileEntityFabricator getTile(IBlockAccess world, int x, int y, int z, boolean master) {
		TileEntityFabricator tile = ((TileEntityFabricator)world.getTileEntity(x, y, z));
		if (!master) return tile;
		return tile.getMaster();
	}
	
	@SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister reg) {
		super.registerBlockIcons(reg);
        multi = reg.registerIcon(XYreloaded.MODID+":multi");
    }
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityFabricator();
	}

}
