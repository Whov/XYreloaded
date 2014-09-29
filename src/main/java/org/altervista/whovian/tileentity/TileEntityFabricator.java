package org.altervista.whovian.tileentity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import codechicken.lib.vec.BlockCoord;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityFabricator extends TileEntity implements ISidedInventory {

	private ItemStack[] SlotsPrevious;
	public List<ShapedRecipes> recipes = new ArrayList();
	//TODO
	//More recipes just to craft more things, not directly used
	//TODO
	//Crafting grids bigger than 3x3?
	//TODO
	//Sided fabricator with different inventories?
	public BlockCoord master;
	public List<BlockCoord> upgrades = new ArrayList();
	public int size;
	
	//Upgradable
	public int speed=40;
	public ItemStack[] Slots;
	public int RecipeLimit = 1;
	
	private boolean forceUpdate;
	private int ticks;
	
	@Override
	public void updateEntity() {
		if (worldObj.isRemote) return;
		if (Slots==null) return;
		if (Arrays.deepEquals(Slots, SlotsPrevious)&&!forceUpdate) return;
		if (ticks<speed) ticks++; else {
			ticks=0;
			SlotsPrevious = Arrays.copyOf(Slots, Slots.length);
			forceUpdate=false;
			tryCrafting();//if something is crafted slots will change and slotsPrevious will desync and, next tick, check again
		}
	}
	
	public void sendDescUpdate() {
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	private void tryCrafting() {
		ItemStack[] required; int[] pullFrom;
		for (ShapedRecipes recipe:recipes) {
			int recipeI=0;
			//My shitty way to get the data I need
			required = ((ShapedRecipes) recipe).recipeItems;
			pullFrom = tryConsumeItems(recipe, required);

			if (pullFrom[0]==-1) continue;
			
			for (int i=1;i<pullFrom.length;i++) {
				if (--Slots[pullFrom[i]].stackSize==0) Slots[pullFrom[i]]=null;
			}
			if (Slots[pullFrom[0]]==null)
				Slots[pullFrom[0]] = recipe.getRecipeOutput().copy();
			else Slots[pullFrom[0]].stackSize+=recipe.getRecipeOutput().stackSize;
			forceUpdate=true;
		}
	}
	
	private int[] tryConsumeItems(IRecipe recipe, ItemStack[] required) {//pullFrom[0] is slot in which output. All others are input
		List<Integer> pullFrom = new ArrayList<Integer>(); int maxStackSize = Math.min(recipe.getRecipeOutput().getMaxStackSize(), this.getInventoryStackLimit());
		pullFrom.add(-1);//output
		requirements:
		for (ItemStack req:required) {
			if (req==null) continue;
			for (int i=0;i<Slots.length;i++) {
				if (pullFrom.get(0)==-1&&(Slots[i]==null||Slots[i].isItemEqual(recipe.getRecipeOutput())&&Slots[i].stackSize<maxStackSize)) pullFrom.set(0, i);
				else if (Slots[i]!=null&&Slots[i].isItemEqual(req)) {
					int remains = Slots[i].stackSize;
					for (int minus:pullFrom) {
						if (i==minus) remains--;
					}
					if (remains>0)
						pullFrom.add(i);
					else continue;
					continue requirements;
				}
			}
			pullFrom.set(0, -1);
			break;
		}
		int size = pullFrom.size();
		int[] arr = new int[size];
		for (int i=0;i<size;i++)
			arr[i] = pullFrom.get(i);
		return arr;
	}
	
	public TileEntityFabricator() {
		Slots = new ItemStack[9];
		SlotsPrevious = new ItemStack[9];
	}
	
	public TileEntityFabricator getMaster() {
		if (master==null) return this;
		return (TileEntityFabricator) worldObj.getTileEntity(master.x, master.y, master.z);
	}
	
	public boolean isMaster() {
		if (master==null) return false;
		return master.x==xCoord&&master.y==yCoord&&master.z==zCoord;
	}
	
	@Override
	public int getSizeInventory() {
		return getMaster().Slots.length;
	}
	
	@Override
	public ItemStack getStackInSlot(int number) {
		return getMaster().Slots[number];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		ItemStack[] Slots = getMaster().Slots;
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
        NBTTagList tagList = tagCompound.getTagList("Slots", 10);
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
        tagCompound.setTag("Slots", itemList);
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
    
	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		int[] accessible = new int[getMaster().Slots.length];
		for (int i=0;i<Slots.length;i++)
			accessible[i] = i;
		return accessible;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack item, int side) {
		return true;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack item, int side) {
		return true;
	}
	
}
