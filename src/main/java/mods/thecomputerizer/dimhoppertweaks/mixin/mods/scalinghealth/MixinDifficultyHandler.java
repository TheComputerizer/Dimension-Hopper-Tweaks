package mods.thecomputerizer.dimhoppertweaks.mixin.mods.scalinghealth;

import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.silentchaos512.scalinghealth.ScalingHealth;
import net.silentchaos512.scalinghealth.config.Config;
import net.silentchaos512.scalinghealth.event.BlightHandler;
import net.silentchaos512.scalinghealth.event.DifficultyHandler;
import net.silentchaos512.scalinghealth.utils.EntityDifficultyChangeList;
import net.silentchaos512.scalinghealth.utils.SHPlayerDataHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Objects;

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
            EntityDifficultyChangeList.DifficultyChanges changes = Config.Difficulty.DIFFICULTY_PER_KILL_BY_MOB.get(killed);
            EntityPlayer player = (EntityPlayer)source.getTrueSource();
            SHPlayerDataHandler.PlayerData data = SHPlayerDataHandler.get(player);
            if(Objects.nonNull(data)) {
                boolean isBlight = BlightHandler.isBlight(killed);
                float amount = isBlight ? changes.onBlightKill : changes.onStandardKill;
                if(Config.Debug.debugMode)
                    ScalingHealth.LOGGER.debug("Killed "+(isBlight ? "Blight " : "")+killed.getName()+
                            ": difficulty"+(amount>0f ? "+" : "")+amount);
                data.incrementDifficulty(DelayedModAccess.incrementDifficultyWithStageFactor(player,amount),true);
            }
        }
    }
}