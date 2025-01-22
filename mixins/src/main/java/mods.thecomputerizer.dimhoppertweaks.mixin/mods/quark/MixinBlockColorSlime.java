package mods.thecomputerizer.dimhoppertweaks.mixin.mods.quark;

import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import vazkii.arl.block.BlockMetaVariants;
import vazkii.quark.automation.block.BlockColorSlime;
import vazkii.quark.automation.block.BlockColorSlime.Variants;

import javax.annotation.ParametersAreNonnullByDefault;

@Mixin(value = BlockColorSlime.class, remap = false)
@ParametersAreNonnullByDefault
public abstract class MixinBlockColorSlime extends BlockMetaVariants<Variants> {
    
    public MixinBlockColorSlime(String name, Material material, Class<Variants> variants) {
        super(name,material,variants);
    }
    
    /**
     * @author The_Computerizer
     * @reason Unstoppable trait
     */
    @Overwrite(remap = true)
    public void onEntityWalk(World world, BlockPos pos, Entity entity) {
        if(SkillWrapper.isUnstoppable(entity)) {
            super.onEntityWalk(world,pos,entity);
            return;
        }
        double absMotionY = Math.abs(entity.motionY);
        if(absMotionY<0.1d && !entity.isSneaking()) {
            double motionFactor = 0.4d+absMotionY*0.2d;
            entity.motionX*=motionFactor;
            entity.motionZ*=motionFactor;
        }
        super.onEntityWalk(world,pos,entity);
    }
}
