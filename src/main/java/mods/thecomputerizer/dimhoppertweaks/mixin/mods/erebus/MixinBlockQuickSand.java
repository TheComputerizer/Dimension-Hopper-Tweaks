package mods.thecomputerizer.dimhoppertweaks.mixin.mods.erebus;

import erebus.blocks.BlockQuickSand;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockQuickSand.class, remap = false)
public class MixinBlockQuickSand {
    
    @Inject(at = @At("HEAD"), method = "canEntityWalkOnBlock", cancellable = true)
    private void dimhoppertweaks$canEntityWalkOnBlock(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if(SkillWrapper.isUnstoppable(entity)) cir.setReturnValue(true);
    }
}