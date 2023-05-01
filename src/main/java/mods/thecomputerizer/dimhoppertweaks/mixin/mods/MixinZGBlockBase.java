package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zollerngalaxy.blocks.ZGBlockBase;

@Mixin(value = ZGBlockBase.class, remap = false)
public class MixinZGBlockBase {

    @Inject(at = @At(value = "HEAD"), method = "onEntityWalk", cancellable = true)
    private void dimhoppertweaks_onEntityWalk(World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        if(!(entity instanceof EntityLivingBase)) ci.cancel();
    }
}
