package mods.thecomputerizer.dimhoppertweaks.mixin.mods.togetherforever;

import com.buuz135.togetherforever.action.AdvancementEventSyncAction;
import net.minecraft.advancements.Advancement;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AdvancementEventSyncAction.class, remap = false)
public class MixinAdvancementEventSyncAction {

    @Inject(at = @At("HEAD"), method = "grantAllParentAchievements", cancellable = true)
    private static void dimhoppertweaks$applyBlacklist(EntityPlayerMP player, Advancement advancement, CallbackInfo ci) {
        if(advancement.getId().getNamespace().matches("triumph")) ci.cancel();
    }
}
