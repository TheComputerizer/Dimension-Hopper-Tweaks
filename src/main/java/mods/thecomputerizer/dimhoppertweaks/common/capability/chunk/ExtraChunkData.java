package mods.thecomputerizer.dimhoppertweaks.common.capability.chunk;

import mods.thecomputerizer.dimhoppertweaks.common.capability.CommonCapability;
import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;

@SuppressWarnings({"SameParameterValue"})
public class ExtraChunkData implements IExtraChunkData {

    public static final ResourceLocation CHUNK_CAPABILITY = DHTRef.res("extra_chunk_data");

    public static boolean isChunkFast(Chunk chunk) {
        return chunk.isLoaded() && CommonCapability.getOrDefault(chunk,ExtraChunkDataProvider.CHUNK_CAPABILITY,
                IExtraChunkData::isFast,false);
    }

    public static void setFastChunk(Chunk chunk, boolean isFast) {
        if(chunk.isLoaded())
            CommonCapability.executeIfPresent(chunk,ExtraChunkDataProvider.CHUNK_CAPABILITY,data -> data.setFast(isFast));
    }

    private boolean isFast;

    public ExtraChunkData() {
        this.isFast = false;
    }

    @Override
    public boolean isFast() {
        return this.isFast;
    }

    @Override
    public void setFast(boolean fast) {
        this.isFast = fast;
    }

    @Override
    public NBTTagCompound writeToNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("isFast",this.isFast);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        this.isFast = tag.getBoolean("isFast");
    }
}