package mods.thecomputerizer.dimhoppertweaks.mixin.mods.randomthings;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Cancellable;
import lumien.randomthings.item.ItemBase;
import lumien.randomthings.item.ItemStableEnderpearl;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemStableEnderpearl.class, remap = false)
public abstract class MixinItemStableEnderpearl extends ItemBase {
    
    public MixinItemStableEnderpearl(String name) {
        super(name);
    }
    
    @WrapOperation(at=@At(value="INVOKE",target="Llumien/randomthings/util/PlayerUtil;teleportPlayerToDimension("+
            "Lnet/minecraft/entity/player/EntityPlayerMP;I)V"),method="onEntityItemUpdate")
    private void dimhoppertweaks$onEntityItemUpdate(EntityPlayerMP player, int dimension, Operation<Void> original,
            @Cancellable CallbackInfoReturnable<Boolean> cir) {
        if(!cir.isCancelled()) {
            if(DelayedModAccess.hasStageForDimension(player,dimension)) original.call(player,dimension);
            else cir.setReturnValue(false);
        }
    }
}
