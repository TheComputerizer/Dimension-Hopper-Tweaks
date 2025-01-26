package mods.thecomputerizer.dimhoppertweaks.mixin.mods.forestry;

import forestry.core.utils.ItemStackUtil;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = ItemStackUtil.class, remap = false)
public class MixinItemStackUtil {
}
