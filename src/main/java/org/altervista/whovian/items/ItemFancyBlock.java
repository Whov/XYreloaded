package org.altervista.whovian.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemFancyBlock extends ItemBlock {

	public ItemFancyBlock(Block block) {
		super(block);
		setHasSubtypes(true);
	}

	@Override
	public String getItemStackDisplayName(ItemStack item){
		String s = super.getItemStackDisplayName(item);
		if (item.getItemDamage()==0) return s;
		return s + " #" + item.getItemDamage()/2;
	}

	@Override
	public int getMetadata(int meta){
		return meta;
	}
	
}
