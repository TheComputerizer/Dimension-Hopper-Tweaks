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

import static mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess.ADDED_ITEMS;

@Mixin(value = AddItemAction.class, remap = false)
public abstract class MixinAddItemAction implements IAction {

    @Shadow @Final private IItemStack stack;

    @Override
    public void apply() {
        ItemStack mcStack = CraftTweakerMC.getItemStack(this.stack);
        if(!ADDED_ITEMS.contains(mcStack)) ADDED_ITEMS.add(mcStack);
    }
}