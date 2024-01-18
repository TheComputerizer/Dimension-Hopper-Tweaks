package mods.thecomputerizer.dimhoppertweaks.mixin.mods.naturesaura;

import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import de.ellpeck.naturesaura.chunk.effect.DrainSpotEffects;
import mods.thecomputerizer.dimhoppertweaks.integration.naturesaura.NaturesAuraSkillEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DrainSpotEffects.class, remap = false)
public abstract class MixinDrainSpotEffects {

    @Inject(at = @At("TAIL"), method = "init")
    private static void dimhoppertweaks$init(CallbackInfo ci) {
        NaturesAuraAPI.DRAIN_SPOT_EFFECTS.put(NaturesAuraSkillEffect.NAME,NaturesAuraSkillEffect::new);
    }
}