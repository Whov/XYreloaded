package org.altervista.whovian.blocks;

import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.UP;
import static net.minecraftforge.common.util.ForgeDirection.WEST;

import org.altervista.whovian.multipart.WirePart;
import org.altervista.whovian.tileentity.TileEntityWire;
import org.altervista.whovian.xyreloaded.XYreloaded;

import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.TileMultipart;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class Wire extends Block implements ITileEntityProvider {
	
	public Wire() {
		super(Material.circuits);
		this.setBlockName("blockWire");
		this.setCreativeTab(XYreloaded.XYcraftTab);
		this.setHardness(0.0f);
		this.setBlockTextureName("redstone_block");
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }
	
	private ForgeDirection getConnection(int x, int y, int z,int tileX, int tileY, int tileZ) {
		ForgeDirection dir;
		for (int i=0;i<6;i++) {
			dir = ForgeDirection.getOrientation(i);
			if (dir.offsetX==tileX-x&&dir.offsetY==tileY-y&&dir.offsetZ==tileZ-z) {
				return dir;//found it!
			}
		}
		return null;
	}
	
	@Override
	public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ) {
		ForgeDirection dir = getConnection(x, y, z, tileX, tileY, tileZ);
		if (ForgeDirection.getOrientation(world.getBlockMetadata(x, y, z))!=dir) {
			((TileEntityWire)world.getTileEntity(x, y, z)).checkConnection(dir);
		}
    }
	
	@Override
	public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side) {
        ForgeDirection dir = ForgeDirection.getOrientation(side);
        if (world.isSideSolid(x-dir.offsetX, y-dir.offsetY, z-dir.offsetZ, dir)) return true;
        if (world.getBlock(x-dir.offsetX, y-dir.offsetY, z-dir.offsetZ).equals(this)) {
        	ForgeDirection dir2 = ForgeDirection.getOrientation(world.getBlockMetadata(x-dir.offsetX, y-dir.offsetY, z-dir.offsetZ));
            System.out.println(world.getTileEntity(x-dir2.offsetX, y-dir2.offsetY, z-dir2.offsetZ));
            if (world.getBlock(x-dir2.offsetX, y-dir2.offsetY, z-dir2.offsetZ).equals(this)) {
            	return true;
            }
            if (world.getTileEntity(x-dir2.offsetX, y-dir2.offsetY, z-dir2.offsetZ)!=null&&world.getTileEntity(x-dir2.offsetX, y-dir2.offsetY, z-dir2.offsetZ) instanceof TileMultipart) {
            	TileMultipart tile = (TileMultipart) world.getTileEntity(x-dir2.offsetX, y-dir2.offsetY, z-dir2.offsetZ);
            	if (tile.partMap(dir.getOpposite().ordinal())!=null&&tile.partMap(dir.getOpposite().ordinal()) instanceof WirePart) return true;
            }
        }
        return false;
    }
	
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		for (int i=0;i<6;i++) {
			if (canPlaceBlockOnSide(world, x, y, z, i)==true) return true;
		}
		return false;
    }

	@Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
		return ((TileEntityWire)world.getTileEntity(x, y, z)).rdLevel;
    }

	@Override
    public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side) {
		return ((TileEntityWire)world.getTileEntity(x, y, z)).rdLevel;
    }
	
	@Override
	public boolean renderAsNormalBlock() {
	     return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public boolean canProvidePower()
    {
        return true;
    }

	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
		return side;
	}
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		for (ForgeDirection dir:ForgeDirection.VALID_DIRECTIONS) {
			((TileEntityWire)world.getTileEntity(x, y, z)).checkConnection(dir);
		}
	}
	
	@Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		//makes it drop
        if (!this.canPlaceBlockAt(world, x, y, z)) {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockToAir(x, y, z);
            return;
        } else {
            int l = world.getBlockMetadata(x, y, z);
            if (!canPlaceBlockOnSide(world, x, y, z, l)) {
                this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
                world.setBlockToAir(x, y, z);
            }
        }
    }
	
	@Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        int l = world.getBlockMetadata(x, y, z);
        if 		(l == 0) this.setBlockBounds(0.3F, 0.8F, 0.3F, 0.7F, 1F, 0.7F);
        else if (l == 1) this.setBlockBounds(0.3F, 0F, 0.3F, 0.7F, 0.2F, 0.7F);
        else if (l == 2) this.setBlockBounds(0.3F, 0.3F, 0.8F, 0.7F, 0.7F, 1F);
        else if (l == 3) this.setBlockBounds(0.3F, 0.3F, 0.0F, 0.7F, 0.7F, 0.2F);
        else if (l == 4) this.setBlockBounds(0.8F, 0.3F, 0.3F, 1.0F, 0.7F, 0.7F);
        else if (l == 5) this.setBlockBounds(0.0F, 0.3F, 0.3F, 0.2F, 0.7F, 0.7F);
    }
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityWire();
	}
	
}
