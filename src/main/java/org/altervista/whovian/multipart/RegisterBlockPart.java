package org.altervista.whovian.multipart;

import java.util.HashSet;
import java.util.Set;

import org.altervista.whovian.xyreloaded.XYreloaded;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.world.World;
import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.TMultiPart;

public class RegisterBlockPart implements MultiPartRegistry.IPartFactory, MultiPartRegistry.IPartConverter {
	public Set<Block> t = null;
	Block block = null;
	Class<? extends TMultiPart> part = null;
	String name = "";
	
	public RegisterBlockPart(Block block, Class<? extends TMultiPart> part)
	{
		try
		{
			this.name = ((TMultiPart)part.getConstructor(new Class[0]).newInstance(new Object[0])).getType();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public RegisterBlockPart(Block block, Class<? extends TMultiPart> part, String name)
	{
		this.block = block;
		this.part = part;
		this.name = name;
	}
	
	public TMultiPart createPart(String name, boolean client) {
		if (name.equals(name)) {
			try
			{
				return (TMultiPart)this.part.getConstructor().newInstance();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	
	public void init()
	{
		if ((this.name == "") || (this.block == null) || (this.part == null)) {
			return;
		}
		MultiPartRegistry.registerConverter(this);
		MultiPartRegistry.registerParts(this, new String[] { this.name });
	}
	
	public Iterable<Block> blockTypes()
	{
		if (this.t == null)
		{
			this.t = new HashSet();
			this.t.add(this.block);
		}
		return this.t;
	}

	public TMultiPart convert(World world, BlockCoord pos) {
		Block id = world.getBlock(pos.x, pos.y, pos.z);
	    if (id == XYreloaded.blockWire) {
			return new WirePart();
	    }
	    return null;
	}
}
