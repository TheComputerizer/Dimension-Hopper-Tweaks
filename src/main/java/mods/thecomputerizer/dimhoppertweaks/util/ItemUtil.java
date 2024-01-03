package mods.thecomputerizer.dimhoppertweaks.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemUtil {

    public static NBTTagCompound getOrCreateTag(ItemStack stack) {
        if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
        return stack.getTagCompound();
    }

    public static boolean isHolding(EntityPlayer player, Item item) {
        return player.getHeldItemMainhand().getItem()==item || player.getHeldItemOffhand().getItem()==item;
    }
}
