package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static java.lang.Integer.MAX_VALUE;
import static mods.thecomputerizer.dimhoppertweaks.registry.TraitRegistry.SWIMMING_LESSONS;
import static net.minecraft.entity.SharedMonsterAttributes.MAX_HEALTH;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends EntityLivingBase {

    @Shadow @Final private static DataParameter<Integer> PLAYER_SCORE;

    public MixinEntityPlayer(World world) {
        super(world);
    }
    
    @SuppressWarnings("DataFlowIssue")
    @Unique private EntityPlayer dimhoppertweaks$cast() {
        return (EntityPlayer)(Object)this;
    }

    /**
     * @author The_Computerizer
     * @reason Handle negative amounts for easily removing XP
     */
    @Overwrite
    public void addExperience(int amount) {
        EntityPlayer player = dimhoppertweaks$cast();
        int max = MAX_VALUE-player.experienceTotal;
        if(amount>max) amount = max;
        int min = -player.experienceTotal;
        if(amount<min) amount = min;
        if(amount==0) return;
        this.addScore(amount);
        player.experience+=(float)amount/(float)player.xpBarCap();
        player.experienceTotal+=amount;
        if(amount>0) {
            while(player.experience>=1f) {
                player.experience = (player.experience-1f)*(float)player.xpBarCap();
                player.addExperienceLevel(1);
                player.experience/=(float)player.xpBarCap();
            }
        } else {
            while(player.experience<0f) {
                player.experience = (player.experience+1f)*(float)player.xpBarCap();
                player.addExperienceLevel(-1);
                player.experience/=(float)player.xpBarCap();
            }
        }
    }

    /**
     * @author The_Computerizer
     * @reason Handle negative amounts for easily removing XP
     */
    @Overwrite
    public void addScore(int score) {
        EntityPlayer player = dimhoppertweaks$cast();
        this.dataManager.set(PLAYER_SCORE,MathHelper.clamp(player.getScore()+score,0,MAX_VALUE));
    }
    
    @Override public void setInWeb() {
        if(!SkillWrapper.isUnstoppable(dimhoppertweaks$cast())) super.setInWeb();
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;"+
            "processInitialInteract(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/EnumHand;)Z"),
            method = "interactOn")
    private boolean dimhoppertweaks$fixTamedHealth(Entity entity, EntityPlayer player, EnumHand hand) {
        if(entity instanceof EntityTameable) {
            EntityTameable tameable = (EntityTameable)entity;
            boolean wasTamed = tameable.isTamed();
            boolean ret = entity.processInitialInteract(player,hand);
            if(!wasTamed && tameable.isTamed())
                tameable.setHealth((float)this.getEntityAttribute(MAX_HEALTH).getAttributeValue());
            return ret;
        }
        return entity.processInitialInteract(player,hand);
    }
    
    @Redirect(at =@At(value="INVOKE", target="Lnet/minecraft/entity/ai/attributes/IAttributeInstance;setBaseValue(D)V"),
            method = "onLivingUpdate")
    private void dimhoppertweaks$applySwimSpeed(IAttributeInstance instance, double v) {
        if(this.inWater && SkillWrapper.hasTrait(dimhoppertweaks$cast(),"agility",SWIMMING_LESSONS)) v*=2d;
        instance.setBaseValue(v);
    }
}