package mods.thecomputerizer.dimhoppertweaks.mixin.mods.reskillable;

import codersafterdark.reskillable.base.LevelLockHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(value = LevelLockHandler.class, remap = false)
public abstract class MixinLevelLockHandler {

    @Inject(at = @At("HEAD"), method = "rightClickBlock", cancellable = true)
    private static void dimhoppertweaks$rightClickBlock(RightClickBlock event, CallbackInfo ci) {
        IBlockState state = event.getEntity().getEntityWorld().getBlockState(event.getPos());
        ResourceLocation res = state.getBlock().getRegistryName();
        if(Objects.nonNull(res) && res.getNamespace().matches("storagedrawers")) ci.cancel();
    }
}