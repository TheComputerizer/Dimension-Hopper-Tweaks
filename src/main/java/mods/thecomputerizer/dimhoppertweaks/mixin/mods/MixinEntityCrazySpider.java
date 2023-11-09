package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import cavern.entity.monster.EntityCavenicSpider;
import cavern.entity.monster.EntityCrazySpider;
import cavern.world.CaveDimensions;
import mods.thecomputerizer.dimhoppertweaks.client.particle.ParticleBlightFire;
import net.minecraft.entity.player.EntityPlayerMP;
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
            int var1 = this.rand.nextInt(2)*2-1;
            int var2 = this.rand.nextInt(2)*2-1;
            double ptX = this.posX+0.25*(double)var1;
            double ptY = this.posY+0.65+(double)this.rand.nextFloat();
            double ptZ = this.posZ+0.25*(double)var2;
            double motionX = this.rand.nextFloat()*1f*(float)var1;
            double motionY = ((double)this.rand.nextFloat()-0.25)*0.125;
            double motionZ = this.rand.nextFloat()*1f*(float)var2;
            ParticleBlightFire particle = new ParticleBlightFire(this.world,ptX,ptY,ptZ,motionX,motionY,motionZ,
                    100f,32d,0.5f);
            particle.setCenter(ptX,ptY,ptZ);
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(particle);
        }
    }
}