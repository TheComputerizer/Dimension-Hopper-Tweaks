package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.mixin.api.IGameStageExtension;
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
public abstract class MixinInventoryCrafting implements IGameStageExtension {
    
    @Shadow @Final public Container eventHandler;
    @Unique private final Set<String> dimhoppertweaks$stages = new HashSet<>();

    @Override public void dimhoppertweaks$setStages(boolean clear, String ... stages) {
        if(Objects.nonNull(this.eventHandler))
            ((IGameStageExtension)this.eventHandler).dimhoppertweaks$setStages(clear,stages);
        else {
            if(clear) this.dimhoppertweaks$stages.clear();
            for(String stage : stages) this.dimhoppertweaks$addStage(stage);
        }
    }

    @Override public void dimhoppertweaks$setStages(Collection<String> stages, boolean clear) {
        if(Objects.nonNull(this.eventHandler))
            ((IGameStageExtension)this.eventHandler).dimhoppertweaks$setStages(stages,clear);
        else {
            if(clear) this.dimhoppertweaks$stages.clear();
            for(String stage : stages) dimhoppertweaks$addStage(stage);
        }
    }

    @Override public Collection<String> dimhoppertweaks$getStages() {
        return Objects.nonNull(this.eventHandler) ?
                ((IGameStageExtension)this.eventHandler).dimhoppertweaks$getStages() : this.dimhoppertweaks$stages;
    }

    @Override public void dimhoppertweaks$addStage(String stage) {
        if(Objects.nonNull(stage) && !stage.isEmpty()) this.dimhoppertweaks$stages.add(stage);
    }

    @Override public boolean dimhoppertweaks$hasStage(String stage) {
        return this.dimhoppertweaks$stages.contains(stage);
    }
}