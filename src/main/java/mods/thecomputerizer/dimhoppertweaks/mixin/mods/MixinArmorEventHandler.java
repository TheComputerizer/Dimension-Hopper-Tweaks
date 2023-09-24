package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import com.cjm721.overloaded.item.functional.armor.ArmorEventHandler;
import com.cjm721.overloaded.storage.GenericDataStorage;
import com.cjm721.overloaded.storage.IGenericDataStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(value = ArmorEventHandler.class, remap = false)
public abstract class MixinArmorEventHandler {

    @Shadow protected abstract void tryEnableNoClip(EntityPlayer player, IGenericDataStorage dataStorage, IGenericDataStorage helmetDataStorage, Side side);

    @Inject(at = @At("HEAD"), method = "onPlayerTickEvent", cancellable = true)
    private void dimhoppertweaks$onPlayerTickEvent(TickEvent.PlayerTickEvent event, CallbackInfo ci) {
        if(Objects.nonNull(event.player) && !event.player.hasCapability(GenericDataStorage.GENERIC_DATA_STORAGE,null)) ci.cancel();
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lcom/cjm721/overloaded/item/functional/armor/ArmorEventHandler;" +
            "tryEnableNoClip(Lnet/minecraft/entity/player/EntityPlayer;Lcom/cjm721/overloaded/storage/IGenericDataStorage;" +
            "Lcom/cjm721/overloaded/storage/IGenericDataStorage;Lnet/minecraftforge/fml/relauncher/Side;)V"), method = "onLivingUpdateEvent")
    private void dimhoppertweaks$redirectNoClip(ArmorEventHandler handler, EntityPlayer player,
                                                IGenericDataStorage generic, IGenericDataStorage helmet, Side side) {
        if(Objects.nonNull(generic) && Objects.nonNull(helmet)) this.tryEnableNoClip(player,generic,helmet,side);
    }
}
