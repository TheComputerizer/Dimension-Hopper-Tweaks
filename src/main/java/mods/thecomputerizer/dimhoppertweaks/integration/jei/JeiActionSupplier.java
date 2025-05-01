package mods.thecomputerizer.dimhoppertweaks.integration.jei;

import crafttweaker.IAction;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mods.jei.JEI;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JEIPlugin;
import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.IModRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@JEIPlugin
public class JeiActionSupplier implements IModPlugin {

    private static final List<Supplier<IItemStack[]>> queuedAdditions = new ArrayList<>();
    private static final List<IAction> queuedDescriptionAdditions = new ArrayList<>();
    private static final List<Supplier<IIngredient[]>> queuedDescriptionRemovals = new ArrayList<>();
    private static final List<Supplier<IItemStack[]>> queuedRemovals = new ArrayList<>();

    public static void finish() {
        queuedAdditions.clear();
        queuedDescriptionAdditions.clear();
        queuedDescriptionRemovals.clear();
        queuedRemovals.clear();
    }

    public static void injectDescriptionAdditions() {
        try {
            queuedDescriptionAdditions.forEach(IAction::apply);
        } catch(Exception ex) {
            DHTRef.LOGGER.error("Failed to add 1 or more JEI descriptions!",ex);
        }
    }

    public static void injectDescriptionQueues(IModRegistry access) {
        if(queuedDescriptionRemovals.isEmpty()) return;
        List<Object> removals = new ArrayList<>();
        for(Supplier<IIngredient[]> queue : queuedDescriptionRemovals) {
            for(IIngredient ingredient : queue.get()) {
                if(ingredient instanceof IItemStack) {
                    ItemStack mcStack = CraftTweakerMC.getItemStack((IItemStack)ingredient);
                    if(Objects.nonNull(mcStack) && !mcStack.isEmpty()) removals.add(mcStack);
                } else if(ingredient instanceof ILiquidStack) {
                    FluidStack mcStack = CraftTweakerMC.getLiquidStack((ILiquidStack)ingredient);
                    if(Objects.nonNull(mcStack) && mcStack.amount>0) removals.add(mcStack);
                }
            }
        }
        if(!removals.isEmpty()) access.dimhoppertweaks$clearDescriptions(removals);
    }

    public static void injectVisibilityQueues() {
        if(queuedRemovals.isEmpty() && queuedAdditions.isEmpty()) return;
        for(Supplier<IItemStack[]> queue : queuedRemovals) {
            for(IItemStack stack : queue.get()) {
                ItemStack mcStack = CraftTweakerMC.getItemStack(stack);
                if(Objects.nonNull(mcStack) && !mcStack.isEmpty()) JEI.HIDDEN_ITEMS.add(mcStack);
            }
        }
        for(Supplier<IItemStack[]> queue : queuedAdditions) {
            for(IItemStack stack : queue.get()) {
                ItemStack mcStack = CraftTweakerMC.getItemStack(stack);
                if(Objects.nonNull(mcStack) && !mcStack.isEmpty()) DelayedModAccess.ADDED_ITEMS.add(mcStack);
            }
        }
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime runtime) {
        queuedAdditions.clear();
        queuedRemovals.clear();
    }

    public static void queueAdditions(Supplier<IItemStack[]> supplier) {
        queuedAdditions.add(supplier);
    }

    public static void queueDescriptionRemovals(Supplier<IIngredient[]> supplier) {
        queuedDescriptionRemovals.add(supplier);
    }

    public static void queueDescriptionAdditions(Collection<IAction> actions) {
        queuedDescriptionAdditions.addAll(actions);
    }

    public static void queueRemovals(Supplier<IItemStack[]> supplier) {
        queuedRemovals.add(supplier);
    }

    @Override
    public void register(mezz.jei.api.IModRegistry registry) {

    }
}
