package mods.thecomputerizer.dimhoppertweaks.mixin.mods.access;

import morph.avaritia.client.render.entity.ModelArmorInfinity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = ModelArmorInfinity.class, remap = false)
public interface ModelArmorInfinityAccess {

    @Accessor ModelArmorInfinity.Overlay getInvulnOverlay();
    @Accessor ModelArmorInfinity.Overlay getOverlay();
}