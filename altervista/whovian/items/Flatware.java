package org.altervista.whovian.items;

import org.altervista.whovian.xyreloaded.XYreloaded;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class Flatware extends Item {

	public String[] names = new String[] {"chopSticks"};
	public IIcon[] icon;
	
	public Flatware() {
		this.setMaxDamage(0);
        this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack item) {
        return names[item.getItemDamage()];
    }

	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {
        return icon[meta];
    }

	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg) {
    	this.icon = new IIcon[names.length];
    	for (int i=0;i<icon.length;i++) {
    		icon[i] = reg.registerIcon(XYreloaded.MODID+":"+names[i]);
    	}
    }
	
}
