package org.altervista.whovian.items;

import org.altervista.whovian.xyreloaded.XYreloaded;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemFabUpgrade extends ItemBlock {

	public String[] names = new String[] {"Blank Booster", "Recipe Booster", "Speed Booster", "Storage Booster"};
	private IIcon[] icon;
	
	public ItemFabUpgrade(Block block) {
		super(block);
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
    		icon[i] = reg.registerIcon(XYreloaded.MODID+":fabUp/"+names[i]);
    	}
    }
	
}
