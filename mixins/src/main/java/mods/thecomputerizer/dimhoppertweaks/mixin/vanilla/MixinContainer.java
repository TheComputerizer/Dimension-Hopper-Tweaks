package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.mixin.api.IGameStageExtension;
import net.minecraft.inventory.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Mixin(Container.class)
public abstract class MixinContainer implements IGameStageExtension {

    @Unique private final Set<String> dimhoppertweaks$stages = new HashSet<>();

    @Override public void dimhoppertweaks$setStages(boolean clear, String ... stages) {
        if(clear) this.dimhoppertweaks$stages.clear();
        for(String stage : stages) dimhoppertweaks$addStage(stage);
    }

    @Override public void dimhoppertweaks$setStages(Collection<String> stages, boolean clear) {
        if(clear) this.dimhoppertweaks$stages.clear();
        for(String stage : stages) dimhoppertweaks$addStage(stage);
    }

    @Override public Collection<String> dimhoppertweaks$getStages() {
        return this.dimhoppertweaks$stages;
    }

    @Override public void dimhoppertweaks$addStage(String stage) {
        if(Objects.nonNull(stage) && !stage.isEmpty()) this.dimhoppertweaks$stages.add(stage);
    }

    @Override public boolean dimhoppertweaks$hasStage(String stage) {
        return this.dimhoppertweaks$stages.contains(stage);
    }
}