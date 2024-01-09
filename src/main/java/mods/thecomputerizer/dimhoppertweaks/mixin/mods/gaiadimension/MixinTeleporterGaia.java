package mods.thecomputerizer.dimhoppertweaks.mixin.mods.gaiadimension;

import androsa.gaiadimension.registry.GDBlocks;
import androsa.gaiadimension.world.TeleporterGaia;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = TeleporterGaia.class, remap = false)
public abstract class MixinTeleporterGaia extends Teleporter {

    @Shadow @Final private Long2ObjectMap<Teleporter.PortalPosition> destinationCoordinateCache;

    public MixinTeleporterGaia(WorldServer world) {
        super(world);
    }

    /**
     * @author The_Computerizer
     * @reason Fix incorrect teleporter position spawning players underground
     */
    @Overwrite
    public boolean placeInExistingPortal(Entity entityIn, float rotationYaw) {
        double d0 = -1.0D;
        int j = MathHelper.floor(entityIn.posX);
        int k = MathHelper.floor(entityIn.posZ);
        boolean flag = true;
        BlockPos blockpos = BlockPos.ORIGIN;
        long l = ChunkPos.asLong(j, k);
        if (this.destinationCoordinateCache.containsKey(l)) {
            Teleporter.PortalPosition teleporter$portalposition = this.destinationCoordinateCache.get(l);
            d0 = 0.0D;
            blockpos = teleporter$portalposition;
            teleporter$portalposition.lastUpdateTime = this.world.getTotalWorldTime();
            flag = false;
        }
        else {
            BlockPos blockpos3 = new BlockPos(entityIn);
            for (int i1 = -128; i1 <= 128; ++i1) {
                BlockPos blockpos2;
                for (int j1 = -128; j1 <= 128; ++j1) {
                    for (BlockPos blockpos1 = blockpos3.add(i1, this.world.getActualHeight() - 1 - blockpos3.getY(), j1); blockpos1.getY() >= 0; blockpos1 = blockpos2) {
                        blockpos2 = blockpos1.down();
                        if (this.world.getBlockState(blockpos1).getBlock()==GDBlocks.gaia_portal) {
                            for (blockpos2 = blockpos1.down(); this.world.getBlockState(blockpos2).getBlock()==GDBlocks.gaia_portal; blockpos2 = blockpos2.down())
                                blockpos1 = blockpos2;
                            double d1 = blockpos1.distanceSq(blockpos3);
                            if (d0 < 0.0D || d1 < d0) {
                                d0 = d1;
                                blockpos = blockpos1;
                            }
                        }
                    }
                }
            }
        }
        if (d0 >= 0.0D) {
            if (flag)
                this.destinationCoordinateCache.put(l,new Teleporter.PortalPosition(blockpos, this.world.getTotalWorldTime()));
            double d5 = (double)blockpos.getX() + 0.5D;
            double d7 = (double)blockpos.getZ() + 0.5D;
            BlockPattern.PatternHelper blockpattern$patternhelper = Blocks.PORTAL.createPatternHelper(this.world, blockpos);
            boolean flag1 = blockpattern$patternhelper.getForwards().rotateY().getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE;
            double d2 = blockpattern$patternhelper.getForwards().getAxis() == EnumFacing.Axis.X ? (double)blockpattern$patternhelper.getFrontTopLeft().getZ() : (double)blockpattern$patternhelper.getFrontTopLeft().getX();
            double d6 = (double)(blockpattern$patternhelper.getFrontTopLeft().getY() + 1) - entityIn.getLastPortalVec().y * (double)blockpattern$patternhelper.getHeight();
            if(flag1) ++d2;
            if (blockpattern$patternhelper.getForwards().getAxis() == EnumFacing.Axis.X)
                d7 = d2 + (1.0D - entityIn.getLastPortalVec().x) * (double)blockpattern$patternhelper.getWidth() * (double)blockpattern$patternhelper.getForwards().rotateY().getAxisDirection().getOffset();
            else d5 = d2 + (1.0D - entityIn.getLastPortalVec().x) * (double)blockpattern$patternhelper.getWidth() * (double)blockpattern$patternhelper.getForwards().rotateY().getAxisDirection().getOffset();
            float f = 0.0F;
            float f1 = 0.0F;
            float f2 = 0.0F;
            float f3 = 0.0F;
            if(blockpattern$patternhelper.getForwards().getOpposite() == entityIn.getTeleportDirection()) {
                f = 1.0F;
                f1 = 1.0F;
            }
            else if(blockpattern$patternhelper.getForwards().getOpposite() == entityIn.getTeleportDirection().getOpposite()) {
                f = -1.0F;
                f1 = -1.0F;
            }
            else if (blockpattern$patternhelper.getForwards().getOpposite() == entityIn.getTeleportDirection().rotateY()) {
                f2 = 1.0F;
                f3 = -1.0F;
            }
            else {
                f2 = -1.0F;
                f3 = 1.0F;
            }
            double d3 = entityIn.motionX;
            double d4 = entityIn.motionZ;
            entityIn.motionX = d3 * (double)f + d4 * (double)f3;
            entityIn.motionZ = d3 * (double)f2 + d4 * (double)f1;
            entityIn.rotationYaw = rotationYaw - (float)(entityIn.getTeleportDirection().getOpposite().getHorizontalIndex()*90)+
                    (float)(blockpattern$patternhelper.getForwards().getHorizontalIndex()*90);
            if(entityIn instanceof EntityPlayerMP)
                ((EntityPlayerMP)entityIn).connection.setPlayerLocation(d5, d6, d7, entityIn.rotationYaw, entityIn.rotationPitch);
            else entityIn.setLocationAndAngles(d5, d6, d7, entityIn.rotationYaw, entityIn.rotationPitch);
            return true;
        }
        else return false;
    }
}
