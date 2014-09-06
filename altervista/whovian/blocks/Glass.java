package org.altervista.whovian.blocks;

import net.minecraft.block.material.Material;

import org.altervista.whovian.xyreloaded.XYreloaded;
import org.altervista.whovian.xyreloaded.XYreloaded.ItankValid;

public class Glass extends ConnectedTextures implements ItankValid {

	public Glass(String name, String path) {
		super(Material.rock, path, XYreloaded.MODID);
		this.setBlockName(name);
		setLightOpacity(0);
		this.Falone=0;
		this.Fjust1=1;
		this.Fcorner=5;
		this.FinLine=9;
	}

	@Override
	public boolean isOpaqueCube() {return false;}
	
	
}
