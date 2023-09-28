package mods.thecomputerizer.dimhoppertweaks.mixin.access;

import java.util.Collection;

public interface TileEntityAccess {

    void dimhoppertweaks$setStages(String ... stages);
    void dimhoppertweaks$setStages(Collection<String> stages);
    void dimhoppertweaks$addStage(String stage);
    boolean dimhoppertweaks$hasStage(String stage);
}
