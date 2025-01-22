package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Particle.class)
public abstract class MixinParticle {

    /**
     * @author The_Computerizer
     * @reason Make particles a bit less expensive
     */
    @Overwrite
    public int getBrightnessForRender(float partialTick) {
        return 15 << 20 | 15 << 4;
    }
}