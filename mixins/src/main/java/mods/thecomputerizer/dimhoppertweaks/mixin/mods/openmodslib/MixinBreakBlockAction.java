package mods.thecomputerizer.dimhoppertweaks.mixin.mods.openmodslib;

import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.item.EntityItem;
import openmods.fakeplayer.BreakBlockAction;
import openmods.fakeplayer.OpenModsFakePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = BreakBlockAction.class, remap = false)
public class MixinBreakBlockAction {
    
    @Inject(at = @At("HEAD"), method = "usePlayer(Lopenmods/fakeplayer/OpenModsFakePlayer;)Ljava/util/List;")
    private void dimhoppertweaks$usePlayer(OpenModsFakePlayer player, CallbackInfoReturnable<List<EntityItem>> cir) {
        DelayedModAccess.inheritTileStages(player);
    }
}