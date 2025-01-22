package mods.thecomputerizer.dimhoppertweaks.mixin.mods.netherex;

import logictechcorp.netherex.init.NetherExBiomes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(value = NetherExBiomes.class, remap = false)
public abstract class MixinNetherExBiomes {

    @Redirect(at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
            ordinal = 1), method = "registerBiomeData")
    private static <E> boolean dimhoppertweaks$ignoreAmethystOre(List<E> instance, E e) {
        return false;
    }
}