package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.mixin.api.IContainer;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.IInventoryCrafting;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Mixin(value = InventoryCrafting.class)
public abstract class MixinInventoryCrafting implements IInventoryCrafting {
    
    @Shadow @Final public Container eventHandler;
    @Unique private final Set<String> dimhoppertweaks$stages = new HashSet<>();

    @Override
    public void dimhoppertweaks$setStages(Collection<String> stages, boolean clear) {
        if(Objects.nonNull(this.eventHandler)) ((IContainer)this.eventHandler).dimhoppertweaks$setStages(stages,clear);
        else {
            if(clear) this.dimhoppertweaks$stages.clear();
            this.dimhoppertweaks$stages.addAll(stages);
        }
    }

    @Override
    public Collection<String> dimhoppertweaks$getStages() {
        return Objects.nonNull(this.eventHandler) ? ((IContainer)this.eventHandler).dimhoppertweaks$getStages() :
                this.dimhoppertweaks$stages;
    }
}