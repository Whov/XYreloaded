package org.altervista.whovian.client.renderer.container;

import org.altervista.whovian.tileentity.TileEntityTank;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerTank extends Container {
	
	private TileEntityTank tank;

	public ContainerTank(InventoryPlayer invP, TileEntityTank tank) {
		this.tank = tank;
		 for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(invP, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(invP, i, 8 + i * 18, 142));
        }
        this.addSlotToContainer(new Slot(tank, 0, 93, 34));
        this.addSlotToContainer(new Slot(tank, 1, 140, 34));
	}
	
	@Override
    public ItemStack transferStackInSlot(EntityPlayer player, int i) {
		Slot slot = getSlot(i);

		if(slot != null && slot.getHasStack()) {
		    ItemStack itemstack = slot.getStack();
		    ItemStack result = itemstack.copy();
	
		    if(i >= 36) {
		    	if(!mergeItemStack(itemstack, 0, 36, false)) {
		    		return null;
		    	}
		    } else if(!mergeItemStack(itemstack, 36, 36 + tank.getSizeInventory(), false)) {
		    	return null;
		    }
	
		    if(itemstack.stackSize == 0) {
		    	slot.putStack(null);
		    } else {
		    	slot.onSlotChanged();
		    }
		    slot.onPickupFromSlot(player, itemstack); 
		    return result;
		}
		return null;
    }
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

}
