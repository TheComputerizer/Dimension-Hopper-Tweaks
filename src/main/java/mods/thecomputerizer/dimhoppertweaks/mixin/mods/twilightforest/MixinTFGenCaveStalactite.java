package mods.thecomputerizer.dimhoppertweaks.mixin.mods.twilightforest;

import net.minecraft.init.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import twilightforest.world.feature.TFGenCaveStalactite;

import static twilightforest.world.feature.TFGenCaveStalactite.addStalactite;

@Mixin(value = TFGenCaveStalactite.class, remap = false)
public abstract class MixinTFGenCaveStalactite {

    /**
     * @author The_Computerizer
     * @reason Remove unwanted stalactite ores
     */
    @Overwrite
    private static void addDefaultStalactites() {
        addStalactite(2,Blocks.REDSTONE_ORE.getDefaultState(),0.8f,8,1,40);
        addStalactite(1,Blocks.COAL_ORE.getDefaultState(),0.8f,12,1,24);
        addStalactite(1,Blocks.GLOWSTONE.getDefaultState(),0.5f,8,1,12);
    }
}