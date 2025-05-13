package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.mixin.api.IContainer;
import net.minecraft.inventory.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Mixin(Container.class)
public abstract class MixinContainer implements IContainer {
    
    @Unique private final Set<String> dimhoppertweaks$stages = new HashSet<>();
    
    @Override
    public void dimhoppertweaks$setStages(Collection<String> stages, boolean clear) {
        if(clear) this.dimhoppertweaks$stages.clear();
        this.dimhoppertweaks$stages.addAll(stages);
    }
    
    @Override
    public Collection<String> dimhoppertweaks$getStages() {
        return this.dimhoppertweaks$stages;
    }
}