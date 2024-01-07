package mods.thecomputerizer.dimhoppertweaks.mixin.mods.jei;

import mezz.jei.plugins.vanilla.ingredients.item.ItemStackHelper;
import mezz.jei.startup.StackHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = ItemStackHelper.class, remap = false)
public interface MixinItemStackHelper {

    @Accessor StackHelper getStackHelper();
}
