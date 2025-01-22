package mods.thecomputerizer.dimhoppertweaks.mixin.mods.twilightforest;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import twilightforest.world.feature.TFGenCaveStalactite;

import static net.minecraft.init.Blocks.COAL_ORE;
import static net.minecraft.init.Blocks.GLOWSTONE;
import static net.minecraft.init.Blocks.REDSTONE_ORE;
import static twilightforest.world.feature.TFGenCaveStalactite.addStalactite;

@Mixin(value = TFGenCaveStalactite.class, remap = false)
public abstract class MixinTFGenCaveStalactite {

    /**
     * @author The_Computerizer
     * @reason Remove unwanted stalactite ores
     */
    @Overwrite
    private static void addDefaultStalactites() {
        addStalactite(2,REDSTONE_ORE.getDefaultState(),0.8f,8,1,40);
        addStalactite(1,COAL_ORE.getDefaultState(),0.8f,12,1,24);
        addStalactite(1,GLOWSTONE.getDefaultState(),0.5f,8,1,12);
    }
}