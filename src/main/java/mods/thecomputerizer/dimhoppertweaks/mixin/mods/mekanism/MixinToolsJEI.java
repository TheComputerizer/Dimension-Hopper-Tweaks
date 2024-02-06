package mods.thecomputerizer.dimhoppertweaks.mixin.mods.mekanism;

import mekanism.tools.client.jei.ToolsJEI;
import mezz.jei.api.IModRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = ToolsJEI.class, remap = false)
public abstract class MixinToolsJEI {

    /**
     * @author The_Computerizer
     * @reason Remove anvil recipes
     */
    @Overwrite
    public void register(IModRegistry registry) {
    }
}