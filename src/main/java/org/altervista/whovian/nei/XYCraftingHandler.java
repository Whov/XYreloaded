package org.altervista.whovian.nei;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.api.IRecipeOverlayRenderer;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.ICraftingHandler;
import codechicken.nei.recipe.ShapedRecipeHandler;

public class XYCraftingHandler extends ShapedRecipeHandler {

	@Override
	public String getRecipeName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int numRecipes() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void drawBackground(int recipe) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawForeground(int recipe) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<PositionedStack> getIngredientStacks(int recipe) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PositionedStack> getOtherStacks(int recipetype) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PositionedStack getResultStack(int recipe) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onUpdate() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean hasOverlay(GuiContainer gui, Container container, int recipe) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IRecipeOverlayRenderer getOverlayRenderer(GuiContainer gui,
			int recipe) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IOverlayHandler getOverlayHandler(GuiContainer gui, int recipe) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int recipiesPerPage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<String> handleTooltip(GuiRecipe gui, List<String> currenttip,
			int recipe) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> handleItemTooltip(GuiRecipe gui, ItemStack stack,
			List<String> currenttip, int recipe) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean keyTyped(GuiRecipe gui, char keyChar, int keyCode, int recipe) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseClicked(GuiRecipe gui, int button, int recipe) {
		return false;
	}

	@Override
	public ICraftingHandler getRecipeHandler(String outputId, Object... results) {
		return null;
	}

}
