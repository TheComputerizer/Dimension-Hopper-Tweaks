package mods.thecomputerizer.dimhoppertweaks.mixin.mods.gaiadimension;

import androsa.gaiadimension.world.TeleporterGaia;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import static androsa.gaiadimension.registry.GDBlocks.gaia_portal;
import static net.minecraft.init.Blocks.PORTAL;
import static net.minecraft.util.EnumFacing.Axis.X;
import static net.minecraft.util.EnumFacing.AxisDirection.NEGATIVE;
import static net.minecraft.util.math.BlockPos.ORIGIN;

@Mixin(value = TeleporterGaia.class, remap = false)
public abstract class MixinTeleporterGaia extends Teleporter {

    @Shadow @Final private Long2ObjectMap<PortalPosition> destinationCoordinateCache;

    public MixinTeleporterGaia(WorldServer world) {
        super(world);
    }

    /**
     * @author The_Computerizer
     * @reason Fix incorrect teleporter position spawning players underground
     */
    @Overwrite
    public boolean placeInExistingPortal(Entity entity, float yaw) {
        double d0 = -1.0D;
        int j = MathHelper.floor(entity.posX);
        int k = MathHelper.floor(entity.posZ);
        boolean flag = true;
        BlockPos pos = ORIGIN;
        long l = ChunkPos.asLong(j,k);
        if (this.destinationCoordinateCache.containsKey(l)) {
            PortalPosition portalPos = this.destinationCoordinateCache.get(l);
            d0 = 0.0D;
            pos = portalPos;
            portalPos.lastUpdateTime = this.world.getTotalWorldTime();
            flag = false;
        } else {
            BlockPos pos3 = new BlockPos(entity);
            for(int i1=-128; i1<=128; i1++) {
                BlockPos pos2;
                for(int j1=-128; j1<=128; j1++) {
                    for(BlockPos pos1 = pos3.add(i1,this.world.getActualHeight()-1-pos3.getY(),j1); 
                         pos1.getY()>=0; pos1 = pos2) {
                        pos2 = pos1.down();
                        if(this.world.getBlockState(pos1).getBlock()==gaia_portal) {
                            for(pos2 = pos1.down();this.world.getBlockState(pos2).getBlock()==gaia_portal;
                                pos2 = pos2.down()) pos1 = pos2;
                            double d1 = pos1.distanceSq(pos3);
                            if(d0<0d || d1<d0) {
                                d0 = d1;
                                pos = pos1;
                            }
                        }
                    }
                }
            }
        }
        if(d0>=0d) {
            if(flag)
                this.destinationCoordinateCache.put(l,new PortalPosition(pos,this.world.getTotalWorldTime()));
            double d5 = (double)pos.getX()+0.5d;
            double d7 = (double)pos.getZ()+0.5d;
            BlockPattern.PatternHelper pattern = PORTAL.createPatternHelper(this.world,pos);
            boolean flag1 = pattern.getForwards().rotateY().getAxisDirection()==NEGATIVE;
            double d2 = pattern.getForwards().getAxis()==X ?
                    (double)pattern.getFrontTopLeft().getZ() : (double)pattern.getFrontTopLeft().getX();
            double d6 = (double)(pattern.getFrontTopLeft().getY()+1)-entity.getLastPortalVec().y*(double)pattern.getHeight();
            if(flag1) d2++;
            if(pattern.getForwards().getAxis()==X)
                 d7 = d2+(1d-entity.getLastPortalVec().x)*(double)pattern.getWidth()*
                         (double)pattern.getForwards().rotateY().getAxisDirection().getOffset();
            else d5 = d2+(1d-entity.getLastPortalVec().x)*(double)pattern.getWidth()*
                    (double)pattern.getForwards().rotateY().getAxisDirection().getOffset();
            float f = 0f;
            float f1 = 0f;
            float f2 = 0f;
            float f3 = 0f;
            if(pattern.getForwards().getOpposite()==entity.getTeleportDirection()) {
                f = 1f;
                f1 = 1f;
            } else if(pattern.getForwards().getOpposite()==entity.getTeleportDirection().getOpposite()) {
                f = -1f;
                f1 = -1f;
            } else if (pattern.getForwards().getOpposite()==entity.getTeleportDirection().rotateY()) {
                f2 = 1f;
                f3 = -1f;
            } else {
                f2 = -1f;
                f3 = 1f;
            }
            double d3 = entity.motionX;
            double d4 = entity.motionZ;
            entity.motionX = d3*(double)f+d4*(double)f3;
            entity.motionZ = d3*(double)f2+d4 *(double)f1;
            entity.rotationYaw = yaw-(float)(entity.getTeleportDirection().getOpposite().getHorizontalIndex()*90)+
                    (float)(pattern.getForwards().getHorizontalIndex()*90);
            if(entity instanceof EntityPlayerMP)
                ((EntityPlayerMP)entity).connection.setPlayerLocation(d5,d6+1d,d7,entity.rotationYaw,entity.rotationPitch);
            else entity.setLocationAndAngles(d5,d6+1d,d7,entity.rotationYaw,entity.rotationPitch);
            return true;
        }
        return false;
    }
}