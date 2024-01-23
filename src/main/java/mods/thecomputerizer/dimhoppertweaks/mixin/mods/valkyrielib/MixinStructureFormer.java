package mods.thecomputerizer.dimhoppertweaks.mixin.mods.valkyrielib;

import com.valkyrieofnight.vliblegacy.lib.multiblock.tilemodule.StructureFormer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;

@Mixin(value = StructureFormer.class, remap = false)
public abstract class MixinStructureFormer {

    @Shadow private boolean isFormed;
    @Shadow protected int mbDirection;
    @Shadow private BlockPos formedPosition;

    /**
     * @author The_Computerizer
     * @reason Handle null formed position
     */
    @Overwrite
    public NBTTagCompound saveTileData(NBTTagCompound nbt, boolean toItem) {
        if(!toItem && this.isFormed && Objects.nonNull(this.formedPosition)) {
            nbt.setBoolean("isFormed",this.isFormed);
            nbt.setInteger("mbdir",this.mbDirection);
            nbt.setInteger("formed_x",this.formedPosition.getX());
            nbt.setInteger("formed_y",this.formedPosition.getY());
            nbt.setInteger("formed_z",this.formedPosition.getZ());
        }
        return nbt;
    }
}