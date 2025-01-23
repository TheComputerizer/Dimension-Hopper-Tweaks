package mods.thecomputerizer.dimhoppertweaks.mixin.mods.naturesaura;

import de.ellpeck.naturesaura.chunk.effect.DrainSpotEffects;
import mods.thecomputerizer.dimhoppertweaks.integration.naturesaura.NaturesAuraSkillEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static de.ellpeck.naturesaura.api.NaturesAuraAPI.DRAIN_SPOT_EFFECTS;
import static mods.thecomputerizer.dimhoppertweaks.integration.naturesaura.NaturesAuraSkillEffect.NAME;

@Mixin(value = DrainSpotEffects.class, remap = false)
public abstract class MixinDrainSpotEffects {

    @Inject(at = @At("TAIL"), method = "init")
    private static void dimhoppertweaks$init(CallbackInfo ci) {
        DRAIN_SPOT_EFFECTS.put(NAME,NaturesAuraSkillEffect::new);
    }
}