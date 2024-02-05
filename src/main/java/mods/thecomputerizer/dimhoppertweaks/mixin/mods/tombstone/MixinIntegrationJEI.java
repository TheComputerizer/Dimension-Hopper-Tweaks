package mods.thecomputerizer.dimhoppertweaks.mixin.mods.tombstone;

import mezz.jei.api.IModRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import ovh.corail.tombstone.compatibility.IntegrationJEI;

@Mixin(value = IntegrationJEI.class, remap = false)
public abstract class MixinIntegrationJEI {

    /**
     * @author The_Computerizer
     * @reason Remove anvil recipes
     */
    @Overwrite
    public void register(IModRegistry registry) {}
}