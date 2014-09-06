package org.altervista.whovian.client.renderer;

import org.altervista.whovian.client.renderer.container.ContainerFabricator;
import org.altervista.whovian.client.renderer.container.ContainerTank;
import org.altervista.whovian.client.renderer.gui.GUIfabricator;
import org.altervista.whovian.client.renderer.gui.GUIfabricatorConfig;
import org.altervista.whovian.client.renderer.gui.GUItank;
import org.altervista.whovian.tileentity.TileEntityFabricator;
import org.altervista.whovian.tileentity.TileEntityTank;
import org.altervista.whovian.xyreloaded.XYreloaded;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
			case XYreloaded.guiTank: return new ContainerTank(player.inventory, ((TileEntityTank)world.getTileEntity(x, y, z)));
			case XYreloaded.guiFab: return new ContainerFabricator(player.inventory, ((TileEntityFabricator)world.getTileEntity(x, y, z)));
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
			case XYreloaded.guiTank: return new GUItank(player.inventory, ((TileEntityTank)world.getTileEntity(x, y, z)));
			case XYreloaded.guiFab: return new GUIfabricator(player.inventory, ((TileEntityFabricator)world.getTileEntity(x, y, z)));
			case XYreloaded.guiFab2: return new GUIfabricatorConfig(((TileEntityFabricator)world.getTileEntity(x, y, z)));
		}
		return null;
	}

}
