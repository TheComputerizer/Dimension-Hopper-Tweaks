package mods.thecomputerizer.dimhoppertweaks.mixin.mods.lightningcraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import sblectric.lightningcraft.blocks.BlockEnergyProvider;
import sblectric.lightningcraft.config.LCConfig;

@Mixin(value = BlockEnergyProvider.class, remap = false)
public class MixinBlockEnergyProvider {

    /**
     * @author The_Computerizer
     * @reason Fix hardcoded RF transfer rates
     */
    @Overwrite
    public int getMaxRFPerTick() {
        return LCConfig.RFtoLEConversion*2;
    }
}
