package mods.thecomputerizer.dimhoppertweaks.mixin.mods.jei;

import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.plugins.vanilla.VanillaRecipeFactory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;

@Mixin(value = VanillaRecipeFactory.class, remap = false)
public abstract class MixinVanillaRecipeFactory {

    /**
     * @author The_Computerizer
     * @reason No adding anvil recipes
     */
    @Overwrite
    public IRecipeWrapper createAnvilRecipe(ItemStack leftInput, List<ItemStack> rightInputs, List<ItemStack> outputs) {
        return null;
    }

    /**
     * @author The_Computerizer
     * @reason No adding anvil recipes
     */
    @Overwrite
    public IRecipeWrapper createAnvilRecipe(List<ItemStack> leftInputs, List<ItemStack> rightInputs, List<ItemStack> outputs) {
        return null;
    }
}