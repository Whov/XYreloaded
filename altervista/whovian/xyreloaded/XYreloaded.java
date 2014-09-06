package org.altervista.whovian.xyreloaded;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.altervista.whovian.blocks.*;
import org.altervista.whovian.client.proxy.CommonProxy;
import org.altervista.whovian.client.renderer.GuiHandler;
import org.altervista.whovian.client.renderer.WireSpecialRenderer;
import org.altervista.whovian.items.*;
import org.altervista.whovian.multipart.*;
import org.altervista.whovian.packets.PacketHandler;
import org.altervista.whovian.tileentity.*;
import org.altervista.whovian.worldGen.XYWorldGenerator;

import codechicken.lib.packet.PacketCustom;
import codechicken.multipart.TileMultipart;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid=XYreloaded.MODID, version=XYreloaded.Version, name="XyReloaded", dependencies="after:ForgeMultipart")

public class XYreloaded {
	public static final String MODID = "xyreloaded";
	public static final String Version = "Pre-Alpha 0.02";
	
	public static Block blockBrickBlack;
	public static Block blockBrickWhite;
	public static Block blockBrickRed;
	public static Block blockBrickGreen;
	public static Block blockBrickBlue;
	public static Block blockBrickYellow;
	public static Block blockBrickPink;
	public static Block blockBrickPurple;
	public static Block blockBrickOrange;
	
	public static Block blockGlassViewer;
	public static Block blockInTank;
	public static Block blockValve;
	public static Block blockWire;
	
	public static Block blockABrickBlack;
	public static Block blockABrickWhite;
	public static Block blockABrickRed;
	public static Block blockABrickGreen;
	public static Block blockABrickBlue;
	public static Block blockABrickYellow;
	public static Block blockABrickPink;
	public static Block blockABrickPurple;
	public static Block blockABrickOrange;
	
	public static Block blockALBrickBlack;
	public static Block blockALBrickWhite;
	public static Block blockALBrickRed;
	public static Block blockALBrickGreen;
	public static Block blockALBrickBlue;
	public static Block blockALBrickYellow;
	public static Block blockALBrickPink;
	public static Block blockALBrickPurple;
	public static Block blockALBrickOrange;
	
	public static Block riceBlock;
	public static Block algaBlock;
	public static Block fabricator;
	
	public static Block[] fancy; 
	public static Block[] animated;
	public static int colourAmount;
	
	
	public static Item riceSeeds;
	public static Item flatWare;
	public static Item oBentou;
	public static Item Idummy;
	
	
	@Instance("xyreloaded")
	public static XYreloaded instance;
	
	public static final int guiTank = 0;
	public static final int guiFab = 1;
	public static final int guiFab2 = 2;
	
