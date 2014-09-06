package org.altervista.whovian.items;

import org.altervista.whovian.xyreloaded.XYreloaded;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class Obentou extends ItemFood {

	public String[] names = new String[] {"basicBowl", "rice_ball2"};
	public IIcon[] icon;
	
	public Obentou() {
		super(2, 0.4f, false);
		this.setMaxDamage(0);
        this.setHasSubtypes(true);
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		switch (stack.getItemDamage()) {
        case 0: return 128;
        default: return 24;
        }
    }
	
	@Override
	public void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
		if (world.isRemote||world.rand.nextFloat() < 0.8) return;
		switch (stack.getItemDamage()) {
		case 0:
			if (world.rand.nextFloat() < 0.5)
				player.addPotionEffect(new PotionEffect(2, 60 * 20, world.rand.nextInt(2)));
			if (world.rand.nextFloat() < 0.5)
				player.addPotionEffect(new PotionEffect(4, 60 * 20, world.rand.nextInt(2)));
		}
	}

	@Override
	public int func_150905_g(ItemStack stack) {
        switch (stack.getItemDamage()) {
        case 0: return 12;
        default: return 5;
        }
    }

	@Override
    public float func_150906_h(ItemStack stack) {
		switch (stack.getItemDamage()) {
        case 0: return 0.8f;
        default: return 0.4f;
        }
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
