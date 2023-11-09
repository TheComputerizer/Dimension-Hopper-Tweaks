package mods.thecomputerizer.dimhoppertweaks.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemUtil {

    public static NBTTagCompound getOrCreateTag(ItemStack stack) {
        if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
        return stack.getTagCompound();
    }
}
