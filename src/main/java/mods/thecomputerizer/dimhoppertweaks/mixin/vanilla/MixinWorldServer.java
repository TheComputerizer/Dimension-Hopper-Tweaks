package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.common.skills.SkillWrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldServer.class)
public class MixinWorldServer {

    @Inject(at = @At(value = "HEAD"), method = "setEntityState")
    private void dimhoppertweaks_setEntityState(Entity entityIn, byte state, CallbackInfo info) {
        if(entityIn instanceof EntityPlayerMP)
            SkillWrapper.shieldHook((EntityPlayerMP)entityIn,state);
    }
}
