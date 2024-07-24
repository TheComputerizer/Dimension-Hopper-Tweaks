package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlime;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.annotation.ParametersAreNonnullByDefault;

@Mixin(BlockSlime.class)
@ParametersAreNonnullByDefault
public abstract class MixinBlockSlime extends Block {
    
    public MixinBlockSlime(Material material) {
        super(material);
    }
    
    /**
     * @author The_Computerizer
     * @reason Unstoppable trait
     */
    @Overwrite
    public void onEntityWalk(World world, BlockPos pos, Entity entity) {
        double absMotionY = Math.abs(entity.motionY);
        if(!SkillWrapper.isUnstoppable(entity) && absMotionY<0.1d && !entity.isSneaking()) {
            double motionFactor = 0.4d+absMotionY*0.2d;
            entity.motionX*=motionFactor;
            entity.motionZ*=motionFactor;
        }
        super.onEntityWalk(world,pos,entity);
    }
}