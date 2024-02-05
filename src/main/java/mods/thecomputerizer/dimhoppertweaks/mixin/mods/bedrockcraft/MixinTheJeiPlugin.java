package mods.thecomputerizer.dimhoppertweaks.mixin.mods.bedrockcraft;

import bedrockcraft.ModBlocks;
import bedrockcraft.ModItems;
import bedrockcraft.dustInfusion.BedrockDustRecipes;
import bedrockcraft.jei.*;
import bedrockcraft.ritual.RitualManager;
import bedrockcraft.ritual.RitualRecipe;
import mezz.jei.api.IModRegistry;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = TheJeiPlugin.class, remap = false)
public abstract class MixinTheJeiPlugin {


    /**
     * @author The_Computerizer
     * @reason Remove anvil recipes
     */
    @Overwrite
    public void register(IModRegistry registry) {
        registry.addRecipeCatalyst(new ItemStack(ModItems.bedrockDust),"bedrockcraft.jei.c.dust_infusion");
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.pedestal),"bedrockcraftjei.c.ritual");
        registry.handleRecipes(ITransformRecipe.class,
                SimpleTransformWrapper.factory("bedrockcraft.jei.c.informative"),
                "bedrockcraft.jei.c.informative");
        registry.handleRecipes(ITransformRecipe.class,
                SimpleTransformWrapper.factory("bedrockcraft.jei.c.dust_infusion"),
                "bedrockcraft.jei.c.dust_infusion");
        registry.handleRecipes(RitualRecipe.class,RitualWrapper::new,"bedrockcraftjei.c.ritual");
        registry.addRecipes(InformationManager.getRecipes(),"bedrockcraft.jei.c.informative");
        registry.addRecipes(BedrockDustRecipes.getJeiRecipes(),"bedrockcraft.jei.c.dust_infusion");
        registry.addRecipes(RitualManager.recipes(),"bedrockcraftjei.c.ritual");
    }
}