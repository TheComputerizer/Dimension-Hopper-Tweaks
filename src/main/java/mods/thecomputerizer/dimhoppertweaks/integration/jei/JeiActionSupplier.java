package mods.thecomputerizer.dimhoppertweaks.integration.jei;

import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mods.jei.JEI;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.ingredients.VanillaTypes;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.DelayedModAccess;
import net.minecraft.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
@JEIPlugin
public class JeiActionSupplier implements IModPlugin {

    private static final List<Supplier<IItemStack[]>> queuedRemovals = new ArrayList<>();
    private static final List<Supplier<IItemStack[]>> queuedAdditions = new ArrayList<>();

    public static void injectQueues() {
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
    public void register(IModRegistry registry) {

    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime runtime) {
        queuedAdditions.clear();
        queuedRemovals.clear();
    }

    public static void queueRemovals(Supplier<IItemStack[]> queued) {
        queuedRemovals.add(queued);
    }

    public static void queueAdditions(Supplier<IItemStack[]> queued) {
        queuedAdditions.add(queued);
    }
}
