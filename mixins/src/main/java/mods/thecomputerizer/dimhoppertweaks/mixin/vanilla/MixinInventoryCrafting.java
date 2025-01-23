package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.mixin.api.IInventoryCrafting;
import net.minecraft.inventory.InventoryCrafting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Mixin(value = InventoryCrafting.class)
public abstract class MixinInventoryCrafting implements IInventoryCrafting {

    @Unique private final Set<String> dimhoppertweaks$stages = new HashSet<>();

    @Override
    public void dimhoppertweaks$setStages(Collection<String> stages) {
        this.dimhoppertweaks$stages.addAll(stages);
    }

    @Override
    public Collection<String> dimhoppertweaks$getStages() {
        return this.dimhoppertweaks$stages;
    }
}