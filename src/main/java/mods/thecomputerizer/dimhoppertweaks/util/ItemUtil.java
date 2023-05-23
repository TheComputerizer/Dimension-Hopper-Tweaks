package mods.thecomputerizer.dimhoppertweaks.util;

import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemUtil {

    public static NBTTagCompound getOrCreateTag(ItemStack stack) {
        if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
        return stack.getTagCompound();
    }

    public static String getTranslationForType(String type, String name) {
        return I18n.format(type + "." + Constants.MODID + "." + name + ".name");
    }
}
