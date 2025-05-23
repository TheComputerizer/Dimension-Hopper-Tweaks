package mods.thecomputerizer.dimhoppertweaks.mixin.mods.mekanism;

import mekanism.common.block.BlockBasic;
import mekanism.common.block.BlockBasic.Size;
import mekanism.common.block.states.BlockStateBasic.BasicBlockType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import java.util.Objects;

import static mekanism.common.block.states.BlockStateBasic.BasicBlockType.REFINED_OBSIDIAN;

@Mixin(value = Size.class, remap = false)
public abstract class MixinSize {

    /**
     * @author The_Computerizer
     * @reason Custom Nether portal implementation
     */
    @Overwrite
    private boolean isFrame(IBlockState state) {
        Block block = state.getBlock();
        BasicBlockType type = null;
        if(block instanceof BlockBasic) type = BasicBlockType.get(state);
        return dimhoppertweaks$isCompressedObsidian(block) || type==REFINED_OBSIDIAN;
    }

    @Unique
    private boolean dimhoppertweaks$isCompressedObsidian(Block block) {
        return Objects.nonNull(block.getRegistryName()) && block.getRegistryName().toString().equals("overloaded:compressed_obsidian");
    }
}