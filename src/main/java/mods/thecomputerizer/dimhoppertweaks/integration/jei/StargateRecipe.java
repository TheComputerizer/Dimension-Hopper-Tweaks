package mods.thecomputerizer.dimhoppertweaks.integration.jei;

import mcp.MethodsReturnNonnullByDefault;
import mods.thecomputerizer.dimhoppertweaks.registry.ItemRegistry;
import net.minecraft.item.ItemStack;

import java.util.List;

@MethodsReturnNonnullByDefault
public class StargateRecipe {

    private final int dimension;
    private final List<ItemStack> inputs;
    private final ItemStack output;

    public StargateRecipe(int dimension, List<ItemStack> inputs) {
        this.dimension = dimension;
        this.inputs = inputs;
        this.output = new ItemStack(ItemRegistry.STARGATE_ADDRESSER);
    }

    public int getDimension() {
        return dimension;
    }

    public List<ItemStack> getInputs() {
        return this.inputs;
    }

    public ItemStack getOutput() {
        return this.output;
    }
}
