package org.altervista.whovian.tileentity;

import org.altervista.whovian.multipart.WirePart;

import codechicken.multipart.TileMultipart;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityWire extends TileEntity {

	public int rdLevel;
	public int[] connections = new int[]{-1,-1,-1,-1,-1,-1};
	
	@Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
		tag.setInteger("rd", rdLevel);
		tag.setIntArray("cnctns", connections);
	}
	
	@Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.rdLevel=tag.getInteger("rd");
        this.connections=tag.getIntArray("cnctns");
	}
	
	public void checkConnection(ForgeDirection dir) {
		TileEntity tile;
		tile = this.worldObj.getTileEntity(this.xCoord+dir.offsetX, this.yCoord+dir.offsetY, this.zCoord+dir.offsetZ);
		if (tile==null) {
			connections[dir.ordinal()] = 0;
        } else if (tile instanceof TileEntityWire) {
        	if (getWorldObj().getBlockMetadata(xCoord, yCoord, zCoord)==getWorldObj().getBlockMetadata(tile.xCoord, tile.yCoord, tile.zCoord))
        		connections[dir.ordinal()] = 1;
        } else if (tile instanceof TileMultipart) {
        	try {//because could be in another face
        		TileMultipart multi = (TileMultipart) tile;
        		WirePart wire = (WirePart) multi.partMap(this.getWorldObj().getBlockMetadata(xCoord, yCoord, zCoord));
        		if (multi.partMap(dir.ordinal())==null)
        			connections[dir.ordinal()] = 1;
        	} catch (Exception e) {}
        }
	}
	
    @Override
    public Packet getDescriptionPacket() {//spedisce pacchetto
    	Packet packet = super.getDescriptionPacket();
    	NBTTagCompound tag = packet != null ? ((S35PacketUpdateTileEntity)packet).func_148857_g() : new NBTTagCompound();
        writeToNBT(tag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {//riceve il pacchetto di sopra
        super.onDataPacket(net, pkt);
        NBTTagCompound tag = pkt.func_148857_g();
        readFromNBT(tag);
    }
	
}
