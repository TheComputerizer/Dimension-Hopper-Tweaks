package mods.thecomputerizer.dimhoppertweaks.mixin.mods.openblocks;

import net.minecraft.entity.Entity;
import openblocks.common.TrophyHandler.Trophy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;


@Mixin(value = Trophy.class, remap = false)
public class MixinTrophy {

    @Inject(at = @At("RETURN"), method = "createEntity")
    private void dimhoppertweaks$createEntity(CallbackInfoReturnable<Entity> cir) {
        Entity entity = cir.getReturnValue();
        if(Objects.nonNull(entity)) entity.getEntityData().setBoolean("isFakeEntityForMoBends",true);
    }
}
