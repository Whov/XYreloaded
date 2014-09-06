package org.altervista.whovian.multipart;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.altervista.whovian.client.renderer.WireSpecialRenderer;
import org.altervista.whovian.tileentity.TileEntityWire;
import org.altervista.whovian.xyreloaded.XYreloaded;

import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.multipart.INeighborTileChange;
import codechicken.multipart.TileMultipart;
import codechicken.multipart.minecraft.McBlockPart;
import codechicken.multipart.minecraft.McSidedMetaPart;

public class WirePart extends McSidedMetaPart implements INeighborTileChange {
	
	private WireSpecialRenderer rend;
	public int rdLevel;
	public int[] connections = {-1, -1, -1, -1, -1, -1};
	
	public WirePart() {
		super();
	}
	
	public WirePart(int meta) {
		super(meta);
	}
	
	@Override
	public Block getBlock() {
		return XYreloaded.blockWire;
	}
	
    public static McBlockPart placement(World world, BlockCoord pos, int side)
    {
        pos = pos.copy().offset(side^1);
        Block block = world.getBlock(pos.x, pos.y, pos.z);
        if(!block.isSideSolid(world, pos.x, pos.y, pos.z, ForgeDirection.getOrientation(side)))
            return null;
        int[] sideMetaMap = new int[]{5, 0, 3, 4, 1, 2};
        return new WirePart(side);
    }
	
	@Override
	public String getType() {
		return "XY|Wire";
	}
	
	@Override
	public void readDesc(MCDataInput packet) {
		super.readDesc(packet);
		rdLevel = packet.readInt();
		BlockCoord con1 = packet.readCoord();
		BlockCoord con2 = packet.readCoord();
		connections[0] = con1.x; connections[1] = con1.y; connections[2] = con1.z;
		connections[3] = con2.x; connections[4] = con2.y; connections[5] = con2.z;
	}
	
	@Override
	public void writeDesc(MCDataOutput packet) {
		super.writeDesc(packet);
		packet.writeInt(rdLevel);
		packet.writeCoord(connections[0], connections[1], connections[2]);
		packet.writeCoord(connections[3], connections[4], connections[5]);
	}
	
	@Override
	public void renderDynamic(Vector3 pos, float frame, int pass) {
		rend.renderTileEntityAt(tile(), pos.x, pos.y, pos.z, sideForMeta(meta)+1);
	}
	
    @Override
    public Cuboid6 getBounds() {
        if 		(meta == 0) return new Cuboid6(0.3F, 0.8F, 0.3F, 0.7F, 1F, 0.7F);
        else if (meta == 1) return new Cuboid6(0.3F, 0F, 0.3F, 0.7F, 0.2F, 0.7F);
        else if (meta == 2) return new Cuboid6(0.3F, 0.3F, 0.8F, 0.7F, 0.7F, 1F);
        else if (meta == 3) return new Cuboid6(0.3F, 0.3F, 0.0F, 0.7F, 0.7F, 0.2F);
        else if (meta == 4) return new Cuboid6(0.8F, 0.3F, 0.3F, 1.0F, 0.7F, 0.7F);
        else if (meta == 5) return new Cuboid6(0.0F, 0.3F, 0.3F, 0.2F, 0.7F, 0.7F);
        return null;
    }
    
    @Override
    public void onWorldJoin() {
    	super.onWorldJoin();
    	rend = new WireSpecialRenderer();
    	rend.func_147497_a(TileEntityRendererDispatcher.instance);
    }

    @Override
    public void invalidateConvertedTile() {
    	TileEntityWire tile = (TileEntityWire)this.world().getTileEntity(x(), y(), z()); 
    	rdLevel = tile.rdLevel;
    	connections = tile.connections;
    }
    
    //no need for methods like onNeighborChanged to detect if can still stay (since this is attached to a block)
    //because this class extends McSidedMetaPart that already does that

    @Override
    public boolean canStay() {
    	BlockCoord pos = new BlockCoord(tile()).offset(sideForMeta(meta));
    	System.out.println("x:"+pos.x+" y:"+pos.y+" z:"+pos.z);
        if (world().isSideSolid(pos.x, pos.y, pos.z, ForgeDirection.getOrientation(sideForMeta(meta)^1))) return true;
        return (getWorld().getBlock(pos.x, pos.y, pos.z).equals(this));
    }
    
	@Override
	public void onNeighborTileChanged(int side, boolean weak) {
		checkConnection(ForgeDirection.getOrientation(side));
		sendDescUpdate();		
	}
	
    public void checkConnection(ForgeDirection dir) {
		TileEntity tile;
		tile = world().getTileEntity(this.x()+dir.offsetX, this.y()+dir.offsetY, this.z()+dir.offsetZ);
		if (tile==null) {
			connections[dir.ordinal()] = 0;
        } else if (tile instanceof TileEntityWire) {
        	connections[dir.ordinal()] = 1;
        } else if (tile instanceof TileMultipart) {
        	try {//because could be in another face
        		TileMultipart multi = (TileMultipart) tile;
        		WirePart wire = (WirePart) multi.partMap(world().getBlockMetadata(x(), y(), z()));
        		if (multi.partMap(dir.ordinal())==null)
        			connections[dir.ordinal()] = 1;
        	} catch (Exception e) {}
        }
	}
    
    @Override
    public void save(NBTTagCompound tag)
    {
      super.save(tag);
      tag.setInteger("rd", rdLevel);
      tag.setIntArray("arr", connections);
    }

    @Override
    public void load(NBTTagCompound tag)
    {
      super.load(tag);
      if (tag.hasKey("rd")) rdLevel=tag.getInteger("rd");
      if (tag.hasKey("arr")) connections=tag.getIntArray("arr");
    }
    
	@Override
	public int sideForMeta(int meta) {
		return ForgeDirection.getOrientation(meta).getOpposite().ordinal();
	}

	@Override
	public boolean weakTileChanges() {
		return false;
	}
    
}
