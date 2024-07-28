package mods.thecomputerizer.dimhoppertweaks.mixin.mods.infernalmobs;

import atomicstryker.infernalmobs.common.MobModifier;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(value = MobModifier.class, remap = false)
public class MixinMobModifier {
    
    @Shadow private EntityLivingBase previousAttackTarget;
    @Shadow private EntityLivingBase attackTarget;
    @Shadow private int targetingTicksSteadyTarget;
    @Shadow protected MobModifier nextMod;
    
    /**
     * @author The_Computerizer
     * @reason Stronger Block For Block check
     */
    @Overwrite
    public void onSetAttackTarget(EntityLivingBase target) {
        if(DelayedModAccess.isInfernalDistracted(target)) {
            this.previousAttackTarget = null;
            this.attackTarget = null;
            this.targetingTicksSteadyTarget = 0;
        }
        else {
            this.previousAttackTarget = this.attackTarget;
            this.attackTarget = target;
            if (this.previousAttackTarget != target) {
                this.targetingTicksSteadyTarget = 0;
            }
        }
        if(Objects.nonNull(this.nextMod)) this.nextMod.onSetAttackTarget(target);
    }
    
    @Redirect(at =@At(
            value="INVOKE", target="Lnet/minecraft/world/World;getClosestPlayerToEntity("+
                                   "Lnet/minecraft/entity/Entity;D)Lnet/minecraft/entity/player/EntityPlayer;"),
            method = "onUpdate")
    private EntityPlayer dimhoppertweaks$ignoreBlockingTarget(World world, Entity entity, double distance) {
        EntityPlayer player = world.getClosestPlayerToEntity(entity,distance);
        if(Objects.nonNull(player) && DelayedModAccess.isInfernalDistracted(player)) player = null;
        return player;
        
    }
}