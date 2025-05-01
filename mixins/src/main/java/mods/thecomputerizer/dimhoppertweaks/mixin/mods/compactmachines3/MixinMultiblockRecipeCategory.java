package mods.thecomputerizer.dimhoppertweaks.mixin.mods.compactmachines3;

import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import org.dave.compactmachines3.jei.MultiblockRecipeCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;


@Mixin(value = MultiblockRecipeCategory.class,remap = false)
public abstract class MixinMultiblockRecipeCategory<W extends IRecipeWrapper> implements IRecipeCategory<W>, ITooltipCallback<ItemStack> {

    @Shadow @Final private IDrawableStatic slotDrawable;

    @Override
    public void drawExtras(Minecraft mc) {
        this.slotDrawable.draw(mc,0,0);
        this.slotDrawable.draw(mc,0,19);
        this.slotDrawable.draw(mc,0,38);
        this.slotDrawable.draw(mc,0,57);
        this.slotDrawable.draw(mc,0,76);
        this.slotDrawable.draw(mc,0,95);
        this.slotDrawable.draw(mc,19,0);
        this.slotDrawable.draw(mc,19,19);
        this.slotDrawable.draw(mc,19,38);
        this.slotDrawable.draw(mc,19,57);
        this.slotDrawable.draw(mc,19,76);
        this.slotDrawable.draw(mc,19,95);
        this.slotDrawable.draw(mc,135,76);
        this.slotDrawable.draw(mc,135,0);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0,true,0,0);
        recipeLayout.getItemStacks().init(1,true,0,19);
        recipeLayout.getItemStacks().init(2,true,0,38);
        recipeLayout.getItemStacks().init(3,true,0,57);
        recipeLayout.getItemStacks().init(4,true,0,76);
        recipeLayout.getItemStacks().init(5,true,0,95);
        recipeLayout.getItemStacks().init(6,true,19,0);
        recipeLayout.getItemStacks().init(7,true,19,19);
        recipeLayout.getItemStacks().init(8,true,19,38);
        recipeLayout.getItemStacks().init(9,true,19,57);
        recipeLayout.getItemStacks().init(10,true,19,76);
        recipeLayout.getItemStacks().init(11,true,19,95);
        recipeLayout.getItemStacks().init(12,true,135,76);
        recipeLayout.getItemStacks().init(13,true,135,95);
        recipeLayout.getItemStacks().init(14,false,135,0);
        recipeLayout.getItemStacks().addTooltipCallback(this);
        recipeLayout.getItemStacks().set(ingredients);
    }

    @ModifyConstant(constant = @Constant(intValue = 5), method = "onTooltip(IZLnet/minecraft/item/ItemStack;"+
            "Ljava/util/List;)V")
    private int dimhoppertweaks$modifyTooltipSlots1(int constant) {
        return 11;
    }

    @ModifyConstant(constant = @Constant(intValue = 6), method = "onTooltip(IZLnet/minecraft/item/ItemStack;"+
            "Ljava/util/List;)V")
    private int dimhoppertweaks$modifyTooltipSlots2(int constant) {
        return 12;
    }
}