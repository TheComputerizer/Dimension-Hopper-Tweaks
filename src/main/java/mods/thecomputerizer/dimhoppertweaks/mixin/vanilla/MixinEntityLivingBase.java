package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.mixin.api.IEntityLivinBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase implements IEntityLivinBase {

    @Unique private boolean dimhoppertweaks$isBlighted;

    @Unique
    private EntityLivingBase dimhoppertweaks$cast() {
        return (EntityLivingBase)(Object)this;
    }

    @Override
    public void dimhoppertweaks$setBlight(boolean isBlighted) {
        this.dimhoppertweaks$isBlighted = isBlighted;
    }

    @Override
    public boolean dimhoppertweaks$isBlighted() {
        return this.dimhoppertweaks$isBlighted;
    }

    @Inject(at = @At("RETURN"), method = "readEntityFromNBT")
    private void dimhoppertweaks$readEntityFromNBT(NBTTagCompound compound, CallbackInfo ci) {
        if(dimhoppertweaks$cast().getEntityData().getBoolean("ScalingHealth.IsBlight"))
            dimhoppertweaks$setBlight(true);
    }
}