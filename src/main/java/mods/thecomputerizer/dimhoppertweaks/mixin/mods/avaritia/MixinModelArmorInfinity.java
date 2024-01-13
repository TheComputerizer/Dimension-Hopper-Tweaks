package mods.thecomputerizer.dimhoppertweaks.mixin.mods.avaritia;

import mods.thecomputerizer.dimhoppertweaks.mixin.access.ModelArmorInfinityAccess;
import morph.avaritia.client.render.entity.ModelArmorInfinity;
import net.minecraft.client.model.ModelBiped;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@Mixin(value = ModelArmorInfinity.class, remap = false)
public abstract class MixinModelArmorInfinity extends ModelBiped implements ModelArmorInfinityAccess {

    @Shadow private ModelArmorInfinity.Overlay invulnOverlay;

    @Shadow private ModelArmorInfinity.Overlay overlay;

    @Override
    public ModelArmorInfinity.Overlay dimhoppertweaks$getOverlay() {
        return this.overlay;
    }

    @Override
    public ModelArmorInfinity.Overlay dimhoppertweaks$getInvulOverlay() {
        return this.invulnOverlay;
    }
}
