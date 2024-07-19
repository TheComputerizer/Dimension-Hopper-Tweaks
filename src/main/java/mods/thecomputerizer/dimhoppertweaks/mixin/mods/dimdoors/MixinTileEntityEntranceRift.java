package mods.thecomputerizer.dimhoppertweaks.mixin.mods.dimdoors;

import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import org.dimdev.ddutils.TeleportUtils;
import org.dimdev.ddutils.WorldUtils;
import org.dimdev.dimdoors.DimDoors;
import org.dimdev.dimdoors.shared.tileentities.TileEntityEntranceRift;
import org.dimdev.dimdoors.shared.tileentities.TileEntityRift;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import static org.dimdev.dimdoors.shared.ModConfig.general;

@Mixin(value = TileEntityEntranceRift.class, remap = false)
public abstract class MixinTileEntityEntranceRift extends TileEntityRift {
    
    @Shadow public EnumFacing orientation;
    
    /**
     * @author The_Computerizer
     * @reason Compatibility with dimension stages & betweenlands blocking
     */
    @Overwrite
    public boolean receiveEntity(Entity entity, float relativeYaw, float relativePitch) {
        Vec3d targetPos = (new Vec3d(this.pos)).add(0.5d,0d,0.5d).add((
                new Vec3d(this.orientation.getDirectionVec())).scale(general.teleportOffset+0.5d));
        int entityDimension = entity.dimension;
        int targetDimension = WorldUtils.getDim(this.world);
        if(entityDimension==20 && targetDimension!=20) {
            DimDoors.chat(entity,"The murky aura of the swamp sticks to the rift and prevents you from leaving.");
            return false;
        }
        if(targetDimension==20 && entityDimension!=20) {
            DimDoors.chat(entity,"The murky aura of the swamp has rejected your presence.");
            return false;
        }
        if(DelayedModAccess.hasStageForDimension(entity,targetDimension)) {
            String stage = DelayedModAccess.getStageForDimension(targetDimension);
            DimDoors.chat(entity,"The rift rejects your lack of knowledge (Missing stage "+stage+")");
            return false;
        }
        if(this.relativeRotation) {
            float yaw = this.getDestinationYaw(entity.rotationYaw)+entity.rotationYaw-relativeYaw;
            float pitch = entity instanceof EntityLiving ? entity.rotationPitch :
                    this.getDestinationPitch(entity.rotationPitch)+entity.rotationPitch-relativePitch;
            TeleportUtils.teleport(entity,targetDimension,targetPos.x,targetPos.y,targetPos.z,yaw,pitch);
        }
        else TeleportUtils.teleport(entity,targetDimension,targetPos.x,targetPos.y,targetPos.z,
                                   this.orientation.getHorizontalAngle(),0f);
        
        return true;
    }
}