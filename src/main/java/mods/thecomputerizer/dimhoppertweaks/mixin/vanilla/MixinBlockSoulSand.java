package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Objects;

import static mods.thecomputerizer.dimhoppertweaks.registry.TraitRegistry.UNSTOPPABLE;

@Mixin(BlockSoulSand.class)
public class MixinBlockSoulSand {
    
    /**
     * @author The_Computerizer
     * @reason Unstoppable trait
     */
    @Overwrite
    public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
        boolean slow = true;
        if(entity instanceof EntityPlayer) {
            PlayerData data = PlayerDataHandler.get((EntityPlayer)entity);
            if(Objects.nonNull(data) && data.getSkillInfo(SkillWrapper.getSkill("agility")).isUnlocked(UNSTOPPABLE))
                slow = false;
        }
        if(slow) {
            entity.motionX*=0.4d;
            entity.motionZ*=0.4d;
        }
    }
}