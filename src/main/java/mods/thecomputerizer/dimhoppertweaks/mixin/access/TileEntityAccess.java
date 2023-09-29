package mods.thecomputerizer.dimhoppertweaks.mixin.access;

import java.util.Collection;

@SuppressWarnings({"BooleanMethodIsAlwaysInverted", "unused"})
public interface TileEntityAccess {

    void dimhoppertweaks$setStages(String ... stages);
    void dimhoppertweaks$setStages(Collection<String> stages);
    Collection<String> dimhoppertweaks$getStages();
    void dimhoppertweaks$addStage(String stage);
    boolean dimhoppertweaks$hasStage(String stage);
}
