package mods.thecomputerizer.dimhoppertweaks.mixin.api;

import java.util.Collection;

@SuppressWarnings({"BooleanMethodIsAlwaysInverted", "unused"})
public interface IGameStageExtension {

    default void dimhoppertweaks$setStages(String ... stages) {
        dimhoppertweaks$setStages(false,stages);
    }

    void dimhoppertweaks$setStages(boolean clear, String ... stages);

    default void dimhoppertweaks$setStages(Collection<String> stages) {
        dimhoppertweaks$setStages(stages,false);
    }

    void dimhoppertweaks$setStages(Collection<String> stages, boolean clear);
    Collection<String> dimhoppertweaks$getStages();
    void dimhoppertweaks$addStage(String stage);
    boolean dimhoppertweaks$hasStage(String stage);
}