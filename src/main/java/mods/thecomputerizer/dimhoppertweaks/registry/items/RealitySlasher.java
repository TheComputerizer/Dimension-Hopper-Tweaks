package mods.thecomputerizer.dimhoppertweaks.registry.items;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

import javax.annotation.Nonnull;

public class RealitySlasher extends ItemSword {

    public RealitySlasher() {
        super(Item.ToolMaterial.DIAMOND);
    }

    @Override
    @Nonnull
    public net.minecraftforge.common.IRarity getForgeRarity(@Nonnull ItemStack stack) {
        return EnumRarity.EPIC;
    }
}
