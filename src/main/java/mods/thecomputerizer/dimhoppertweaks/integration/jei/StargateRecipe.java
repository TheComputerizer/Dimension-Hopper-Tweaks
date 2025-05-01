package mods.thecomputerizer.dimhoppertweaks.integration.jei;

import lombok.Getter;
import net.minecraft.item.ItemStack;

import java.util.List;

import static mods.thecomputerizer.dimhoppertweaks.registry.ItemRegistry.STARGATE_ADDRESSER;

@Getter
public class StargateRecipe {
    
    private final int dimension;
    private final List<ItemStack> inputs;
    private final ItemStack output;

    public StargateRecipe(int dimension, List<ItemStack> inputs) {
        this.dimension = dimension;
        this.inputs = inputs;
        this.output = new ItemStack(STARGATE_ADDRESSER);
    }
    
}