	public static CreativeTabs XYcraftTab = new CreativeTabs("XYcraftTab") {
		
		@SideOnly(Side.CLIENT)
		@Override
		public Item getTabIconItem() {
			return XYreloaded.oBentou;
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public int func_151243_f() {
			return 1;
		}
	};
	
	@SidedProxy(clientSide="org.altervista.whovian.client.proxy.ClientProxy", serverSide="org.altervista.whovian.xyreloaded.client.proxy.CommonProxy")
    public static CommonProxy proxy;
	
	
	public interface ItankValid {
	}
	
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		blockBrickBlack = new FancyBlock("blockBrickBlack");
		blockBrickWhite = new FancyBlock("blockBrickWhite");
		blockBrickRed = new FancyBlock("blockBrickRed");
		blockBrickGreen = new FancyBlock("blockBrickGreen");
		blockBrickBlue = new FancyBlock("blockBrickBlue");
		blockBrickYellow = new FancyBlock("blockBrickYellow");
		blockBrickPink = new FancyBlock("blockBrickPink");
		blockBrickPurple = new FancyBlock("blockBrickPurple");
		blockBrickOrange = new FancyBlock("blockBrickOrange");
		
		blockGlassViewer = new Glass("blockGlassViewer", "glass/blockGlassViewer_side_");
		blockValve = new Valve();
		blockInTank = new blockInTank();
		blockWire = new Wire();
		riceBlock = new RiceBlock();
		fabricator = new Fabricator();
		riceSeeds = new riceSeeds();
		algaBlock = new Alga();
		flatWare = new Flatware();
		oBentou = new Obentou();
		Idummy = new ItemDummy();
		
		blockABrickBlack = new AnimatedBlock("blockABrickBlack");
		blockABrickWhite = new AnimatedBlock("blockABrickWhite");
		blockABrickRed = new AnimatedBlock("blockABrickRed");
		blockABrickGreen = new AnimatedBlock("blockABrickGreen");
		blockABrickBlue = new AnimatedBlock("blockABrickBlue");
		blockABrickYellow = new AnimatedBlock("blockABrickYellow");
		blockABrickPink = new AnimatedBlock("blockABrickPink");
		blockABrickPurple = new AnimatedBlock("blockABrickPurple");
		blockABrickOrange = new AnimatedBlock("blockABrickOrange");
		
		GameRegistry.registerBlock(blockGlassViewer, "blockglassviewer");
		GameRegistry.registerBlock(blockValve, "blockvalve");
		GameRegistry.registerBlock(blockInTank, "blockintank");
		
		GameRegistry.registerBlock(blockBrickBlack, ItemFancyBlock.class, "blockbrickblack");
		GameRegistry.registerBlock(blockBrickWhite, ItemFancyBlock.class, "blockbrickwhite");
		GameRegistry.registerBlock(blockBrickRed, ItemFancyBlock.class, "blockbrickred");
		GameRegistry.registerBlock(blockBrickGreen, ItemFancyBlock.class, "blockbrickgreen");
		GameRegistry.registerBlock(blockBrickBlue, ItemFancyBlock.class, "blockbrickblue");
		GameRegistry.registerBlock(blockBrickYellow, ItemFancyBlock.class, "blockbrickyellow");
		GameRegistry.registerBlock(blockBrickPink, ItemFancyBlock.class, "blockbrickpink");
		GameRegistry.registerBlock(blockBrickPurple, ItemFancyBlock.class, "blockbrickpurple");
		GameRegistry.registerBlock(blockBrickOrange, ItemFancyBlock.class, "blockbrickorange");
		
		GameRegistry.registerBlock(blockABrickBlack, ItemFancyBlock.class, "blockabrickblack");
		GameRegistry.registerBlock(blockABrickWhite, ItemFancyBlock.class, "blockabrickwhite");
		GameRegistry.registerBlock(blockABrickRed, ItemFancyBlock.class, "blockabrickred");
		GameRegistry.registerBlock(blockABrickGreen, ItemFancyBlock.class, "blockabrickgreen");
		GameRegistry.registerBlock(blockABrickBlue, ItemFancyBlock.class, "blockabrickblue");
		GameRegistry.registerBlock(blockABrickYellow, ItemFancyBlock.class, "blockabrickyellow");
		GameRegistry.registerBlock(blockABrickPink, ItemFancyBlock.class, "blockabrickpink");
		GameRegistry.registerBlock(blockABrickPurple, ItemFancyBlock.class, "blockabrickpurple");
		GameRegistry.registerBlock(blockABrickOrange, ItemFancyBlock.class, "blockabrickorange");
		
		GameRegistry.registerBlock(riceBlock, "riceblock");
		GameRegistry.registerBlock(algaBlock, "algablock");
		GameRegistry.registerBlock(fabricator, "fabricator");
		GameRegistry.registerItem(riceSeeds, "riceseeds");
		GameRegistry.registerItem(flatWare, "flatware");
		GameRegistry.registerItem(oBentou, "obentou");
		GameRegistry.registerItem(Idummy, "dummyitem");
		
		if (Loader.isModLoaded("ForgeMultipart")) {
			GameRegistry.registerBlock(blockWire, ItemMultiPartWire.class, "blockWire");
			new RegisterBlockPart(blockWire, WirePart.class, "XY|Wire").init();
		} else {
			GameRegistry.registerBlock(blockWire, ItemMultiPartWire.class, "blockWire");
		}
		
		GameRegistry.registerWorldGenerator(new XYWorldGenerator(), 5);
		
        MinecraftForge.EVENT_BUS.register(new org.altervista.whovian.events.EventHandler());
        PacketCustom.assignHandler(this, new PacketHandler());

		fancy = new Block[]{blockBrickBlack, blockBrickWhite, blockBrickRed, blockBrickGreen, blockBrickBlue, blockBrickYellow, blockBrickPink, blockBrickPurple, blockBrickOrange};
		animated = new Block[] {blockABrickBlack, blockABrickWhite, blockABrickRed, blockABrickGreen, blockABrickBlue, blockABrickYellow, blockABrickPink, blockABrickPurple, blockABrickOrange};
		
		colourAmount = fancy.length;
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		GameRegistry.registerTileEntity(TileEntityTank.class, "tileTank");
		GameRegistry.registerTileEntity(TileEntityFabricator.class, "tileFab");
		GameRegistry.registerTileEntity(TileEntityWire.class, "tileWire");
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		proxy.registerRenderers();
		
		ItemStack Sbrick = new ItemStack(Blocks.stonebrick);
		ItemStack glass = new ItemStack(Blocks.glass);
		ItemStack torch = new ItemStack(Blocks.torch);
		ItemStack redDye = new ItemStack(Items.dye, 1, 1);
		
		for (int i=0;i<colourAmount;i++) {
			GameRegistry.addShapelessRecipe(new ItemStack(animated[i]), new ItemStack(fancy[i]), glass);
		}
		
		for (Block block:fancy) {
			ItemStack L0 = new ItemStack(block, 1, 0);
			ItemStack L2 = new ItemStack(block, 1, 2);
			ItemStack L4 = new ItemStack(block, 1, 4);
			ItemStack L6 = new ItemStack(block, 1, 6);
			ItemStack L8 = new ItemStack(block, 1, 8);
			ItemStack L10 = new ItemStack(block, 1, 10);
			ItemStack L12 = new ItemStack(block, 1, 12);
			ItemStack L14 = new ItemStack(block, 1, 14);
			ItemStack[] L = {L0, L2, L4, L6, L8, L10, L12, L14};
			int i;
			for (i=0;i<L.length-1;i++) {
				GameRegistry.addShapelessRecipe(L[i+1], torch, L[i]);
			}
			for (i=0;i<L.length-2;i++) {
				GameRegistry.addShapelessRecipe(L[i+2], torch, torch, L[i]);
			}
			for (i=0;i<L.length-3;i++) {
				GameRegistry.addShapelessRecipe(L[i+3], torch, torch, torch, L[i]);
			}
			for (i=0;i<L.length-4;i++) {
				GameRegistry.addShapelessRecipe(L[i+4], torch, torch, torch, torch, L[i]);
			}
			for (i=0;i<L.length-5;i++) {
				GameRegistry.addShapelessRecipe(L[i+5], torch, torch, torch, torch, torch, L[i]);
			}
			for (i=0;i<L.length-6;i++) {
				GameRegistry.addShapelessRecipe(L[i+6], torch, torch, torch, torch, torch, torch, L[i]);
			}
			GameRegistry.addShapelessRecipe(L[7], torch, torch, torch, torch, torch, torch, torch, L[0]);
		}
		for (Block block:animated) {
			ItemStack L0 = new ItemStack(block, 1, 0);
			ItemStack L2 = new ItemStack(block, 1, 2);
			ItemStack L4 = new ItemStack(block, 1, 4);
			ItemStack L6 = new ItemStack(block, 1, 6);
			ItemStack L8 = new ItemStack(block, 1, 8);
			ItemStack L10 = new ItemStack(block, 1, 10);
			ItemStack L12 = new ItemStack(block, 1, 12);
			ItemStack L14 = new ItemStack(block, 1, 14);
			ItemStack[] L = {L0, L2, L4, L6, L8, L10, L12, L14};
			int i;
			for (i=0;i<L.length-1;i++) {
				GameRegistry.addShapelessRecipe(L[i+1], torch, L[i]);
			}
			for (i=0;i<L.length-2;i++) {
				GameRegistry.addShapelessRecipe(L[i+2], torch, torch, L[i]);
			}
			for (i=0;i<L.length-3;i++) {
				GameRegistry.addShapelessRecipe(L[i+3], torch, torch, torch, L[i]);
			}
			for (i=0;i<L.length-4;i++) {
				GameRegistry.addShapelessRecipe(L[i+4], torch, torch, torch, torch, L[i]);
			}
			for (i=0;i<L.length-5;i++) {
				GameRegistry.addShapelessRecipe(L[i+5], torch, torch, torch, torch, torch, L[i]);
			}
			for (i=0;i<L.length-6;i++) {
				GameRegistry.addShapelessRecipe(L[i+6], torch, torch, torch, torch, torch, torch, L[i]);
			}
			GameRegistry.addShapelessRecipe(L[7], torch, torch, torch, torch, torch, torch, torch, L[0]);
		}
		
		ItemStack rice = new ItemStack(riceSeeds);
		
		GameRegistry.addShapelessRecipe(new ItemStack(flatWare, 1, 0), new ItemStack(Items.stick), new ItemStack(Items.stick));
		GameRegistry.addShapelessRecipe(new ItemStack(oBentou, 1, 0), new ItemStack(flatWare, 1, 0), new ItemStack(Items.bowl), rice, rice, rice, rice, rice, rice, rice);
		GameRegistry.addShapedRecipe(new ItemStack(Idummy, 1, 0), "rr", "rr", 'r', rice);
		GameRegistry.addShapelessRecipe(new ItemStack(oBentou, 1, 1), new ItemStack(algaBlock), new ItemStack(Idummy, 1, 0));
		
	}
	
}
