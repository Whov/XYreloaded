package org.altervista.whovian.multipart;

import java.util.Random;

import org.altervista.whovian.blocks.Wire;
import org.altervista.whovian.xyreloaded.Tricks;
import org.altervista.whovian.xyreloaded.Tricks.IRed;
import org.altervista.whovian.xyreloaded.XYreloaded;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Cuboid6;
import codechicken.multipart.IRandomDisplayTick;
import codechicken.multipart.IRedstonePart;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import codechicken.multipart.minecraft.McBlockPart;
import codechicken.multipart.minecraft.McMetaPart;
import codechicken.multipart.minecraft.TorchPart;

public class WirePart extends McMetaPart implements IRedstonePart, IRandomDisplayTick, IRed {

	@Override
	public String getType() {
		return "XY|Wire";
	}

	@Override
	public Cuboid6 getBounds() {
		return new Cuboid6(0.3, 0.3, 0.3, 0.7, 0.7, 0.7);
	}

	@Override
	public Block getBlock() {
		return XYreloaded.blockWire;
	}

	@Override
	public boolean canConnectRedstone(int side) {
		return true;
	}
	
	@Override
	public int strongPowerLevel(int arg0) {
		return meta;
	}

	@Override
	public int weakPowerLevel(int arg0) {
		return meta;
	}

	@Override
	public int getMeta(World world, int x, int y, int z) {
		return meta;
	}

	@Override
	public boolean canSend(ForgeDirection dir) {
		return ((TileMultipart)getTile()).partMap(dir.ordinal())==null;
	}
	
	@Override
	public boolean canReceive(ForgeDirection dir) {
		net.minecraft.tileentity.TileEntity tile = getWorld().getTileEntity(x()+dir.offsetX, y()+dir.offsetY, z()+dir.offsetZ);
		if (tile==null||!(tile instanceof TileMultipart)) return true;
		return ((TileMultipart)tile).partMap(ForgeDirection.OPPOSITES[dir.ordinal()])==null;
	}

	@Override
	public void setMeta(World world, int x, int y, int z, int value) {
		meta=(byte) value;
		this.sendDescUpdate();
	}

	@Override
	public void onPartChanged(TMultiPart part) {
		if (getWorld().isRemote) return;
		int rd = Wire.getStrengthAcrossNetwork(getWorld(), x(), y(), z(), 0);
		System.out.println(rd);
		Wire.updateRedstone(getWorld(), x(), y(), z(), rd);
		sendDescUpdate();
	}
	
	@Override
	public void writeDesc(MCDataOutput packet) {
		packet.writeByte(meta);
	}

	@Override
	public void readDesc(MCDataInput packet) {
		meta = packet.readByte();
	}
	
	@Override
	public void randomDisplayTick(Random rand) {
		int l = meta;

        if (l > 0)
        {
            double d0 = (double)x() + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
            double d1 = (double)((float)y() + 0.0625F);
            double d2 = (double)z() + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
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

            getWorld().spawnParticle("reddust", d0, d1, d2, (double)f1, (double)f2, (double)f3);
        }
	}
	
}
