package mods.thecomputerizer.dimhoppertweaks.mixin.mods.lightningcraft;

import mods.thecomputerizer.dimhoppertweaks.config.DHTConfigHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import sblectric.lightningcraft.blocks.BlockAirTerminal;
import sblectric.lightningcraft.blocks.base.BlockMeta;

@Mixin(value = BlockAirTerminal.class, remap = false)
public abstract class MixinBlockAirTerminal extends BlockMeta {
    
    @Shadow public abstract int getMetaFromState(IBlockState state);
    
    public MixinBlockAirTerminal(Block parent, int subBlocks, float hardness, float resistance, boolean sameIcon) {
        super(parent,subBlocks,hardness,resistance,sameIcon);
    }
    
    /**
     * @author The_Computerizer
     * @reason Overhaul air terminal efficiency
     */
    @Overwrite
    public double getEfficiency(IBlockState state) {
        return DHTConfigHelper.airTerminalEfficiency(getMetaFromState(state));
    }
}