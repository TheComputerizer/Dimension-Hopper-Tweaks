package mods.thecomputerizer.dimhoppertweaks.mixin.mods.dimdoors;

import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.Entity;
import org.dimdev.ddutils.WorldUtils;
import org.dimdev.dimdoors.DimDoors;
import org.dimdev.dimdoors.shared.tileentities.TileEntityFloatingRift;
import org.dimdev.dimdoors.shared.tileentities.TileEntityRift;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TileEntityFloatingRift.class, remap = false)
public abstract class MixinTileEntityFloatingRift extends TileEntityRift {
    
    @Inject(at = @At("HEAD"), method = "receiveEntity", cancellable=true)
    private void receiveEntity(
            Entity entity, float relativeYaw, float relativePitch, CallbackInfoReturnable<Boolean> cir) {
        int entityDimension = entity.dimension;
        int targetDimension = WorldUtils.getDim(this.world);
        if(entityDimension==20 && targetDimension!=20) {
            DimDoors.chat(entity,"The murky aura of the swamp sticks to the rift and prevents you from leaving.");
            cir.setReturnValue(false);
        }
        if(targetDimension==20 && entityDimension!=20) {
            DimDoors.chat(entity,"The murky aura of the swamp has rejected your presence.");
            cir.setReturnValue(false);
        }
        if(!DelayedModAccess.hasStageForDimension(entity, targetDimension)) {
            String stage = DelayedModAccess.getStageForDimension(targetDimension);
            DimDoors.chat(entity,"The rift rejects your lack of knowledge (Missing stage "+stage+")");
            cir.setReturnValue(false);
        }
    }
}