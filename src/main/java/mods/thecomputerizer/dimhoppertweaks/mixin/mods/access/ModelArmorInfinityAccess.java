package mods.thecomputerizer.dimhoppertweaks.mixin.mods.access;

import morph.avaritia.client.render.entity.ModelArmorInfinity;
import morph.avaritia.client.render.entity.ModelArmorInfinity.Overlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = ModelArmorInfinity.class, remap = false)
public interface ModelArmorInfinityAccess {

    @Accessor Overlay getInvulnOverlay();
    @Accessor Overlay getOverlay();
}