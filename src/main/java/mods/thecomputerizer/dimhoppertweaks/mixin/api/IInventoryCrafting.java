package mods.thecomputerizer.dimhoppertweaks.mixin.api;

import java.util.Collection;

public interface IInventoryCrafting {
    
    default void dimhoppertweaks$setStages(Collection<String> stages) {
        dimhoppertweaks$setStages(stages,false);
    }
    
    void dimhoppertweaks$setStages(Collection<String> stages, boolean clear);
    Collection<String> dimhoppertweaks$getStages();
}