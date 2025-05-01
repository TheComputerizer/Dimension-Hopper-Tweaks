package mods.thecomputerizer.dimhoppertweaks.common.capability.chunk;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;

@SuppressWarnings("ConstantConditions")
public class ExtraChunkDataProvider implements ICapabilitySerializable<NBTBase> {


    @CapabilityInject(IExtraChunkData.class)
    public static final Capability<IExtraChunkData> CHUNK_CAPABILITY = null;
    private final IExtraChunkData impl = CHUNK_CAPABILITY.getDefaultInstance();

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability==CHUNK_CAPABILITY;
    }

    @Override
    public <T> @Nullable T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return capability==CHUNK_CAPABILITY ? CHUNK_CAPABILITY.cast(this.impl) : null;
    }

    @Override
    public NBTBase serializeNBT() {
        return CHUNK_CAPABILITY.getStorage().writeNBT(CHUNK_CAPABILITY,this.impl,null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        CHUNK_CAPABILITY.getStorage().readNBT(CHUNK_CAPABILITY, this.impl, null, nbt);
    }
}