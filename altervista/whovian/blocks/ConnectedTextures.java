package org.altervista.whovian.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ConnectedTextures extends Block {
	
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;
	private String modId;
	private String path;
	/**just by itself<br>1*/
	public int Falone;
	/**xxx<br>xAA<br>xxx<br>4*/
	public int Fjust1=-1;
	/**xAx<br>xAA<br>xAx<br>4*/
	public int Fedge3=-1;
	/**xxx<br>xAA<br>xAx<br>4*/
	public int Fcorner=-1;
	/**xAx<br>xAx<br>xAx<br>3*/
	public int FinLine=-1;
	/**AAA<br>AAA<br>AAA<br>1*/
	public int Fsurrounded=-1;	
	
	/**
	 * NB: set at least some of the public fields to tell getIcon how to behave (the last number tells you how many textures will be needed.<br>
	 * follow the order given by you)<br>
	 * The block base texture (for when rendered in hand or such) is (string) path+"0" and should be the block texture for when Falone. Change this if needed.
	 * @param material
	 * @param path texture path (will end with a number that is added automatically based on the public field)
	 * @param modId used to get the texture
	 */
	public ConnectedTextures(Material material, String path, String modId) {
		super(material);
		this.path=path;
		this.modId=modId;
		this.setBlockTextureName(modId+":"+path+"0");
	}
	
	/**
	 * Don't touch this either
	 * order your textures clock-wise from the top one (and play with it a bit).
	 */
	@SideOnly(Side.CLIENT)
	@Override
    public void registerBlockIcons(IIconRegister reg) {
		super.registerBlockIcons(reg);
		int amount=0;
		if (Falone!=-1) amount++;
		if (Fjust1!=-1) amount+=4;
		if (Fedge3!=-1) amount+=4;
		if (Fcorner!=-1) amount+=4;
		if (FinLine!=-1) amount+=3;
		if (Fsurrounded!=-1) amount++;
        this.icons = new IIcon[amount];
        for (int i = 0; i < icons.length; ++i) {
            this.icons[i] = reg.registerIcon(modId+":"+path+i);
        }
    }

	/**
	 * This is the reason you downloaded this API, so don't touch this
	 * NB: the order of the matches is:
	 * 1) Fsurrounded and Falone
	 * 2) Fedge3
	 * 3) FinLine
	 * 4) Fcorner
	 * 5) Fjust1
	 * If one fails because there is no set, the next ones will be checked, because only the presence of the block is checked, not its absence.
	 * If no icon is found the standard icon is chosen.
	 */
	@SideOnly(Side.CLIENT)
	@Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		if (!connectionAllowed(world, x, y, z, side)) return icons[0];
		boolean[] a = getB(world, x, y, z);
		switch (side) {
		case 0:
			if ((Fsurrounded!=-1)&&(allB(true, a[0], a[1], a[4], a[5]))) return icons[Fsurrounded];
			if (Fedge3!=-1) {
				if (allB(true, a[0], a[1], a[5])) return icons[Fedge3];
				if (allB(true, a[0], a[1], a[4])) return icons[Fedge3+2];
				if (allB(true, a[4], a[5], a[1])) return icons[Fedge3+3];
				if (allB(true, a[4], a[5], a[0])) return icons[Fedge3+1];
			} if (FinLine!=-1) {
				if (allB(true, a[0], a[1])) return icons[FinLine];
				if (allB(true, a[4], a[5])) return icons[FinLine+2];
			} if (Fcorner!=-1) {
				if (allB(true, a[1], a[5])) return icons[Fcorner];
				if (allB(true, a[0], a[5])) return icons[Fcorner+1];
				if (allB(true, a[0], a[4])) return icons[Fcorner+2];
				if (allB(true, a[1], a[4])) return icons[Fcorner+3];
			} if (Fjust1!=-1) {
				if (a[5]) return icons[Fjust1];
				if (a[0]) return icons[Fjust1+1];
				if (a[4]) return icons[Fjust1+2];
				if (a[1]) return icons[Fjust1+3];
			}
			return icons[Falone];
		case 1:
			if ((Fsurrounded!=-1)&&(allB(true, a[0], a[1], a[4], a[5]))) return icons[Fsurrounded];
			if (Fedge3!=-1) {
				if (allB(true, a[0], a[1], a[5])) return icons[Fedge3];
				if (allB(true, a[0], a[1], a[4])) return icons[Fedge3+2];
				if (allB(true, a[4], a[5], a[1])) return icons[Fedge3+3];
				if (allB(true, a[4], a[5], a[0])) return icons[Fedge3+1];
			} if (FinLine!=-1) {
				if (allB(true, a[0], a[1])) return icons[FinLine];
				if (allB(true, a[4], a[5])) return icons[FinLine+2];
			} if (Fcorner!=-1) {
				if (allB(true, a[1], a[5])) return icons[Fcorner];
				if (allB(true, a[0], a[5])) return icons[Fcorner+1];
				if (allB(true, a[0], a[4])) return icons[Fcorner+2];
				if (allB(true, a[1], a[4])) return icons[Fcorner+3];
			} if (Fjust1!=-1) {
				if (a[5]) return icons[Fjust1];
				if (a[0]) return icons[Fjust1+1];
				if (a[4]) return icons[Fjust1+2];
				if (a[1]) return icons[Fjust1+3];
			}
			return icons[Falone];
		case 2:
			if ((Fsurrounded!=-1)&&(allB(true, a[0], a[1], a[2], a[3]))) return icons[Fsurrounded];
			if (Fedge3!=-1) {
				if (allB(true, a[0], a[1], a[3])) return icons[Fedge3+2];
				if (allB(true, a[0], a[1], a[2])) return icons[Fedge3];
				if (allB(true, a[2], a[3], a[1])) return icons[Fedge3+1];
				if (allB(true, a[2], a[3], a[0])) return icons[Fedge3+3];
			} if (FinLine!=-1) {
				if (allB(true, a[0], a[1])) return icons[FinLine];
				if (allB(true, a[2], a[3])) return icons[FinLine+1];
			} if (Fcorner!=-1) {
				if (allB(true, a[1], a[3])) return icons[Fcorner+2];
				if (allB(true, a[0], a[3])) return icons[Fcorner+3];
				if (allB(true, a[0], a[2])) return icons[Fcorner];
				if (allB(true, a[1], a[2])) return icons[Fcorner+1];
			} if (Fjust1!=-1) {
				if (a[3]) return icons[Fjust1+2];
				if (a[0]) return icons[Fjust1+3];
				if (a[2]) return icons[Fjust1];
				if (a[1]) return icons[Fjust1+1];
			}
			return icons[Falone];
		case 3:
			if ((Fsurrounded!=-1)&&(allB(true, a[0], a[1], a[2], a[3]))) return icons[Fsurrounded];
			if (Fedge3!=-1) {
				if (allB(true, a[0], a[1], a[3])) return icons[Fedge3+2];
				if (allB(true, a[0], a[1], a[2])) return icons[Fedge3];
				if (allB(true, a[2], a[3], a[1])) return icons[Fedge3+3];
				if (allB(true, a[2], a[3], a[0])) return icons[Fedge3+1];
			} if (FinLine!=-1) {
				if (allB(true, a[0], a[1])) return icons[FinLine];
				if (allB(true, a[2], a[3])) return icons[FinLine+1];
			} if (Fcorner!=-1) {
				if (allB(true, a[1], a[3])) return icons[Fcorner+3];
				if (allB(true, a[0], a[3])) return icons[Fcorner+2];
				if (allB(true, a[0], a[2])) return icons[Fcorner+1];
				if (allB(true, a[1], a[2])) return icons[Fcorner];
			} if (Fjust1!=-1) {
				if (a[3]) return icons[Fjust1+2];
				if (a[0]) return icons[Fjust1+1];
				if (a[2]) return icons[Fjust1];
				if (a[1]) return icons[Fjust1+3];
			}
			return icons[Falone];
		case 4:
			if ((Fsurrounded!=-1)&&(allB(true, a[4], a[5], a[2], a[3]))) return icons[Fsurrounded];
			if (Fedge3!=-1) {
				if (allB(true, a[4], a[5], a[3])) return icons[Fedge3+2];
				if (allB(true, a[4], a[5], a[2])) return icons[Fedge3];
				if (allB(true, a[2], a[3], a[5])) return icons[Fedge3+3];
				if (allB(true, a[2], a[3], a[4])) return icons[Fedge3+1];
			} if (FinLine!=-1) {
				if (allB(true, a[4], a[5])) return icons[FinLine+1];
				if (allB(true, a[2], a[3])) return icons[FinLine+2];
			} if (Fcorner!=-1) {
				if (allB(true, a[5], a[3])) return icons[Fcorner+3];
				if (allB(true, a[4], a[3])) return icons[Fcorner+2];
				if (allB(true, a[4], a[2])) return icons[Fcorner+1];
				if (allB(true, a[5], a[2])) return icons[Fcorner];
			} if (Fjust1!=-1) {
				if (a[3]) return icons[Fjust1+2];
				if (a[4]) return icons[Fjust1+3];
				if (a[2]) return icons[Fjust1];
				if (a[5]) return icons[Fjust1+1];
			}
			return icons[Falone];
		case 5:
			if ((Fsurrounded!=-1)&&(allB(true, a[4], a[5], a[2], a[3]))) return icons[Fsurrounded];
			if (Fedge3!=-1) {
				if (allB(true, a[4], a[5], a[3])) return icons[Fedge3+2];
				if (allB(true, a[4], a[5], a[2])) return icons[Fedge3];
				if (allB(true, a[2], a[3], a[5])) return icons[Fedge3+1];
				if (allB(true, a[2], a[3], a[4])) return icons[Fedge3+3];
			} if (FinLine!=-1) {
				if (allB(true, a[4], a[5])) return icons[FinLine+1];
				if (allB(true, a[2], a[3])) return icons[FinLine+2];
			} if (Fcorner!=-1) {
				if (allB(true, a[5], a[3])) return icons[Fcorner+2];
				if (allB(true, a[4], a[3])) return icons[Fcorner+3];
				if (allB(true, a[4], a[2])) return icons[Fcorner];
				if (allB(true, a[5], a[2])) return icons[Fcorner+1];
			} if (Fjust1!=-1) {
				if (a[3]) return icons[Fjust1+2];
				if (a[4]) return icons[Fjust1+3];
				if (a[2]) return icons[Fjust1];
				if (a[5]) return icons[Fjust1+1];
			}
			return icons[Falone];
		}
		//should never be needed
		return blockIcon;
    }
	
	/**
	 * Is the connection allowed always when sides touch or special conditions?
	 * Override if needed
	 * @param world
	 * @param x of this block
	 * @param y of this block
	 * @param z of this block
	 * @param side of this block that is being rendered
	 * @return true/false (true is default)
	 */
	public boolean connectionAllowed(IBlockAccess world, int x, int y, int z, int side) {
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
    {
        return world.getBlock(x, y, z) == this ? false : super.shouldSideBeRendered(world, x, y, z, side);
    }
	
	private boolean allB(boolean test, boolean... b) {
		for (boolean v:b) {
			if (v!=test) return false;
		}
		return true;
	}
	
	private boolean[] getB(IBlockAccess world, int x, int y, int z) {
		return new boolean[]{
				world.getBlock(x-1, y, z).equals(this),
				world.getBlock(x+1, y, z).equals(this),
				world.getBlock(x, y-1, z).equals(this),
				world.getBlock(x, y+1, z).equals(this),
				world.getBlock(x, y, z-1).equals(this),
				world.getBlock(x, y, z+1).equals(this),
		};
	}
	
}
