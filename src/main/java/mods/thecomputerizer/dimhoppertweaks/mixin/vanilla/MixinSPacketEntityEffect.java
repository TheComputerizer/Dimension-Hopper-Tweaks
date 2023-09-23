package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.mixin.access.SPacketEntityEffectAccess;
import net.minecraft.network.play.server.SPacketEntityEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SPacketEntityEffect.class)
public class MixinSPacketEntityEffect implements SPacketEntityEffectAccess {

    @Unique private boolean dimhoppertweaks$isServerErrored;

    @Inject(at = @At("RETURN"), method = "<init>()V")
    private void dimhoppertweaks$onEmptyConstructed(CallbackInfo ci) {
        this.dimhoppertweaks$isServerErrored = true;
    }

    @Override
    public boolean dimhoppertweaks$isNotErrored() {
        return !this.dimhoppertweaks$isServerErrored;
    }
}
