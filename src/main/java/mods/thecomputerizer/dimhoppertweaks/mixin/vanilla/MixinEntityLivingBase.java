package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.mixin.access.EntityLivinBaseAccess;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityLivingBase.class)
public class MixinEntityLivingBase implements EntityLivinBaseAccess {

    @Unique private boolean dimhoppertweaks$isBlighted;

    @Override
    public void dimhoppertweaks$setBlight(boolean isBlighted) {
        this.dimhoppertweaks$isBlighted = isBlighted;
    }

    @Override
    public boolean dimhoppertweaks$isBlighted() {
        return this.dimhoppertweaks$isBlighted;
    }

    @Inject(at = @At("RETURN"), method = "readEntityFromNBT")
    private void readEntityFromNBT(NBTTagCompound compound, CallbackInfo ci) {
        if(((EntityLivingBase)(Object)this).getEntityData().getBoolean("ScalingHealth.IsBlight"))
            dimhoppertweaks$setBlight(true);
    }
}
