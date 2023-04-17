package mods.thecomputerizer.dimhoppertweaks.mixin;

import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Particle.class)
public class MixinParticle {

    @Shadow
    protected boolean canCollide;

    @Inject(at = @At(value = "HEAD"), method = "move", cancellable = true)
    private void dimhoppertweaks_move(double x, double y, double z, CallbackInfo ci) {
        this.canCollide = false;
    }
}
