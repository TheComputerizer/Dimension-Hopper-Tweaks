package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.mixin.api.IEntityLivinBase;
import mods.thecomputerizer.dimhoppertweaks.mixin.vanilla.access.ModifiableAttributeInstanceAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends Entity implements IEntityLivinBase {

    public MixinEntityLivingBase(World world) {
        super(world);
    }

    @Shadow public abstract IAttributeInstance getEntityAttribute(IAttribute attribute);

    @Shadow public abstract float getHealth();

    @Shadow @Final private static DataParameter<Float> HEALTH;
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

    /**
     * @author The_Computerizer
     * @reason Account for max health changes
     */
    @Overwrite
    public final float getMaxHealth() {
        IAttributeInstance attribute = this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
        if(attribute instanceof ModifiableAttributeInstance) {
            ModifiableAttributeInstanceAccess access = (ModifiableAttributeInstanceAccess)attribute;
            double cached  = access.getCachedValue();
            double newVal = attribute.getAttributeValue();
            if(newVal!=cached) this.dataManager.set(HEALTH,MathHelper.clamp(getHealth(),0f,(float)newVal));
            return (float)newVal;
        }
        return (float)attribute.getAttributeValue();
    }
}