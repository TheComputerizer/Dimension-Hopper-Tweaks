package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import mods.thecomputerizer.dimhoppertweaks.mixin.access.ModelArmorInfinityAccess;
import morph.avaritia.client.render.entity.ModelArmorInfinity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ModelArmorInfinity.class, remap = false)
public class MixinModelArmorInfinity implements ModelArmorInfinityAccess {

    @Shadow private ModelArmorInfinity.Overlay overlay;

    @Shadow private ModelArmorInfinity.Overlay invulnOverlay;

    @Override
    public ModelArmorInfinity.Overlay dimhoppertweaks$getOverlay() {
        return this.overlay;
    }

    @Override
    public ModelArmorInfinity.Overlay dimhoppertweaks$getInvulnOverlay() {
        return this.invulnOverlay;
    }
}
