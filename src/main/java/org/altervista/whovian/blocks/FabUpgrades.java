package org.altervista.whovian.blocks;

import java.util.Random;

import org.altervista.whovian.tileentity.TileEntityFabricator;
import org.altervista.whovian.xyreloaded.XYreloaded;

import scala.actors.threadpool.Arrays;
import codechicken.lib.vec.BlockCoord;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class FabUpgrades extends Block {

	private IIcon[] icon;
	public String[] names = new String[] {"Blank Booster", "Recipe Booster", "Speed Booster", "Storage Booster"};
	
	public FabUpgrades() {
		super(Material.rock);
		this.setBlockName("fabUpgrade");
		this.setCreativeTab(XYreloaded.XYcraftTab);
		this.setHardness(2.0f);
		this.setBlockTextureName("redstone_block");
	}
	
	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float Hx, float Hy, float Hz, int meta) {
		ForgeDirection dir = ForgeDirection.getOrientation(side).getOpposite();
		if (world.getBlock(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ).equals(XYreloaded.fabricator) &&
				world.getTileEntity(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ) instanceof TileEntityFabricator &&
				((TileEntityFabricator) world.getTileEntity(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ)).size>0) {
			TileEntityFabricator tile = (TileEntityFabricator) world.getTileEntity(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ);
			tile = (TileEntityFabricator) world.getTileEntity(tile.xCoord, tile.yCoord, tile.zCoord);
			switch (meta) {
			case 1: tile.RecipeLimit+=1; break;
			case 2: tile.speed-=2; break;
			case 3: tile.Slots = (ItemStack[]) Arrays.copyOf(tile.Slots, tile.Slots.length+9);
			default: return meta;
			}
			tile.upgrades.add(new BlockCoord(x, y, z));
		}
		return meta;
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			if (world.getBlock(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ).equals(XYreloaded.fabricator) &&
					world.getTileEntity(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ) instanceof TileEntityFabricator &&
					((TileEntityFabricator) world.getTileEntity(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ)).size>0) {
				
				TileEntityFabricator tile = (TileEntityFabricator) world.getTileEntity(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ);
				tile = (TileEntityFabricator) world.getTileEntity(tile.xCoord, tile.yCoord, tile.zCoord);
				int index = tile.upgrades.indexOf(new BlockCoord(x, y, z));
				if (index==-1) continue;//might be adiacent to other fabs
				tile.upgrades.remove(index);
				switch (meta) {
				case 1: tile.RecipeLimit-=1; break;
				case 2: tile.speed+=2; break;
				case 3:
					ItemStack itemstack;
					Random rand = new Random();
					for (int i=0;i<9;i++) {
						
						itemstack = tile.Slots[i+tile.Slots.length-9];
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
					tile.Slots = (ItemStack[]) Arrays.copyOf(tile.Slots, tile.Slots.length-9);
				default: return;
				}
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return this.blockIcon;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
		this.icon = new IIcon[names.length];
    	for (int i=0;i<icon.length;i++) {
    		icon[i] = reg.registerIcon(XYreloaded.MODID+":fabUp/"+names[i]);
    	}
    }
	
}
