package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionType.class)
public abstract class MixinDimensionType {

    @Unique private static DimensionType[] dimhoppertweaks$actualTypes;

    @Inject(at = @At("TAIL"), method = "register", remap = false)
    private static void storeRegDimension(String name, String suffix, int id, Class<? extends WorldProvider> provider,
                                          boolean keepLoaded, CallbackInfoReturnable<DimensionType> cir) {
        DimensionType dim = cir.getReturnValue();
        DimensionType[] newArray = new DimensionType[dimhoppertweaks$actualTypes.length+1];
        int i;
        for(i=0; i<dimhoppertweaks$actualTypes.length; i++) newArray[i] = dimhoppertweaks$actualTypes[i];
        newArray[i] = dim;
        dimhoppertweaks$actualTypes = newArray;
    }

    @Inject(at = @At("HEAD"), method = "values", remap = false, cancellable = true)
    private static void getValues(CallbackInfoReturnable<DimensionType[]> cir) {
        cir.setReturnValue(dimhoppertweaks$actualTypes);
    }

    static {
        dimhoppertweaks$actualTypes = new DimensionType[]{DimensionType.OVERWORLD,DimensionType.NETHER,DimensionType.THE_END};
    }
}