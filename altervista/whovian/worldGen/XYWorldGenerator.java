package org.altervista.whovian.worldGen;

import java.util.Random;

import org.altervista.whovian.xyreloaded.XYreloaded;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;

public class XYWorldGenerator implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		switch(world.provider.dimensionId){
		case 0 :
			gen:
			for (int times=0;times<5;times++) {
				int x = random.nextInt(16)+chunkX*16;
				int z = random.nextInt(16)+chunkZ*16;
				int y = world.getTopSolidOrLiquidBlock(x, z);
				for (int i=0;i<5;i++) {
					if (world.getBlock(x, y+i, z).getMaterial()!=Material.water) continue gen;
				}
				Block blockBelow = world.getBlock(x, y-1, z);
				if (blockBelow==Blocks.dirt||blockBelow==Blocks.grass||blockBelow==Blocks.gravel) {
					world.setBlock(x, y, z, XYreloaded.algaBlock, random.nextInt(2), 0);
//					System.out.println(x+" "+y+" "+z);
					if (random.nextDouble() < 0.2D)	return;
				}
			}
		}
	}
}
