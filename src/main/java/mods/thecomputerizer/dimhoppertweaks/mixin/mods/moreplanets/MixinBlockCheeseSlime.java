package mods.thecomputerizer.dimhoppertweaks.mixin.mods.moreplanets;

import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import stevekung.mods.moreplanets.planets.chalos.blocks.BlockCheeseSlime;

@Mixin(value = BlockCheeseSlime.class, remap = false)
public abstract class MixinBlockCheeseSlime {
    
    /**
     * @author The_Computerizer
     * @reason Unstoppable trait
     */
    @Overwrite(remap = true)
    public void onEntityWalk(World world, BlockPos pos, Entity entity) {
        if(SkillWrapper.isUnstoppable(entity)) return;
        double absMotionY = Math.abs(entity.motionY);
        if(absMotionY<0.1d && !entity.isSneaking()) {
            double motionFactor = 0.4d+absMotionY*0.2d;
            entity.motionX*=motionFactor;
            entity.motionZ*=motionFactor;
        }
    }
}
