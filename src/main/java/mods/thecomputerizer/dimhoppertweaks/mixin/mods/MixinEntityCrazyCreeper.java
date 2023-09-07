package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import cavern.entity.monster.EntityCavenicCreeper;
import cavern.entity.monster.EntityCrazyCreeper;
import cavern.world.CaveDimensions;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityCrazyCreeper.class, remap = false)
public abstract class MixinEntityCrazyCreeper extends EntityCavenicCreeper {

    @Shadow @Final private BossInfoServer bossInfo;

    @Unique private boolean dimhoppertweaks$inCavenia = true;

    public MixinEntityCrazyCreeper(World world) {
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
}
