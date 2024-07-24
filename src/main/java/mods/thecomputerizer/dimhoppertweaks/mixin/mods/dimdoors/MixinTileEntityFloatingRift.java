package mods.thecomputerizer.dimhoppertweaks.mixin.mods.dimdoors;

import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import org.dimdev.ddutils.Location;
import org.dimdev.ddutils.TeleportUtils;
import org.dimdev.ddutils.WorldUtils;
import org.dimdev.dimdoors.DimDoors;
import org.dimdev.dimdoors.shared.tileentities.TileEntityFloatingRift;
import org.dimdev.dimdoors.shared.tileentities.TileEntityRift;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = TileEntityFloatingRift.class, remap = false)
public abstract class MixinTileEntityFloatingRift extends TileEntityRift {
    
    /**
     * @author The_Computerizer
     * @reason Compatibility with dimension stages & betweenlands blocking
     */
    @Overwrite
    public boolean receiveEntity(Entity entity, float relativeYaw, float relativePitch) {
        int entityDimension = entity.dimension;
        int targetDimension = WorldUtils.getDim(this.world);
        if(entityDimension==20 && targetDimension!=20) {
            DimDoors.chat(entity, "The murky aura of the swamp sticks to the rift and prevents you from leaving.");
            return false;
        }
        if(targetDimension==20 && entityDimension!=20) {
            DimDoors.chat(entity,"The murky aura of the swamp has rejected your presence.");
            return false;
        }
        if(!DelayedModAccess.hasStageForDimension(entity, targetDimension)) {
            String stage = DelayedModAccess.getStageForDimension(targetDimension);
            DimDoors.chat(entity,"The rift rejects your lack of knowledge (Missing stage "+stage+")");
            return false;
        }
        if(this.relativeRotation) {
            float yaw = this.getDestinationYaw(entity.rotationYaw)+entity.rotationYaw-relativeYaw;
            float pitch = entity instanceof EntityLiving ? entity.rotationPitch :
                    this.getDestinationPitch(entity.rotationPitch)+entity.rotationPitch-relativePitch;
            TeleportUtils.teleport(entity,new Location(this.world,this.pos),yaw,pitch);
        }
        else TeleportUtils.teleport(entity,new Location(this.world,this.pos),this.getDestinationYaw(entity.rotationYaw),
                                    this.getDestinationPitch(entity.rotationPitch));
        return true;
    }
}