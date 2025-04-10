package mods.thecomputerizer.dimhoppertweaks.registry.items;

import mcp.MethodsReturnNonnullByDefault;
import mods.thecomputerizer.dimhoppertweaks.util.ItemUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.IRarity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static net.minecraft.item.EnumRarity.EPIC;

@MethodsReturnNonnullByDefault @ParametersAreNonnullByDefault
public class EpicItem extends Item {

    @Override public @Nonnull IRarity getForgeRarity(@Nonnull ItemStack stack) {
        return EPIC;
    }

    protected NBTTagCompound getTag(ItemStack stack) {
        return ItemUtil.getOrCreateTag(stack);
    }
}