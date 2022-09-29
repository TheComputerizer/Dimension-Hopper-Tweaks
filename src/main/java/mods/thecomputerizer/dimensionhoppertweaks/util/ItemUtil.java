package mods.thecomputerizer.dimensionhoppertweaks.util;

import mods.thecomputerizer.dimensionhoppertweaks.DimensionHopperTweaks;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemUtil {

    public static NBTTagCompound getOrCreateTag(ItemStack stack) {
        return stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
    }

    public static String getTranslationForType(String type, String name) {
        return I18n.format(type + "." + DimensionHopperTweaks.MODID + "." + name + ".name");
    }
}
