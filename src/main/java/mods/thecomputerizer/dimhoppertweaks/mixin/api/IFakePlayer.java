package mods.thecomputerizer.dimhoppertweaks.mixin.api;

import java.util.Collection;

public interface IFakePlayer {

    void dimhoppertweaks$setStages(Collection<String> stages);
    boolean dimhoppertweaks$hasStage(String stage);
}