package mods.thecomputerizer.dimhoppertweaks.mixin.mods.togetherforever;

import com.buuz135.togetherforever.action.AdvancementEventSyncAction;
import mods.thecomputerizer.dimhoppertweaks.config.DHTConfigHelper;
import net.minecraft.advancements.Advancement;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AdvancementEventSyncAction.class, remap = false)
public abstract class MixinAdvancementEventSyncAction {

    @Inject(at = @At("HEAD"), method = "grantAllParentAchievements", cancellable = true)
    private static void dimhoppertweaks$applyBlacklist(EntityPlayerMP player, Advancement advancement, CallbackInfo ci) {
        ResourceLocation id = advancement.getId();
        if(id.getNamespace().matches("triumph")) {
            DHTConfigHelper.devInfo("NOT SYNCING BLACKLISTED ADVANCEMENT `{}`",id);
            ci.cancel();
        }
    }
}