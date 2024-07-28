package mods.thecomputerizer.dimhoppertweaks.mixin.mods.moreplanets;

import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import stevekung.mods.moreplanets.planets.chalos.blocks.BlockCheeseSlime;

@Mixin(value = BlockCheeseSlime.class, remap = false)
public abstract class MixinBlockCheeseSlime {
    
    @Inject(at = @At("HEAD"), method = "onEntityWalk", remap = true, cancellable = true)
    private void dimhoppertweaks$checkWalk(World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        if(SkillWrapper.isUnstoppable(entity)) ci.cancel();
    }
}