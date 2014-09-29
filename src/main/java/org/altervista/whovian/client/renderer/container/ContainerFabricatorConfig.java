package org.altervista.whovian.client.renderer.container;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.altervista.whovian.tileentity.TileEntityFabricator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;

public class ContainerFabricatorConfig extends Container {

	public TileEntityFabricator tile;

	/** The crafting matrix inventory (3x3). */
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
    
    public IInventory craftResult = new InventoryCraftResult();
    
    public ContainerFabricatorConfig(InventoryPlayer invP, TileEntityFabricator t) {
    	tile=t;
		
        this.addSlotToContainer(new SlotCrafting(invP.player, this.craftMatrix, this.craftResult, 0, 125, 35));
        int l;
        int i1;

        for (l = 0; l < 3; ++l) {
            for (i1 = 0; i1 < 3; ++i1) {
                this.addSlotToContainer(new Slot(this.craftMatrix, i1 + l * 3, 31 + i1 * 18, 17 + l * 18));
            }
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(invP, j + i * 9 + 9, 9 + j * 18, 84 + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(invP, i, 9 + i * 18, 142));
        }
        
        this.onCraftMatrixChanged(this.craftMatrix);
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    public void onCraftMatrixChanged(IInventory p_75130_1_)
    {
        this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, tile.getWorldObj()));
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
		    } else if(!mergeItemStack(itemstack, 36, 36 + tile.getSizeInventory(), false)) {
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
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return true;
	}
	
}
