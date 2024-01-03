package mods.thecomputerizer.dimhoppertweaks.mixin.mods.infernalmobs;

import atomicstryker.infernalmobs.common.InfernalMobsCore;
import atomicstryker.infernalmobs.common.mods.MM_Choke;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = MM_Choke.class, remap = false)
public class MixinMM_Choke {

    @Shadow private EntityLivingBase lastTarget;

    @Shadow private int lastAir;

    /**
     * @author The_Computerizer
     * @reason Fix air being able to go past max value
     */
    @Overwrite
    private void updateAir() {
        this.lastTarget.setAir(this.lastAir);
        if(this.lastTarget instanceof EntityPlayerMP) {
            InfernalMobsCore.instance().sendAirPacket((EntityPlayerMP)this.lastTarget,MathHelper.clamp(this.lastAir,0,300));
            InfernalMobsCore.instance().getModifiedPlayerTimes().put(this.lastTarget.getName(),System.currentTimeMillis());
        }

    }
}
