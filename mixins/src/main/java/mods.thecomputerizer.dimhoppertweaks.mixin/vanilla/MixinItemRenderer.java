package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import net.minecraft.client.renderer.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {

    /*
    @ModifyVariable(at = @At("HEAD"), method = "renderItemSide", ordinal = 0)
    private ItemStack dimhoppertweaks$renderItemSide(ItemStack stack) {
        if(stack.getItem() instanceof RecipeFunctionItem)
            return ((RecipeFunctionItem)stack.getItem()).transformStack(stack);
        return stack;
    }

    @ModifyVariable(at = @At("HEAD"), method = "renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;" +
            "FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V", ordinal = 0)
    private ItemStack dimhoppertweaks$renderItemInFirstPerson(ItemStack stack) {
        if(stack.getItem() instanceof RecipeFunction)
            return ((RecipeFunction)stack.getItem()).transformStack(stack);
        return stack;
    }
     */
}