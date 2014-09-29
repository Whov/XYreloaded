package org.altervista.whovian.xyreloaded;

import org.altervista.whovian.multipart.WirePart;

import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class Tricks {

	public interface ItankValid {
	}

	public interface IRed {
		public int getMeta(World world, int x, int y, int z);
		public void setMeta(World world, int x, int y, int z, int value);
		boolean canSend(ForgeDirection dir);
		boolean canReceive(ForgeDirection dir);
	}
	
	public static IRed getRed(World world, int x, int y, int z) {
		if (world.getBlock(x, y, z)==XYreloaded.blockWire) return (IRed)world.getBlock(x, y, z);
		else if (world.getTileEntity(x, y, z) instanceof TileMultipart) {
			TileMultipart t = (TileMultipart) world.getTileEntity(x, y, z);
			TMultiPart p = t.jPartList().get(0);
			if (p instanceof WirePart) return (IRed) p;
		}
		return null;
	}
	
}
