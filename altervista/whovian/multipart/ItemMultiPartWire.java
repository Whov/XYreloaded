package org.altervista.whovian.multipart;

import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Vector3;
import codechicken.multipart.JItemMultiPart;
import codechicken.multipart.TItemMultiPart;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

//for future use
public class ItemMultiPartWire extends ItemBlock {

    @SideOnly(Side.CLIENT)
    private IIcon icon;
    
	public ItemMultiPartWire(Block block) {
		super(block);
	}

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg)
    {
        this.icon = reg.registerIcon("redstone_block");
    }
    
}
