package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BlockSoulSand.class)
public abstract class MixinBlockSoulSand {
    
    /**
     * @author The_Computerizer
     * @reason Unstoppable trait
     */
    @Overwrite
    public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
        if(!SkillWrapper.isUnstoppable(entity)) {
            entity.motionX*=0.4d;
            entity.motionZ*=0.4d;
        }
    }
}