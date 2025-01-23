package mods.thecomputerizer.dimhoppertweaks.mixin.mods.twilightforest;

import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import twilightforest.item.ItemTFPeacockFan;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.Random;

import static net.minecraft.init.MobEffects.JUMP_BOOST;
import static net.minecraft.init.SoundEvents.ENTITY_PLAYER_BREATH;
import static net.minecraft.util.EnumActionResult.SUCCESS;
import static net.minecraft.util.EnumParticleTypes.CLOUD;

@ParametersAreNonnullByDefault
@Mixin(value = ItemTFPeacockFan.class, remap = false)
public abstract class MixinItemTFPeacockFan extends Item {

    @Shadow protected abstract int doFan(World world, EntityPlayer player);
    @Shadow protected abstract AxisAlignedBB getEffectAABB(EntityPlayer player);

    /**
     * @author The_Computerizer
     * @reason Nerf peacock feather fan in respect to slime slings & hang gliders
     */
    @Overwrite(remap = true)
    public @Nonnull ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if(player.onGround) {
            if(world.isRemote) {
                AxisAlignedBB fanBox = this.getEffectAABB(player);
                Vec3d lookVec = player.getLookVec();
                for(int i=0;i<30;i++)
                    world.spawnParticle(CLOUD,
                            fanBox.minX+(double)world.rand.nextFloat()*(fanBox.maxX-fanBox.minX),
                            fanBox.minY+(double)world.rand.nextFloat()*(fanBox.maxY-fanBox.minY),
                            fanBox.minZ+(double)world.rand.nextFloat()*(fanBox.maxZ-fanBox.minZ),
                            lookVec.x,lookVec.y,lookVec.z);
            } else {
                int fanned = this.doFan(world,player);
                if(fanned>0) stack.damageItem(fanned,player);
            }
            dimhoppertweaks$playBreathSound(player,itemRand);
        } else {
            Potion jump = JUMP_BOOST;
            int jumpBoostAmplifier = 0;
            int itemDamage;
            PotionEffect jumpEffect = player.getActivePotionEffect(jump);
            if(Objects.nonNull(jumpEffect)) jumpBoostAmplifier = jumpEffect.getAmplifier()+1;
            if(world.isRemote) {
                player.motionX*=3d;
                player.motionY = 1.5d+(0.5d*jumpBoostAmplifier);
                player.motionZ*=3d;
                player.fallDistance = 0f;
                dimhoppertweaks$playBreathSound(player,itemRand);
            } else {
                if(jumpBoostAmplifier==0) player.addPotionEffect(new PotionEffect(jump,45,0));
                itemDamage = dimhoppertweaks$calculateDamage(SkillWrapper.getFanUsage(player),jumpBoostAmplifier);
                stack.damageItem(itemDamage,player);
            }
            player.getCooldownTracker().setCooldown(this,60+(5*jumpBoostAmplifier));
        }
        player.setActiveHand(hand);
        return ActionResult.newResult(SUCCESS,stack);
    }

    @Unique
    private void dimhoppertweaks$playBreathSound(EntityPlayer player, Random rand) {
        player.playSound(ENTITY_PLAYER_BREATH,1f+rand.nextFloat(),rand.nextFloat()*0.7f+0.3f);
    }

    @Unique
    private int dimhoppertweaks$calculateDamage(float usageCount, float jumpBoostAmplifier) {
        return MathHelper.clamp(MathHelper.ceil((usageCount+1f)*(1.5f+(0.5f*jumpBoostAmplifier))),1,32);
    }
}