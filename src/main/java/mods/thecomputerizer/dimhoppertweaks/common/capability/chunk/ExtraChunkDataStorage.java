package mods.thecomputerizer.dimhoppertweaks.common.capability.chunk;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class ExtraChunkDataStorage implements Capability.IStorage<IExtraChunkData> {

    @Override
    public @Nullable NBTBase writeNBT(Capability<IExtraChunkData> capability, IExtraChunkData instance, EnumFacing side) {
        return instance.writeToNBT();
    }

    @Override
    public void readNBT(Capability<IExtraChunkData> capability, IExtraChunkData instance, EnumFacing side, NBTBase nbt) {
        instance.readFromNBT((NBTTagCompound)nbt);
    }
}