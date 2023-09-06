package mods.thecomputerizer.dimhoppertweaks.registry.items;

import mods.thecomputerizer.dimhoppertweaks.util.ItemUtil;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class EpicItem extends Item {
    @Override
    @Nonnull
    public net.minecraftforge.common.IRarity getForgeRarity(@Nonnull ItemStack stack) {
        return EnumRarity.EPIC;
    }

    protected NBTTagCompound getTag(ItemStack stack) {
        return ItemUtil.getOrCreateTag(stack);
    }
}
