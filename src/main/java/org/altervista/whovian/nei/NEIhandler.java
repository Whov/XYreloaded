package org.altervista.whovian.nei;

import net.minecraft.item.ItemStack;

import org.altervista.whovian.xyreloaded.XYreloaded;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEIhandler implements IConfigureNEI {

	@Override
	public void loadConfig() {
//		API.registerRecipeHandler(new XYCraftingHandler()); for future machines
		API.hideItem(new ItemStack(XYreloaded.riceBlock));
		API.addItemListEntry(new ItemStack(XYreloaded.oBentou, 1, 1));
	}

	@Override
	public String getName() {
		return "NEI Integration in Xy Reloaded";
	}

	@Override
	public String getVersion() {
		return "0.1 alpha";
	}

}
