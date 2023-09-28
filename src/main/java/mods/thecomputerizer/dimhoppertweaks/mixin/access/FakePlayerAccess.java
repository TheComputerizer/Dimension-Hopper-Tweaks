package mods.thecomputerizer.dimhoppertweaks.mixin.access;

import java.util.Collection;

public interface FakePlayerAccess {

    void dimhoppertweaks$setStages(Collection<String> stages);
    boolean dimhoppertweaks$hasStage(String stage);
}
