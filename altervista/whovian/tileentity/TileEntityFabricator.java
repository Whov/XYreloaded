package org.altervista.whovian.tileentity;

import codechicken.lib.vec.BlockCoord;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityFabricator extends TileEntity implements IInventory {

	public ItemStack[] Slots;
	public BlockCoord master;
	public int size;
	
	public TileEntityFabricator() {
		Slots = new ItemStack[9];
		size=0;
	}

	public TileEntityFabricator getMaster() {
		if (master!=null) {
			return (TileEntityFabricator) worldObj.getTileEntity(master.x, master.y, master.z);
		} return this;
	}
	
	@Override
	public int getSizeInventory() {
		return Slots.length;
	}
	
	@Override
	public ItemStack getStackInSlot(int number) {
		return Slots[number];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (Slots[slot]!=null) {
			ItemStack stack;
			if (Slots[slot].stackSize<=amount) {
				stack = Slots[slot];
				Slots[slot] = null;
				return stack;
			} else {
				stack = Slots[slot].splitStack(amount);
				if (Slots[slot].stackSize==0) Slots[slot]=null;
				return stack;
			}
			
		}
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.Slots[slot] = stack;
		if (stack!=null&&stack.stackSize>this.getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public String getInventoryName() {
		return "XYreloaded Fabricator";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 32;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this &&
                player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
	}

	@Override
	public void openInventory() {
		
	}

	@Override
	public void closeInventory() {
		
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return true;
	}
	
	@Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        NBTTagList tagList = tagCompound.getTagList("Inventory", 10);
	    int SlotSize = tagCompound.getInteger("SlotSize");
        if (SlotSize>1) {
        	ItemStack[] Slots;
        	if (this.Slots.length!=SlotSize)
        		Slots = new ItemStack[SlotSize];
        	else Slots = this.Slots;
	        for (int i = 0; i < tagList.tagCount(); i++) {
	            NBTTagCompound tag = tagList.getCompoundTagAt(i);
	            byte slot = tag.getByte("Slot");
	            if (slot >= 0 && slot < Slots.length) {
	            	Slots[slot] = ItemStack.loadItemStackFromNBT(tag);
	            }
	        }
	        this.Slots=Slots;
        }
        size = tagCompound.getInteger("size");
        if (tagCompound.hasKey("master")) {
        	int[] pos = tagCompound.getIntArray("master");
        	master = new BlockCoord(pos[0], pos[1], pos[2]);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        NBTTagList itemList = new NBTTagList();
	    tagCompound.setInteger("SlotSize", 0);
        if (Slots!=null) {
		    for (int i = 0; i < Slots.length; i++) {
		        ItemStack stack = Slots[i];
		        if (stack != null) {
		            NBTTagCompound tag = new NBTTagCompound();
		            tag.setByte("Slot", (byte) i);
		            stack.writeToNBT(tag);
		            itemList.appendTag(tag);
		        }
		    }
		    tagCompound.setInteger("SlotSize", Slots.length);
        }
        tagCompound.setTag("Inventory", itemList);
        tagCompound.setInteger("size", size);
        if (master!=null)
        	tagCompound.setIntArray("master", new int[]{master.x, master.y, master.z});
    }
    
    @Override
    public Packet getDescriptionPacket() {
    	Packet packet = super.getDescriptionPacket();
    	NBTTagCompound tag = packet != null ? ((S35PacketUpdateTileEntity)packet).func_148857_g() : new NBTTagCompound();
        writeToNBT(tag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound tag = pkt.func_148857_g();
        readFromNBT(tag);
    }
	
}
