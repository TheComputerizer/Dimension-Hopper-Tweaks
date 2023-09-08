package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.mixin.access.EntityLivinBaseAccess;
import mods.thecomputerizer.dimhoppertweaks.registry.ParticleRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP {

    @Shadow protected Minecraft mc;

    /**
     * @author The_Computerizer
     * @reason Spawn blight fire particles if its a blight
     */
    @Overwrite
    public void onCriticalHit(Entity entityHit) {
        boolean isBLight = false;
        if(entityHit instanceof EntityLivingBase) {
            EntityLivingBase based = (EntityLivingBase)entityHit;
            if(((EntityLivinBaseAccess)based).dimhoppertweaks$isBlighted())
                isBLight = true;
        }
        if(isBLight) this.mc.effectRenderer.emitParticleAtEntity(entityHit, ParticleRegistry.BLIGHT_FIRE);
        else this.mc.effectRenderer.emitParticleAtEntity(entityHit, EnumParticleTypes.CRIT);
    }
}
