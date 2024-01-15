package mods.thecomputerizer.dimhoppertweaks.mixin.mods.loottweaker;

import leviathan143.loottweaker.common.mutable_loot.MutableLootPool;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = MutableLootPool.class, remap = false)
public abstract class MixinMutableLootPool {

    @Redirect(at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;" +
            "Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V"),
            method = "<init>(Lnet/minecraft/world/storage/loot/LootPool;)V")
    private void dimhoppertweaks$noMoreSanityChecks(Logger instance, String s, Object o, Object o1, Object o2) {}
}