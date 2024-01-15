package mods.thecomputerizer.dimhoppertweaks.mixin.mods.crafttweaker;

import crafttweaker.IAction;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mods.jei.actions.AddItemAction;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = AddItemAction.class, remap = false)
public abstract class MixinAddItemAction implements IAction {

    @Shadow @Final private IItemStack stack;

    @Override
    public void apply() {
        ItemStack mcStack = CraftTweakerMC.getItemStack(this.stack);
        if(!DelayedModAccess.ADDED_ITEMS.contains(mcStack)) DelayedModAccess.ADDED_ITEMS.add(mcStack);
    }
}