package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.mixin.access.EntityLivinBaseAccess;
import mods.thecomputerizer.dimhoppertweaks.registry.ParticleRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(EntityPlayer.class)
public class MixinEntityPlayer {

    @Unique private EntityLivingBase dimhoppertweaks$targetAttackedEntity;

    @Inject(at = @At("HEAD"), method = "attackTargetEntityWithCurrentItem")
    private void dimhoppertweaks$getEntityInstanceForParticleReplacement(Entity targetEntity, CallbackInfo ci) {
        if(targetEntity instanceof EntityLivingBase)
            this.dimhoppertweaks$targetAttackedEntity = (EntityLivingBase)targetEntity;
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldServer;spawnParticle(Lnet/minecraft/util/EnumParticleTypes;DDDIDDDD[I)V"), method = "attackTargetEntityWithCurrentItem")
    private void dimhoppertweaks$attackTargetEntityWithCurrentItem(
            WorldServer world, EnumParticleTypes particle, double x, double y, double z, int num, double xOffset,
            double yOffset, double zOffset, double speed, int[] particleArguments) {
        boolean isBlighted = Objects.nonNull(this.dimhoppertweaks$targetAttackedEntity) &&
                ((EntityLivinBaseAccess)this.dimhoppertweaks$targetAttackedEntity).dimhoppertweaks$isBlighted();
        EnumParticleTypes particleType = isBlighted ? ParticleRegistry.BLIGHT_FIRE : particle;
        world.spawnParticle(particleType,x,y,z,num,xOffset,yOffset,zOffset,speed,particleArguments);
    }
}
