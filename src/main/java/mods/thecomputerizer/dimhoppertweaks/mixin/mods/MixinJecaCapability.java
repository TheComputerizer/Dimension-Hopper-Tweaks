package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import me.towdium.jecalculation.JecaCapability;
import me.towdium.jecalculation.data.structure.RecordPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(value = JecaCapability.class, remap = false)
public class MixinJecaCapability {

    @Redirect(at = @At(value = "INVOKE", target = "Lme/towdium/jecalculation/JecaCapability$Container;" +
            "getRecord()Lme/towdium/jecalculation/data/structure/RecordPlayer;"), method = "getRecord")
    private static RecordPlayer dimhoppertweaks$redirectGetRecord(JecaCapability.Container container) {
        return Objects.nonNull(container) ? container.getRecord() : null;
    }
}
