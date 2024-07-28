package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlime;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.ParametersAreNonnullByDefault;

@Mixin(BlockSlime.class)
@ParametersAreNonnullByDefault
public abstract class MixinBlockSlime extends Block {
    
    public MixinBlockSlime(Material material) {
        super(material);
    }
    
    @Inject(at = @At("HEAD"), method = "onEntityWalk", cancellable = true)
    private void dimhoppertweaks$checkWalk(World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        if(SkillWrapper.isUnstoppable(entity)) ci.cancel();
    }
}