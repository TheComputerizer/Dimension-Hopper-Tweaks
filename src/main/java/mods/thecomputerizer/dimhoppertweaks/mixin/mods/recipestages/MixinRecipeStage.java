package mods.thecomputerizer.dimhoppertweaks.mixin.mods.recipestages;

import com.blamejared.recipestages.recipes.RecipeStage;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.InventoryCraftingAccess;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = RecipeStage.class, remap = false)
public class MixinRecipeStage {

    @Shadow private String tier;

    @Inject(at = @At("RETURN"), method = "isGoodForCrafting", cancellable = true)
    private void dimhoppertweaks$isGoodForCrafting(InventoryCrafting inv, CallbackInfoReturnable<Boolean> cir) {
        if(FMLCommonHandler.instance().getEffectiveSide().isServer())
            cir.setReturnValue(cir.getReturnValueZ() || ((InventoryCraftingAccess)inv).dimhoppertweaks$getStages().contains(this.tier));
    }
}
