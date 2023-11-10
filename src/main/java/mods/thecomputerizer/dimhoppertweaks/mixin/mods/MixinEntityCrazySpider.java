package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import cavern.entity.monster.EntityCavenicSpider;
import cavern.entity.monster.EntityCrazySpider;
import cavern.world.CaveDimensions;
import mods.thecomputerizer.dimhoppertweaks.client.particle.ParticleBlightFire;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityCrazySpider.class, remap = false)
public abstract class MixinEntityCrazySpider extends EntityCavenicSpider {

    @Shadow @Final private BossInfoServer bossInfo;

    @Unique private boolean dimhoppertweaks$inCavenia = true;

    public MixinEntityCrazySpider(World world) {
        super(world);
    }

    @Inject(at = @At("RETURN"), method = "<init>")
    private void dimhoppertweaks$init(World world, CallbackInfo ci) {
        if(world.provider.getDimensionType()!=CaveDimensions.CAVENIA) {
            this.dimhoppertweaks$inCavenia = false;
            this.bossInfo.setVisible(false);
        }
    }

    /**
     * @author The_Computerizer
     * @reason Fix weird boss bar issues
     */
    @Overwrite
    protected void updateAITasks() {
        super.updateAITasks();
        if(!this.world.isRemote && this.dimhoppertweaks$inCavenia) {
            boolean isCloseEnough = false;
            for(EntityPlayerMP player : this.bossInfo.getPlayers()) {
                if (this.getDistance(player)<30d) {
                    isCloseEnough = true;
                    break;
                }
            }
            this.bossInfo.setDarkenSky(isCloseEnough);
            this.bossInfo.setVisible(isCloseEnough);
        }
        this.bossInfo.setPercent(this.getHealth()/this.getMaxHealth());
    }

    /**
     * @author The_Computerizer
     * @reason Fix too many particals being rendered
     */
    @Overwrite
    @SideOnly(Side.CLIENT)
    public void onUpdate() {
        super.onUpdate();
        if(this.world.isRemote) {
            AxisAlignedBB aabb = this.getEntityBoundingBox();
            Vec3d center = aabb.getCenter();
            double xFlip = this.rand.nextInt(2)*2-1;
            double yFlip = this.rand.nextInt(2)*2-1;
            double zFlip = this.rand.nextInt(2)*2-1;
            double ptX = center.x+(Math.abs(aabb.maxX-aabb.minX)*xFlip);
            double ptY = center.y+(Math.abs(aabb.maxY-aabb.minY)*0.75d*yFlip);
            double ptZ = center.z+(Math.abs(aabb.maxZ-aabb.minZ)*zFlip);
            double motionX = this.rand.nextDouble()*xFlip;
            double motionY = this.rand.nextDouble()*0.75d*yFlip;
            double motionZ = this.rand.nextDouble()*zFlip;
            ParticleBlightFire particle = new ParticleBlightFire(this.world,ptX,ptY,ptZ,motionX,motionY,motionZ,
                    100f,32d,0.5f,false);
            particle.setCenter(center);
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(particle);
        }
    }
}