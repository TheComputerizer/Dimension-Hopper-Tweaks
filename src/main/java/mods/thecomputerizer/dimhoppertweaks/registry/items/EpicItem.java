package mods.thecomputerizer.dimhoppertweaks.registry.items;

import mcp.MethodsReturnNonnullByDefault;
import mods.thecomputerizer.dimhoppertweaks.util.ItemUtil;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.IRarity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class EpicItem extends Item {
    @Override
    public @Nonnull IRarity getForgeRarity(@Nonnull ItemStack stack) {
        return EnumRarity.EPIC;
    }

    protected NBTTagCompound getTag(ItemStack stack) {
        return ItemUtil.getOrCreateTag(stack);
    }
}
