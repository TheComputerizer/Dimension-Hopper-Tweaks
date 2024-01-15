package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends EntityLivingBase {

    @Shadow @Final private static DataParameter<Integer> PLAYER_SCORE;
    @Shadow public int experienceTotal;
    @Shadow public float experience;
    @Shadow public abstract int getScore();
    @Shadow public abstract int xpBarCap();
    @Shadow public abstract void addExperienceLevel(int levels);

    public MixinEntityPlayer(World world) {
        super(world);
    }

    /**
     * @author The_Computerizer
     * @reason Handle negative amounts for easily removing XP
     */
    @Overwrite
    public void addExperience(int amount) {
        int max = Integer.MAX_VALUE-this.experienceTotal;
        if(amount>max) amount = max;
        int min = -this.experienceTotal;
        if(amount<min) amount = min;
        if(amount==0) return;
        this.addScore(amount);
        this.experience+=(float)amount/(float)this.xpBarCap();
        this.experienceTotal+=amount;
        if(amount>0) {
            while(this.experience>=1f) {
                this.experience = (this.experience-1f)*(float)this.xpBarCap();
                this.addExperienceLevel(1);
                this.experience/=(float)this.xpBarCap();
            }
        }
        else {
            while(this.experience<0f) {
                this.experience = (this.experience+1f)*(float)this.xpBarCap();
                this.addExperienceLevel(-1);
                this.experience/=(float)this.xpBarCap();
            }
        }
    }

    /**
     * @author The_Computerizer
     * @reason Handle negative amounts for easily removing XP
     */
    @Overwrite
    public void addScore(int score) {
        this.dataManager.set(PLAYER_SCORE,MathHelper.clamp(this.getScore()+score,0,Integer.MAX_VALUE));
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
                tameable.setHealth((float)this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue());
            return ret;
        }
        return entity.processInitialInteract(player, hand);
    }
}