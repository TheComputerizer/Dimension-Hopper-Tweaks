package mods.thecomputerizer.dimhoppertweaks.mixin.mods.scalinghealth;

import mcp.MethodsReturnNonnullByDefault;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.silentchaos512.lib.util.Color;
import net.silentchaos512.scalinghealth.item.ItemDifficultyChanger;
import net.silentchaos512.scalinghealth.utils.SHPlayerDataHandler;
import net.silentchaos512.scalinghealth.utils.SHPlayerDataHandler.PlayerData;
import org.dimdev.dimdoors.DimDoors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

import static net.minecraft.util.EnumActionResult.PASS;
import static net.minecraft.util.EnumActionResult.SUCCESS;
import static net.minecraft.util.SoundCategory.PLAYERS;
import static net.silentchaos512.gems.SilentGems.logHelper;
import static net.silentchaos512.scalinghealth.ScalingHealth.random;
import static net.silentchaos512.scalinghealth.ScalingHealth.proxy;
import static net.silentchaos512.scalinghealth.config.Config.Items.cursedHeartChange;
import static net.silentchaos512.scalinghealth.config.Config.Items.enchantedHeartChange;
import static net.silentchaos512.scalinghealth.init.ModSounds.CURSED_HEART_USE;
import static net.silentchaos512.scalinghealth.init.ModSounds.ENCHANTED_HEART_USE;
import static net.silentchaos512.scalinghealth.lib.EnumModParticles.CURSED_HEART;
import static net.silentchaos512.scalinghealth.lib.EnumModParticles.ENCHANTED_HEART;

@Mixin(value = ItemDifficultyChanger.class, remap = false)
@ParametersAreNonnullByDefault @MethodsReturnNonnullByDefault
public class MixinItemDifficultyChanger extends Item {
    
    /**
     * @author The_Computerizer
     * @reason Add min/max difficulty caps depending on progression
     */
    @Overwrite(remap = true)
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        PlayerData data = SHPlayerDataHandler.get(player);
        if(Objects.isNull(data)) {
            player.getCooldownTracker().setCooldown(this,200);
            return new ActionResult<>(PASS,stack);
        }
        else {
            double particleX = player.posX;
            double particleY = player.posY+(0.65d*player.height);
            double particleZ = player.posZ;
            double zSpeed;
            int i;
            double xSpeed;
            double ySpeed;
            int meta = stack.getItemDamage();
            switch(meta) {
                case 0:
                    double min = DelayedModAccess.getMinDifficulty(player);
                    double maxDecrease = data.getDifficulty()-min;
                    if(maxDecrease<=0d) {
                        if(!world.isRemote)
                            DimDoors.chat(player,"Forbidden knowledge renders the enchanted heart less effective (Minimum = "+min+")");
                        player.getCooldownTracker().setCooldown(this,40);
                        return new ActionResult<>(PASS,stack);
                    }
                    if(!world.isRemote) {
                        data.incrementDifficulty(Math.max(-maxDecrease,enchantedHeartChange));
                        stack.shrink(1);
                    }
                    for(i=0;i<20-5*proxy.getParticleSettings();i++) {
                        xSpeed = 0.08d*random.nextGaussian();
                        ySpeed = 0.05d*random.nextGaussian();
                        zSpeed = 0.08d*random.nextGaussian();
                        proxy.spawnParticles(ENCHANTED_HEART,new Color(1f,1f,0.5f),world,particleX,
                                             particleY,particleZ,xSpeed,ySpeed,zSpeed);
                    }
                    world.playSound(null,player.getPosition(),ENCHANTED_HEART_USE,PLAYERS,0.4f,1.7f);
                    player.getCooldownTracker().setCooldown(this,40);
                    return new ActionResult<>(SUCCESS,stack);
                case 1:
                    double max = DelayedModAccess.getMaxDifficulty(player);
                    double maxIncrease = max-data.getDifficulty();
                    if(maxIncrease<=0d) {
                        if(!world.isRemote)
                            DimDoors.chat(player,"The cursed heart requires more knowlege to use (Maximum = "+max+")");
                        player.getCooldownTracker().setCooldown(this,40);
                        return new ActionResult<>(PASS,stack);
                    }
                    if(!world.isRemote) {
                        data.incrementDifficulty(Math.min(maxIncrease,cursedHeartChange));
                        stack.shrink(1);
                    }
                    for(i=0;i<20-5*proxy.getParticleSettings();i++) {
                        xSpeed = 0.08d*random.nextGaussian();
                        ySpeed = 0.05d*random.nextGaussian();
                        zSpeed = 0.08d*random.nextGaussian();
                        proxy.spawnParticles(CURSED_HEART,new Color(0.4f,0f,0.6f),world,particleX,
                                             particleY,particleZ,xSpeed,ySpeed,zSpeed);
                    }
                    world.playSound(null,player.getPosition(),CURSED_HEART_USE,PLAYERS,0.3f,
                                    (float)(0.699999988079071d+0.05000000074505806d*random.nextGaussian()));
                    player.getCooldownTracker().setCooldown(this,40);
                    return new ActionResult<>(SUCCESS,stack);
                default:
                    logHelper.warn("DifficultyChanger invalid meta: {}",meta);
                    player.getCooldownTracker().setCooldown(this,40);
                    return new ActionResult<>(PASS,stack);
            }
        }
    }
}