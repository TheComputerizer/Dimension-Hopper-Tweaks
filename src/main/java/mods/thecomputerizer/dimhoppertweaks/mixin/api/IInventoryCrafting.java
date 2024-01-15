package mods.thecomputerizer.dimhoppertweaks.mixin.api;

import java.util.Collection;

public interface IInventoryCrafting {

    void dimhoppertweaks$setStages(Collection<String> stages);
    Collection<String> dimhoppertweaks$getStages();
}