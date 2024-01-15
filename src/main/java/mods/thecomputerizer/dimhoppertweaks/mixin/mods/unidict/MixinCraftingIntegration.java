package mods.thecomputerizer.dimhoppertweaks.mixin.mods.unidict;

import blusunrize.immersiveengineering.common.items.ItemIETool;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import wanion.unidict.integration.CraftingIntegration;

@Mixin(value = CraftingIntegration.class, remap = false)
public abstract class MixinCraftingIntegration {

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraftforge/registries/IForgeRegistry;"+
            "register(Lnet/minecraftforge/registries/IForgeRegistryEntry;)V"), method = "lambda$null$3")
    private void dimhoppertweaks$yeetThePlates(IForgeRegistry<IRecipe> registry, IForgeRegistryEntry<IRecipe> entry) {
        boolean register = true;
        hey: {
            if(entry instanceof IRecipe) {
                for(Ingredient ingr : ((IRecipe)entry).getIngredients()) {
                    for(ItemStack stack : ingr.getMatchingStacks()) {
                        if(stack.getItem() instanceof ItemIETool && stack.getMetadata()==0) {
                            register = false;
                            break hey;
                        }
                    }
                }
            }
        }
        if(register && entry instanceof IRecipe) registry.register((IRecipe)entry);
    }
}