package org.altervista.whovian.client.proxy;

import org.altervista.whovian.blocks.ConnectedTextures;
import org.altervista.whovian.client.renderer.*;
import org.altervista.whovian.tileentity.TileEntityTank;
import org.altervista.whovian.tileentity.TileEntityWire;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
	
	public static int MaisRenderType;
	public static int AlgaRenderType;
	public static int renderPass;
	
	 public void registerRenderers() {
         MaisRenderType = RenderingRegistry.getNextAvailableRenderId();
         AlgaRenderType = RenderingRegistry.getNextAvailableRenderId();
         
         ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTank.class, new TankSpecialRenderer());
         ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWire.class, new WireSpecialRenderer());
         RenderingRegistry.registerBlockHandler(new MaisRenderer());
         RenderingRegistry.registerBlockHandler(new AlgaRenderer());
	 }

}
