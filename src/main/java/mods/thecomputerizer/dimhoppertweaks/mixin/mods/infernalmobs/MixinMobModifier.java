package mods.thecomputerizer.dimhoppertweaks.mixin.mods.infernalmobs;

import atomicstryker.infernalmobs.common.MobModifier;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(value = MobModifier.class, remap = false)
public class MixinMobModifier {
    
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