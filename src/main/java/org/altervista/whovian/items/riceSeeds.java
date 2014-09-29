package org.altervista.whovian.items;

import org.altervista.whovian.xyreloaded.XYreloaded;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSeeds;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraft.item.ItemStack;

public class riceSeeds extends ItemSeeds {

	public riceSeeds() {
		super(XYreloaded.riceBlock, Blocks.dirt);
		this.setUnlocalizedName("XYseeds");
		this.setTextureName(XYreloaded.MODID+":rice_seeds");
	}

	@Override
    public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
    {
        return EnumPlantType.Water;
    }
	
	@Override
    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(p_77659_2_, p_77659_3_, true);
        if (movingobjectposition == null) {
            return p_77659_1_;
        }
        else {
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                int i = movingobjectposition.blockX;
                int j = movingobjectposition.blockY;
                int k = movingobjectposition.blockZ;
                if (!p_77659_2_.canMineBlock(p_77659_3_, i, j, k)) {
                    return p_77659_1_;
                }
                if (!p_77659_3_.canPlayerEdit(i, j, k, movingobjectposition.sideHit, p_77659_1_)) {
                    return p_77659_1_;
                }
                if (p_77659_2_.getBlock(i, j, k).getMaterial() == Material.water && p_77659_2_.getBlockMetadata(i, j, k) == 0 && (p_77659_2_.getBlock(i, j - 1, k)==Blocks.dirt||p_77659_2_.getBlock(i, j - 1, k)==Blocks.grass)) {
                    p_77659_2_.setBlock(i, j, k, XYreloaded.riceBlock);
                    if (!p_77659_3_.capabilities.isCreativeMode) {
                        --p_77659_1_.stackSize;
                    }
                }
            }
            return p_77659_1_;
        }
    }
	
}
