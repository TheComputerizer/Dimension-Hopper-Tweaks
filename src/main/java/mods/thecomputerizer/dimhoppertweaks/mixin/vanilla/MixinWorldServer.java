package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.common.capability.SkillWrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldServer.class)
public abstract class MixinWorldServer {

    @Inject(at = @At(value = "HEAD"), method = "setEntityState")
    private void dimhoppertweaks$setEntityState(Entity entity, byte state, CallbackInfo info) {
        if(entity instanceof EntityPlayerMP)
            SkillWrapper.shieldHook((EntityPlayerMP)entity,state);
    }
}