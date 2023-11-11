package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import org.dave.compactmachines3.jei.MultiblockRecipeWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = MultiblockRecipeWrapper.class, remap = false)
public class MixinMultiblockRecipeWrapper {

    @ModifyConstant(constant = @Constant(intValue = 6), method = "<init>")
    private int dimhoppertweaks$afterInit(int constant) {
        return 12;
    }
}
