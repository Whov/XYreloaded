package org.altervista.whovian.blocks;

import org.altervista.whovian.xyreloaded.Tricks;
import org.altervista.whovian.xyreloaded.XYreloaded;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class WireInput extends Block {

	public WireInput() {
		super(Material.rock);
		this.setBlockName("wireinput");
		this.setBlockTextureName(XYreloaded.MODID+":wireInputFront");
		this.setCreativeTab(XYreloaded.XYcraftTab);
	}
	
//	@Override
//	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
//		int meta = world.getBlockMetadata(x, y, z);
//		setBlockBounds(0f, 0f, 0f, 1f, 1f, 1f);
//	}
//	
//	@Override
//	@SideOnly(Side.CLIENT)
//    public IIcon getIcon(int side, int meta) {
//		if (side==ForgeDirection.OPPOSITES[meta]) return icons[1];
//		else if (side==meta) return this.blockIcon;
//		return icons[0];
//    }
	
	@Override
	public boolean getWeakChanges(IBlockAccess world, int x, int y, int z) {
        return true;
    }
	
	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int stackMeta) {
		ForgeDirection dir = ForgeDirection.getOrientation(side).getOpposite();
		int rdLevel = getRedstoneSignal(world, x, y, z, dir);
		for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
//			if (world.getBlock(x+d.offsetX, y+d.offsetY, z+d.offsetZ)==XYreloaded.blockWire &&
//					world.getBlockMetadata(x+d.offsetX, y+d.offsetY, z+d.offsetZ)<rdLevel)
			//TODO
			if (Tricks.getRed(world, x+d.offsetX, y+d.offsetY, z+d.offsetZ)!=null &&
					Tricks.getRed(world, x+d.offsetX, y+d.offsetY, z+d.offsetZ).getMeta(world, x+d.offsetX, y+d.offsetY, z+d.offsetZ)<rdLevel)
				Wire.updateRedstone(world, x+d.offsetX, y+d.offsetY, z+d.offsetZ, rdLevel);
		}
		return ForgeDirection.OPPOSITES[side];
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int metadata) {
		for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
//			if (world.getBlock(x+d.offsetX, y+d.offsetY, z+d.offsetZ)==XYreloaded.blockWire) {
			//TODO
			if (Tricks.getRed(world, x+d.offsetX, y+d.offsetY, z+d.offsetZ)!=null) {
				int rdLevel = Wire.getStrengthAcrossNetwork(world, x+d.offsetX, y+d.offsetY, z+d.offsetZ, 0);
				Wire.updateRedstone(world, x+d.offsetX, y+d.offsetY, z+d.offsetZ, rdLevel);
			}
		}
	}
	
	@Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
	
    public static int getRedstoneSignal(World world, int x, int y, int z, ForgeDirection dir) {
		return world.getIndirectPowerLevelTo(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ, dir.ordinal());
    }
    
    public static int getRedstoneSignal(World world, int x, int y, int z) {
    	ForgeDirection dir = ForgeDirection.getOrientation(world.getBlockMetadata(x, y, z));
		return getRedstoneSignal(world, x, y, z, dir);
    }
    
    @Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
    	if (block==XYreloaded.blockWire) return;
    	for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
//			if (world.getBlock(x+d.offsetX, y+d.offsetY, z+d.offsetZ)==XYreloaded.blockWire) {
    		if (Tricks.getRed(world, x+d.offsetX, y+d.offsetY, z+d.offsetZ)!=null) {
				int rdLevel = Wire.getStrengthAcrossNetwork(world, x+d.offsetX, y+d.offsetY, z+d.offsetZ, 0);
				Wire.updateRedstone(world, x+d.offsetX, y+d.offsetY, z+d.offsetZ, rdLevel);
			}
		}
	}
    
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        super.registerBlockIcons(reg);
        icons = new IIcon[] {reg.registerIcon(XYreloaded.MODID+":wireInputSide"), reg.registerIcon(XYreloaded.MODID+":wireInputBack")}; 
    }
	
}
