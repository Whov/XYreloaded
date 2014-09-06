package org.altervista.whovian.blocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.altervista.whovian.tileentity.TileEntityTank;
import org.altervista.whovian.xyreloaded.XYreloaded;
import org.altervista.whovian.xyreloaded.XYreloaded.ItankValid;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTank;

public class blockInTank extends Block{
	
	private IIcon[] icons;
	
	public blockInTank() {
		super(Material.glass);
		this.setBlockName("blockintank");
		this.setCreativeTab(XYreloaded.XYcraftTab);
		this.setHardness(0.25f);
		this.setBlockTextureName(XYreloaded.MODID+":inTank");
	}
	
	public boolean isOpaqueCube() {return false;}
	
	
	@SideOnly(Side.CLIENT)
	@Override
    public void registerBlockIcons(IIconRegister reg) {
		super.registerBlockIcons(reg);
        this.icons = new IIcon[] {reg.registerIcon(XYreloaded.MODID+":inTank"), reg.registerIcon(XYreloaded.MODID+":transparent")};
    }
	
	@SideOnly(Side.CLIENT)
	@Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		if (world.getBlockMetadata(x, y, z)==3) return icons[1];
		return icons[0];
	}
	
	private boolean isMultiBlockStructure(World world, int x, int y, int z, List checked) {
		boolean isS=true;
		checked.add(new int[]{x, y, z});//supponi sia circondato
		search:
		for (int x2=-1;x2<2;x2++) {for (int y2=-1;y2<2;y2++) {for (int z2=-1;z2<2;z2++) {//controlla nel 3x3
			Block block = world.getBlock(x+x2, y+y2, z+z2);
			//se blocchi intorno non buoni
			if ((block.equals(XYreloaded.blockValve) || block.equals(XYreloaded.blockInTank)&&world.getBlockMetadata(x+x2, y+y2, z+z2)==0)||
					block instanceof ItankValid&&world.getBlockMetadata(x+x2, y+y2, z+z2)%2==0) { } 
			else if (block.equals(XYreloaded.blockInTank)&&(!(Lcontains(checked, new int[]{x+x2,y+y2,z+z2})))&&world.getBlockMetadata(x+x2, y+y2, z+z2)==0) {
				if (((blockInTank)block).isMultiBlockStructure(world,x+x2, y+y2, z+z2, checked)==false) {isS=false; break search;}
			} else {
				isS=false;
				break search;
			}
		}}}
		return isS==true ? true:false;
	}
	
	public boolean FormMultiBlockStructure(World world, int x, int y, int z, TileEntityTank tank) {
		if (tank.checked.isEmpty()) {
			if (!this.isMultiBlockStructure(world, x, y, z, new ArrayList())) return false;
			System.out.println("CheckPoint");
			if (tank.tank==null) tank.tank = new FluidTank(0);
			else world.markBlockForUpdate(tank.xCoord, tank.yCoord, tank.zCoord);
			tank.checked = new ArrayList();
			tank.layers = new HashMap<Integer, Integer>();
		}
		world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		tank.checked.add(new int[]{x, y, z});
		tank.layers.put(y, tank.layers.getOrDefault(y, 0)+1);
		tank.tank.setCapacity(tank.tank.getCapacity()+8000);
		Block block;
		TileEntityTank tile;
		for (int x2=-1;x2<2;x2++) {for (int y2=-1;y2<2;y2++) {for (int z2=-1;z2<2;z2++) {//controlla nel 3x3
			block = world.getBlock(x+x2, y+y2, z+z2);
			tile = world.getTileEntity(x+x2, y+y2, z+z2)==null ? null : (TileEntityTank)world.getTileEntity(x+x2, y+y2, z+z2);
			if (block.equals(XYreloaded.blockValve)) {
				world.setBlockMetadataWithNotify(x+x2, y+y2, z+z2, 2, 2);
				tile.active = new int[]{tank.xCoord, tank.yCoord, tank.zCoord};
				world.markBlockForUpdate(tank.xCoord, tank.yCoord, tank.zCoord);
			} else if (block.equals(XYreloaded.blockInTank)&&(!(Lcontains(tank.checked, new int[]{x+x2,y+y2,z+z2})))) {
				((blockInTank)block).FormMultiBlockStructure(world,x+x2, y+y2, z+z2, tank);
			} else if (block instanceof ItankValid) {
				world.setBlockMetadataWithNotify(x+x2, y+y2, z+z2, world.getBlockMetadata(x+x2, y+y2, z+z2)+1, 2);
			}
			if (!(Lcontains(tank.checked, new int[]{x+x2,y+y2,z+z2}))) tank.checked.add(new int[]{x+x2,y+y2,z+z2});
		}}}
		tank.layers.put(-1, Math.min(tank.layers.getOrDefault(-1, 1000), y)); 
		return true;
	}
	
	public boolean BreakMultiBlockStructure(World world, int x, int y, int z) {
		world.setBlockMetadataWithNotify(x, y, z, 0, 2);
		Block block;
		for (int x2=-1;x2<2;x2++) {for (int y2=-1;y2<2;y2++) {for (int z2=-1;z2<2;z2++) {//controlla nel 3x3
			block = world.getBlock(x+x2, y+y2, z+z2);
			if (block.equals(XYreloaded.blockInTank)&&world.getBlockMetadata(x+x2, y+y2, z+z2)>0)
				((blockInTank)block).BreakMultiBlockStructure(world, x+x2, y+y2, z+z2);
			else if (block.equals(XYreloaded.blockValve)) {
				world.setBlockMetadataWithNotify(x+x2, y+y2, z+z2, 0, 2);
				world.markBlockForUpdate(x+x2, y+y2, z+z2);
				TileEntityTank tile = ((TileEntityTank)world.getTileEntity(x+x2, y+y2, z+z2)); 
				tile.checked.clear();
				if (tile.layers!=null) tile.layers.clear();
				if (tile.tank!=null) tile.tank.setCapacity(0);
			}
			else if (block instanceof ItankValid)
				world.setBlockMetadataWithNotify(x+x2, y+y2, z+z2, world.getBlockMetadata(x+x2, y+y2, z+z2)-1, 2);
		}}}
		return true;
	}
	
	public TileEntityTank findMainTileEntity(World world, int x, int y, int z, List checked) {
		for (int x2=-1;x2<2;x2++) {for (int y2=-1;y2<2;y2++) {for (int z2=-1;z2<2;z2++) {//controlla nel 3x3x3
			Block block = world.getBlock(x+x2, y+y2, z+z2);
			if (block.equals(XYreloaded.blockValve)) {
				int[] coords = ((TileEntityTank)world.getTileEntity(x+x2, y+y2, z+z2)).active;
				if (world.getTileEntity(coords[0], coords[1], coords[2])!=null) return (TileEntityTank)world.getTileEntity(coords[0], coords[1], coords[2]);
			}
			else if (block.equals(XYreloaded.blockInTank)&&Lcontains(checked, new int[]{x+x2, y+y2, z+z2})==false) {
				checked.add(new int[]{x+x2, y+y2, z+z2});
				TileEntityTank tile = ((blockInTank)block).findMainTileEntity(world, x+x2, y+y2, z+z2, checked);
				if (tile!=null) {
					int[] coords = tile.active;
					return (TileEntityTank)world.getTileEntity(coords[0], coords[1], coords[2]);
				}
			}
		}}}
		return null;
	}

	/* data una Lista di Arrays verifica se la lista contiene un array equivalente a quello dato */
	public static boolean Lcontains(List list, int[] arr) {
		Iterator i = list.iterator();
		while (i.hasNext()) {
			if (Arrays.equals((int[])i.next(), arr)) return true;
		}
		return false;
	}
	
}
