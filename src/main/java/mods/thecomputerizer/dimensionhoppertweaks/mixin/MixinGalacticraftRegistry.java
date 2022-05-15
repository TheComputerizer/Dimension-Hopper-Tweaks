package mods.thecomputerizer.dimensionhoppertweaks.mixin;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import mods.thecomputerizer.dimensionhoppertweaks.DimensionHopperTweaks;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = GalacticraftRegistry.class, remap = false)
public class MixinGalacticraftRegistry {

    @Inject(at = @At(value = "HEAD"), method = "registerDimension", cancellable = true)
    private static void registerDimension(String name, String suffix, int id, Class<? extends WorldProvider> provider, boolean keepLoaded, CallbackInfoReturnable<DimensionType> callback) {
        callback.setReturnValue(DimensionType.getById(id));
        DimensionHopperTweaks.LOGGER.info("Manually hooked the galacticraft dimension registry.");
    }
}
