package mods.thecomputerizer.dimhoppertweaks.common.capability;

import net.minecraft.nbt.NBTTagCompound;

public interface ICommonCapability {

    NBTTagCompound writeToNBT();
    void readFromNBT(NBTTagCompound tag);
}