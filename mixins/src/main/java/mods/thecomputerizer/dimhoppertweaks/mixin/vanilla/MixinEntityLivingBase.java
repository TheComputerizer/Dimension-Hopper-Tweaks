package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.IEntityLivingBase;
import mods.thecomputerizer.dimhoppertweaks.mixin.vanilla_access.ModifiableAttributeInstanceAccess;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

import static net.minecraft.entity.SharedMonsterAttributes.MAX_HEALTH;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends Entity implements IEntityLivingBase {

    @Shadow @Final private static DataParameter<Float> HEALTH;
    @Unique private boolean dimhoppertweaks$isBlighted;
    @Shadow public abstract float getHealth();
    @Shadow public abstract IAttributeInstance getEntityAttribute(IAttribute attribute);

    public MixinEntityLivingBase(World world) {
        super(world);
    }

    @Unique private EntityLivingBase dimhoppertweaks$cast() {
        return (EntityLivingBase)(Object)this;
    }
    
    @Unique private float dimhoppertweaks$getSlipperiness(
            Block block, IBlockState state, IBlockAccess world, BlockPos pos, @Nullable Entity entity) {
        return SkillWrapper.isUnstoppable(entity) ? 0.6f : block.getSlipperiness(state,world,pos,entity);
    }
    
    @Redirect(at = @At(value="INVOKE", target="Lnet/minecraft/entity/ai/attributes/IAttributeInstance;"+
                                              "getAttributeValue()D"), method = "handleJumpLava")
    private double dimhoppertweaks$handleJumpLava(IAttributeInstance instance) {
        double value = instance.getAttributeValue();
        if(SkillWrapper.isGoodSwimmer(dimhoppertweaks$cast())) value*=2d;
        return value;
    }
    
    @Redirect(at = @At(value="INVOKE", target="Lnet/minecraft/entity/ai/attributes/IAttributeInstance;"+
                                              "getAttributeValue()D"), method = "handleJumpWater")
    private double dimhoppertweaks$handleJumpWater(IAttributeInstance instance) {
        double value = instance.getAttributeValue();
        if(SkillWrapper.isGoodSwimmer(dimhoppertweaks$cast())) value*=2d;
        return value;
    }

    @Override public boolean dimhoppertweaks$isBlighted() {
        return this.dimhoppertweaks$isBlighted;
    }

    @Inject(at = @At("RETURN"), method = "readEntityFromNBT")
    private void dimhoppertweaks$readEntityFromNBT(NBTTagCompound compound, CallbackInfo ci) {
        if(dimhoppertweaks$cast().getEntityData().getBoolean("ScalingHealth.IsBlight"))
            dimhoppertweaks$setBlight(true);
    }
    
    @Redirect(at = @At(value="INVOKE", target="Lnet/minecraft/entity/ai/attributes/IAttributeInstance;"+
                                   "getAttributeValue()D", ordinal = 0), method = "moveRelative")
    private double dimhoppertweaks$moveRelative1(IAttributeInstance instance) {
        double value = instance.getAttributeValue();
        if(SkillWrapper.isGoodSwimmer(dimhoppertweaks$cast())) value*=2d;
        return value;
    }
    
    @Redirect(at = @At(value="INVOKE", target="Lnet/minecraft/entity/ai/attributes/IAttributeInstance;"+
                                              "getAttributeValue()D", ordinal = 1), method = "moveRelative")
    private double dimhoppertweaks$moveRelative2(IAttributeInstance instance) {
        double value = instance.getAttributeValue();
        if(SkillWrapper.isGoodSwimmer(dimhoppertweaks$cast())) value*=2d;
        return value;
    }
    
    @Redirect(at = @At(value="INVOKE", target="Lnet/minecraft/entity/ai/attributes/IAttributeInstance;"+
                                              "getAttributeValue()D", ordinal = 2), method = "moveRelative")
    private double dimhoppertweaks$moveRelative3(IAttributeInstance instance) {
        double value = instance.getAttributeValue();
        if(SkillWrapper.isGoodSwimmer(dimhoppertweaks$cast())) value*=2d;
        return value;
    }
    
    @Override public void dimhoppertweaks$setBlight(boolean isBlighted) {
        this.dimhoppertweaks$isBlighted = isBlighted;
    }
    
    @Redirect(at = @At(value="INVOKE", target="Lnet/minecraft/block/Block;getSlipperiness("+
                                   "Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;"+
                                   "Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;)F",
            ordinal = 0, remap = false), method = "travel")
    private float dimhoppertweaks$checkSlip1(
            Block block, IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
        return dimhoppertweaks$getSlipperiness(block,state,world,pos,entity);
    }
    
    @Redirect(at = @At(value="INVOKE", target="Lnet/minecraft/block/Block;getSlipperiness("+
                                              "Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;"+
                                              "Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;)F",
            ordinal = 1, remap = false), method = "travel")
    private float dimhoppertweaks$checkSlip2(
            Block block, IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
        return dimhoppertweaks$getSlipperiness(block,state,world,pos,entity);
    }

    /**
     * @author The_Computerizer
     * @reason Account for max health changes
     */
    @Overwrite
    public final float getMaxHealth() {
        IAttributeInstance attribute = this.getEntityAttribute(MAX_HEALTH);
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