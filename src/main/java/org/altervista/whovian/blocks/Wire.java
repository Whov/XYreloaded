package org.altervista.whovian.blocks;

import java.util.Random;

import org.altervista.whovian.xyreloaded.Tricks;
import org.altervista.whovian.xyreloaded.Tricks.IRed;
import org.altervista.whovian.xyreloaded.XYreloaded;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Wire extends Block implements IRed {

    public Wire() {
        super(Material.circuits);
        this.setBlockBounds(0.3F, 0.3F, 0.3F, 0.7F, 0.7F, 0.7F);
        this.setBlockTextureName("redstone_block");
        this.setBlockName("wire");
        this.setCreativeTab(XYreloaded.XYcraftTab);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
    	int signal;
//    	if (world.getTileEntity(x, y, z)!=null && world.getTileEntity(x, y, z) instanceof codechicken.multipart.TileMultipart) {
//    		signal = ((WirePart)((codechicken.multipart.TileMultipart)world.getTileEntity(x, y, z)).jPartList().get(0)).meta;
//    	} else
    		signal = world.getBlockMetadata(x, y, z);
        if (signal==15) return 16729156;
        if (signal>10) return 16746632;
        if (signal>6) return 16777215;
        if (signal>3) return 8978431;
        if (signal>0) return 5636095;
        return 1179647;
    }
    
    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        if (world.isRemote) return;
        int level=0;
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
//    		if (world.getBlock(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ)==XYreloaded.blockWire)
//    			level = Math.max(level, world.getBlockMetadata(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ));
        	//TODO
        	if (Tricks.getRed(world, x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ)!=null)
    			level = Math.max(level, Tricks.getRed(world, x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ).getMeta(world, x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ));
    		else if (world.getBlock(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ)==XYreloaded.blockWireInput)
    			level = Math.max(level, WireInput.getRedstoneSignal(world, x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ));
    	}
        updateRedstone(world,x,y,z,level);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata) {
		if (world.isRemote) return;
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
//    		if (world.getBlock(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ)==XYreloaded.blockWire) {
			//TODO
			if (Tricks.getRed(world, x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ)!=null) {
    			int rdLevel = getStrengthAcrossNetwork(world, x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ, 0);
    			updateRedstone(world, x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ, rdLevel);
    		}
    	}
    }
    
    private static void updateAdiacents(World world, int x, int y, int z) {
    	world.notifyBlockChange(x, y, z, XYreloaded.blockWire);
    	for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS)
    		world.notifyBlockChange(x+d.offsetX, y+d.offsetY, z+d.offsetZ, XYreloaded.blockWire);
    }
    
    /** Returns the strongest redstone value across the network and resets rd value for all the wires.
     * When manually called, Rd must be 0. Recursively calls itself increasing it**/
    public static int getStrengthAcrossNetwork(World world, int x, int y, int z, int Rd) {
//		world.setBlockMetadataWithNotify(x, y, z, 0, 2);
		//TODO
		Tricks.getRed(world, x, y, z).setMeta(world, x, y, z, 0);
		updateAdiacents(world, x, y, z);
		for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
//			if (world.getBlock(x+d.offsetX, y+d.offsetY, z+d.offsetZ)==XYreloaded.blockWire&&world.getBlockMetadata(x+d.offsetX, y+d.offsetY, z+d.offsetZ)>0) {
//				Rd = getStrengthAcrossNetwork(world, x+d.offsetX, y+d.offsetY, z+d.offsetZ, Rd);
//			} else if (world.getBlock(x+d.offsetX, y+d.offsetY, z+d.offsetZ)==XYreloaded.blockWireInput) {
//				Rd = Math.max(Rd, WireInput.getRedstoneSignal(world, x+d.offsetX, y+d.offsetY, z+d.offsetZ));
//			}
			//TODO
			if (!Tricks.getRed(world, x, y, z).canSend(d)) continue;
			if (Tricks.getRed(world, x+d.offsetX, y+d.offsetY, z+d.offsetZ)!=null &&
					Tricks.getRed(world, x+d.offsetX, y+d.offsetY, z+d.offsetZ).getMeta(world, x+d.offsetX, y+d.offsetY, z+d.offsetZ)>0) {
				Rd = getStrengthAcrossNetwork(world, x+d.offsetX, y+d.offsetY, z+d.offsetZ, Rd);
			} else if (world.getBlock(x+d.offsetX, y+d.offsetY, z+d.offsetZ)==XYreloaded.blockWireInput) {
				Rd = Math.max(Rd, WireInput.getRedstoneSignal(world, x+d.offsetX, y+d.offsetY, z+d.offsetZ));
			}
		}
		return Rd;
	}
    
    public static void updateRedstone(World world, int x, int y, int z, int rdLevel) {
//    	world.setBlockMetadataWithNotify(x, y, z, rdLevel, 2);
    	//TODO
    	Tricks.getRed(world, x, y, z).setMeta(world, x, y, z, rdLevel);
    	updateAdiacents(world, x, y, z);
    	for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
//    		if (world.getBlock(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ)==XYreloaded.blockWire&&
//    				world.getBlockMetadata(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ)!=rdLevel)
    		//TODO
    		if (Tricks.getRed(world, x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ)!=null &&
    				Tricks.getRed(world, x, y, z).canReceive(dir) &&
    				Tricks.getRed(world, x, y, z).canSend(dir) &&
    				Tricks.getRed(world, x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ).getMeta(world, x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ)!=rdLevel)
    			Wire.updateRedstone(world, x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ, rdLevel);
    	}
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess p_149748_1_, int p_149748_2_, int p_149748_3_, int p_149748_4_, int p_149748_5_)
    {
        return this.isProvidingWeakPower(p_149748_1_, p_149748_2_, p_149748_3_, p_149748_4_, p_149748_5_);
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
        return world.getBlockMetadata(x, y, z);
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand)
    {
        int l = world.getBlockMetadata(x, y, z);

        if (l > 0)
        {
            double d0 = (double)x + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
            double d1 = (double)((float)y + 0.0625F);
            double d2 = (double)z + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
            float f = (float)l / 15.0F;
            float f1 = f * 0.6F + 0.4F;

            if (l == 0)
            {
                f1 = 0.0F;
            }

            float f2 = f * f * 0.7F - 0.5F;
            float f3 = f * f * 0.6F - 0.7F;

            if (f2 < 0.0F)
            {
                f2 = 0.0F;
            }

            if (f3 < 0.0F)
            {
                f3 = 0.0F;
            }

            world.spawnParticle("reddust", d0, d1, d2, (double)f1, (double)f2, (double)f3);
        }
    }

	@Override
	public int getMeta(World world, int x, int y, int z) {
		return world.getBlockMetadata(x, y, z);
	}

	@Override
	public boolean canSend(ForgeDirection side) {
		return true;
	}
	
	@Override
	public boolean canReceive(ForgeDirection side) {
		return true;
	}

	@Override
	public void setMeta(World world, int x, int y, int z, int value) {
		world.setBlockMetadataWithNotify(x, y, z, value, 2);
	}
    
}