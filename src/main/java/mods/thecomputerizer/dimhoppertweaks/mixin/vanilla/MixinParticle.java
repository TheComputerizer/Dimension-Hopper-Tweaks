package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Particle.class)
public class MixinParticle {

    @Shadow protected boolean canCollide;

    @Inject(at = @At(value = "HEAD"), method = "move")
    private void dimhoppertweaks$move(double x, double y, double z, CallbackInfo ci) {
        this.canCollide = false;
    }

    /**
     * @author The_Computerizer
     * @reason Make particles a bit less expensive
     */
    @Overwrite
    public int getBrightnessForRender(float partialTick) {
        return 15 << 20 | 15 << 4;
    }
}
