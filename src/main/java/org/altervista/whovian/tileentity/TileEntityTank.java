package org.altervista.whovian.tileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class TileEntityTank extends TileEntity implements ISidedInventory {
	
	public static final int MAX_VOLUME = 216;//cubo 6*6*6 circa
	public int[] active;//if initialized then fetch all methods to this main thisEntityTank
	public List checked = new ArrayList();
	public HashMap<Integer, Integer> layers;
	
	public ItemStack[] Slots = new ItemStack[2];
	public FluidTank tank;

	@Override
	public void updateEntity() {
 		if (this.tank==null||this.tank.getFluidAmount()>=this.tank.getCapacity()||!FluidContainerRegistry.isFilledContainer(Slots[0])) return;
 		if (Slots[1]!=null&&Slots[1].getItem()!=Items.bucket) return;
		FluidStack inputFluid = FluidContainerRegistry.getFluidForFilledItem(Slots[0]);
		if (this.tank.fill(inputFluid, false)==inputFluid.amount) {
			if (FluidContainerRegistry.isBucket(Slots[0])) {
				if (Slots[1]==null) Slots[1] = new ItemStack(Items.bucket);
				else Slots[1].stackSize+=1;
			}
			Slots[0].stackSize-=1;
			if (Slots[0].stackSize==0) Slots[0]=null;
			this.tank.fill(inputFluid, true);
 		}
		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	   
    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        if (tank==null) return;
        if (tank.getCapacity()==0) return;
        tag.setBoolean("tankExists", true);
        //System.out.println("Writing to NBT...");
        tag.setIntArray("act", active);
        tag.setInteger("cap", tank.getCapacity());
        NBTTagCompound tk = new NBTTagCompound();
        this.tank.writeToNBT(tk);
        tag.setTag("data", tk);
        NBTTagCompound tkP = new NBTTagCompound();
        tkP.setInteger("size", checked.size());
        for (int i=0;i<checked.size();i++) {
        	tkP.setIntArray(String.valueOf(i), (int[])checked.get(i));
        }
        tag.setTag("parts", tkP);
        NBTTagCompound tkL = new NBTTagCompound();
        tkL.setInteger("size", layers.size());
        tkL.setInteger("lowest", layers.get(-1));
        for (int i=layers.get(-1);i<layers.size()+layers.get(-1)-1;i++) {//da layer + basso a più alto (a -1 è associato il livello più basso, ma non va contato nel loop
        	tkL.setInteger(String.valueOf(i), layers.get(i));
        }
        tag.setTag("layers", tkL);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        //System.out.println("Beginning to read NBT data... Expect a 'According to plan' message right after this");
        if (!tag.hasKey("tankExists")) return;
        //System.out.println("According to plan");
        this.active = tag.getIntArray("act");
        this.tank = new FluidTank(tag.getInteger("cap"));
        this.tank.readFromNBT(tag.getCompoundTag("data"));
        this.checked.clear();
        this.layers = new HashMap<Integer, Integer>();
        NBTTagCompound tkP = (NBTTagCompound) tag.getTag("parts");
        for (int i=0;i<tkP.getInteger("size");i++) {
        	this.checked.add(tkP.getIntArray(String.valueOf(i)));
        }
        NBTTagCompound tkL = (NBTTagCompound) tag.getTag("layers");
        this.layers.put(-1, tkL.getInteger("lowest"));
        int i = layers.get(-1);
        while (tkL.hasKey(String.valueOf(i))) {//dal più basso livello in sù finché esistono
        	this.layers.put(i, tkL.getInteger(String.valueOf(i)));
        	i++;
        }
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
	public int getSizeInventory() {
		return this.Slots.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return Slots[slot];
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
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.Slots[slot] = stack;
		if (stack!=null&&stack.stackSize>this.getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public String getInventoryName() {
		return "XyReloaded Tank";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return false;
	}

	@Override
	public void openInventory() {
		
	}

	@Override
	public void closeInventory() {
		
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (slot==0) return true;
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		return null;
	}

	@Override
	public boolean canInsertItem(int var1, ItemStack var2, int var3) {
		return false;
	}

	@Override
	public boolean canExtractItem(int var1, ItemStack var2, int var3) {
		return false;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}
}
