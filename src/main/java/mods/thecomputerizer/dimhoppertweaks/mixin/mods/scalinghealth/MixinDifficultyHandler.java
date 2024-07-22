package mods.thecomputerizer.dimhoppertweaks.mixin.mods.scalinghealth;

import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.silentchaos512.scalinghealth.event.BlightHandler;
import net.silentchaos512.scalinghealth.event.DifficultyHandler;
import net.silentchaos512.scalinghealth.utils.EntityDifficultyChangeList.DifficultyChanges;
import net.silentchaos512.scalinghealth.utils.SHPlayerDataHandler;
import net.silentchaos512.scalinghealth.utils.SHPlayerDataHandler.PlayerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Objects;

import static net.silentchaos512.scalinghealth.ScalingHealth.LOGGER;
import static net.silentchaos512.scalinghealth.config.Config.Debug.debugMode;
import static net.silentchaos512.scalinghealth.config.Config.Difficulty.DIFFICULTY_PER_KILL_BY_MOB;

@Mixin(value = DifficultyHandler.class, remap = false)
public abstract class MixinDifficultyHandler {

    /**
     * @author The_Computerizer
     * @reason Add gamestage based difficulty scaling
     */
    @Overwrite
    @SubscribeEvent
    public void onMobDeath(LivingDeathEvent event) {
        EntityLivingBase killed = event.getEntityLiving();
        DamageSource source = event.getSource();
        if(source.getTrueSource() instanceof EntityPlayer) {
            DifficultyChanges changes = DIFFICULTY_PER_KILL_BY_MOB.get(killed);
            EntityPlayer player = (EntityPlayer)source.getTrueSource();
            PlayerData data = SHPlayerDataHandler.get(player);
            if(Objects.nonNull(data)) {
                double maxIncrease = DelayedModAccess.getMaxDifficulty(player)-data.getDifficulty();
                if(maxIncrease>0d) {
                    boolean isBlight = BlightHandler.isBlight(killed);
                    float amount = isBlight ? changes.onBlightKill : changes.onStandardKill;
                    if(debugMode)
                        LOGGER.debug("Killed {}{}: difficulty{}{}",isBlight ? "Blight " : "",
                                                   killed.getName(),amount>0f ? "+" : "",amount);
                    double increase = DelayedModAccess.incrementDifficultyWithStageFactor(player,amount);
                    data.incrementDifficulty(Math.min(increase,maxIncrease),true);
                }
            }
        }
    }
}