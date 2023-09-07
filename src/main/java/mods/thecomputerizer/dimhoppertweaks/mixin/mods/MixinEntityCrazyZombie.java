package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import cavern.entity.monster.EntityCavenicZombie;
import cavern.entity.monster.EntityCrazyZombie;
import cavern.world.CaveDimensions;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityCrazyZombie.class, remap = false)
public abstract class MixinEntityCrazyZombie extends EntityCavenicZombie {

    @Shadow @Final private BossInfoServer bossInfo;

    @Unique private boolean dimhoppertweaks$inCavenia = true;

    public MixinEntityCrazyZombie(World world) {
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
            boolean canSee = false;
            double distance = -1d;
            for(EntityPlayerMP player : this.bossInfo.getPlayers()) {
                distance = this.getDistance(player);
                if (this.canEntityBeSeen(player) && distance<30d) {
                    canSee = true;
                    break;
                }
            }
            this.bossInfo.setDarkenSky(distance<30d);
            this.bossInfo.setVisible(canSee);
        }
        this.bossInfo.setPercent(this.getHealth()/this.getMaxHealth());
    }
}
