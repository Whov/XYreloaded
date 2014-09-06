package org.altervista.whovian.blocks;

import java.util.ArrayList;
import java.util.Random;

import org.altervista.whovian.client.proxy.ClientProxy;
import org.altervista.whovian.xyreloaded.XYreloaded;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLogic;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class RiceBlock extends Block implements IGrowable {

	public RiceBlock() {
		super(Material.coral);
		setBlockName("maisBlock");
		this.setTickRandomly(true);
        float f = 0.5F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
        this.setHardness(0.0F);
        this.setStepSound(soundTypeGrass);
        this.setBlockTextureName("wheat");
        this.disableStats();
	}

	@SideOnly(Side.CLIENT)
    private IIcon[] icons;

	@Override
	public int getRenderType() {
		return ClientProxy.MaisRenderType;
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
    public int getRenderBlockPass()
	{
		return 1;
	}
   
    @Override
    public boolean canRenderInPass(int pass)
    {
        ClientProxy.renderPass = pass;
        return true;
    }
	
	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_) {
        return null;
    }
	
	@Override
	public boolean func_149851_a(World p_149851_1_, int p_149851_2_, int p_149851_3_, int p_149851_4_, boolean p_149851_5_) {
		return p_149851_1_.getBlockMetadata(p_149851_2_, p_149851_3_, p_149851_4_) != 3;
	}

	@Override
	public boolean func_149852_a(World p_149852_1_, Random p_149852_2_, int p_149852_3_, int p_149852_4_, int p_149852_5_) {
		return true;
	}
	
	@Override
	public void func_149853_b(World p_149853_1_, Random p_149853_2_, int p_149853_3_, int p_149853_4_, int p_149853_5_) {
		this.func_149863_m(p_149853_1_, p_149853_3_, p_149853_4_, p_149853_5_);
	}
	
	public void func_149863_m(World p_149863_1_, int p_149863_2_, int p_149863_3_, int p_149863_4_) {
        int l = p_149863_1_.getBlockMetadata(p_149863_2_, p_149863_3_, p_149863_4_) + 1;
        if (l > 3) {
            l = 3;
        }
        p_149863_1_.setBlockMetadataWithNotify(p_149863_2_, p_149863_3_, p_149863_4_, l, 2);
    }

    @Override
    public boolean canBlockStay(World p_149718_1_, int p_149718_2_, int p_149718_3_, int p_149718_4_) {
    	return this.canPlaceBlockAt(p_149718_1_, p_149718_2_, p_149718_3_, p_149718_4_);
    }
	
    @Override
    public boolean canPlaceBlockAt(World p_149718_1_, int p_149718_2_, int p_149718_3_, int p_149718_4_) {
    	int count = 0;
    	count += (p_149718_1_.getBlock(p_149718_2_+1, p_149718_3_, p_149718_4_)==Blocks.water)?1:0;
    	count += (p_149718_1_.getBlock(p_149718_2_-1, p_149718_3_, p_149718_4_)==Blocks.water)?1:0;
    	count += (p_149718_1_.getBlock(p_149718_2_, p_149718_3_, p_149718_4_+1)==Blocks.water)?1:0;
    	count += (p_149718_1_.getBlock(p_149718_2_, p_149718_3_, p_149718_4_-1)==Blocks.water)?1:0;
    	return (p_149718_1_.getBlock(p_149718_2_, p_149718_3_ - 1, p_149718_4_)==Blocks.dirt||p_149718_1_.getBlock(p_149718_2_, p_149718_3_ - 1, p_149718_4_)==Blocks.grass) && count>1;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if (!canBlockStay(world, x, y, z)) {this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0); world.setBlockToAir(x, y, z);}
    }
    
	@Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        super.updateTick(world, x, y, z, rand);
        if (world.getBlockLightValue(x, y + 1, z) >= 8) {
            int l = world.getBlockMetadata(x, y, z);
            if (l < 3) {
                if (rand.nextInt(8) == 0) {
                    world.setBlockMetadataWithNotify(x, y, z, ++l, 2);
                }
            }
        }
	}
	
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        if (meta < 0 || meta > 3) {
        	meta = 3;
        }
        return this.icons[meta];
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return XYreloaded.riceSeeds;
    }

    @Override
    public int quantityDropped(Random p_149745_1_) {
        return 1;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
        return XYreloaded.riceSeeds;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        this.icons = new IIcon[4];
        for (int i = 0; i < this.icons.length; ++i) {
            this.icons[i] = p_149651_1_.registerIcon(XYreloaded.MODID+":Mais_stage_" + i);
        }
    }
    
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> ret = super.getDrops(world, x, y, z, metadata, fortune);
        if (metadata<3) return ret;
    	for (int i = 0; i < 3 + fortune; ++i) {
    		if (world.rand.nextInt(8) <= metadata) {
                ret.add(new ItemStack(XYreloaded.riceSeeds, 1, 0));
            }
        }
        return ret;
    }
    
}
