package mods.thecomputerizer.dimhoppertweaks.common.capability.chunk;

import mods.thecomputerizer.dimhoppertweaks.common.capability.ICommonCapability;

public interface IExtraChunkData extends ICommonCapability {

    boolean isFast();
    void setFast(boolean fast);
}