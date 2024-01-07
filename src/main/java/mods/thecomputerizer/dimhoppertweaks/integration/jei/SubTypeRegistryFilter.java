package mods.thecomputerizer.dimhoppertweaks.integration.jei;

import mcp.MethodsReturnNonnullByDefault;
import mezz.jei.api.ISubtypeRegistry;
import net.minecraft.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SubTypeRegistryFilter implements ISubtypeRegistry.ISubtypeInterpreter {
    @Override
    public String apply(ItemStack stack) {
        return null;
    }
}
