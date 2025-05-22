package mods.thecomputerizer.dimhoppertweaks.mixin.mods.rftools;

import mcjty.rftools.craftinggrid.CraftingRecipe;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = CraftingRecipe.class, remap = false)
public abstract class MixinCraftingRecipe {
}