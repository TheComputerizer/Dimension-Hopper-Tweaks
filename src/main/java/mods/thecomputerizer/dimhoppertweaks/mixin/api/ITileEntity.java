package mods.thecomputerizer.dimhoppertweaks.mixin.api;

import java.util.Collection;

@SuppressWarnings({"BooleanMethodIsAlwaysInverted", "unused"})
public interface ITileEntity {

    void dimhoppertweaks$setStages(String ... stages);
    void dimhoppertweaks$setStages(Collection<String> stages);
    Collection<String> dimhoppertweaks$getStages();
    void dimhoppertweaks$addStage(String stage);
    boolean dimhoppertweaks$hasStage(String stage);
}