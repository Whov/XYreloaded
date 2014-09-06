package org.altervista.whovian.client.renderer.container;

import org.altervista.whovian.tileentity.TileEntityFabricator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFabricator extends Container {

	private TileEntityFabricator fab;

	public ContainerFabricator(InventoryPlayer invP, TileEntityFabricator fab) {
		this.fab = fab;
		 for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(invP, j + i * 9 + 9, 8 + j * 18, 140 + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(invP, i, 8 + i * 18, 198));
        }
        for (int i=0;i<fab.Slots.length/9;i++) { for (int j=0;j<9;j++) {
        	this.addSlotToContainer(new Slot(fab, 9*i+j, 8+18*j, 18*(i+1)));
        }}
	}

	@Override
    public ItemStack transferStackInSlot(EntityPlayer player, int i) {
		Slot slot = getSlot(i);//shift-clicked

		if(slot != null && slot.getHasStack()) {
		    ItemStack itemstack = slot.getStack();
		    ItemStack result = itemstack.copy();
		    if(i >= 36) {
		    	if(!mergeItemStack(itemstack, 0, 36, false)) {
		    		return null;
		    	}
		    } else if(!mergeItemStack(itemstack, 36, 36 + fab.getSizeInventory(), false)) {
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
	protected boolean mergeItemStack(ItemStack p_75135_1_, int p_75135_2_, int p_75135_3_, boolean p_75135_4_)
    {
        boolean flag1 = false;
        int k = p_75135_2_;

        if (p_75135_4_)
        {
            k = p_75135_3_ - 1;
        }

        Slot slot;
        ItemStack itemstack1;
        //MY NERF HERE!
        int maxStackSize;
		if (p_75135_2_!=0)
            maxStackSize = Math.min(p_75135_1_.getMaxStackSize(), fab.getInventoryStackLimit());
        else maxStackSize = p_75135_1_.getMaxStackSize();

        if (p_75135_1_.isStackable())
        {
            while (p_75135_1_.stackSize > 0 && (!p_75135_4_ && k < p_75135_3_ || p_75135_4_ && k >= p_75135_2_))
            {
                slot = (Slot)this.inventorySlots.get(k);
                itemstack1 = slot.getStack();

                //if found a match
                if (itemstack1 != null && itemstack1.getItem() == p_75135_1_.getItem() && (!p_75135_1_.getHasSubtypes() || p_75135_1_.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(p_75135_1_, itemstack1))
                {
                    int l = itemstack1.stackSize + p_75135_1_.stackSize;
                    
					//if everything fits in one slot
                    if (l <= maxStackSize)
                    {
                        p_75135_1_.stackSize = 0;
                        itemstack1.stackSize = l;
                        slot.onSlotChanged();
                        flag1 = true;
                    }//else just decrease
                    else if (itemstack1.stackSize < maxStackSize)
                    {
                        p_75135_1_.stackSize -= maxStackSize - itemstack1.stackSize;
                        itemstack1.stackSize = maxStackSize;
                        slot.onSlotChanged();
                        flag1 = true;
                    }
                }

                if (p_75135_4_)
                {
                    --k;
                }
                else
                {
                    ++k;
                }
            }
        }

        //if more, start using empty slots
        if (p_75135_1_.stackSize > 0)
        {
            if (p_75135_4_)
            {
                k = p_75135_3_ - 1;
            }
            else
            {
                k = p_75135_2_;
            }

            while (!p_75135_4_ && k < p_75135_3_ || p_75135_4_ && k >= p_75135_2_)
            {
                slot = (Slot)this.inventorySlots.get(k);
                itemstack1 = slot.getStack();

                if (itemstack1 == null)
                {
                	itemstack1 = p_75135_1_.copy();
                	itemstack1.stackSize=Math.min(maxStackSize, itemstack1.stackSize);
                    slot.putStack(itemstack1.copy());
                    slot.onSlotChanged();
                    p_75135_1_.stackSize -= itemstack1.stackSize;
                    //if finished
                    if (p_75135_1_.stackSize==0) {
                		flag1 = true;
                		break;
                	}
                }

                if (p_75135_4_)
                {
                    --k;
                }
                else
                {
                    ++k;
                }
            }
        }

        return flag1;
    }
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}
	
}
