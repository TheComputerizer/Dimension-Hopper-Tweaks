package mods.thecomputerizer.dimhoppertweaks.mixin.mods.thebetweenlands;

import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thebetweenlands.common.block.terrain.BlockMud;

@Mixin(value = BlockMud.class, remap = false)
public abstract class MixinBlockMud {
    
    @Inject(at = @At("HEAD"), method = "canEntityWalkOnMud", cancellable = true)
    private void dimhoppertweaks$canEntityWalkOnMud(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if(SkillWrapper.isUnstoppable(entity)) cir.setReturnValue(true);
    }
}