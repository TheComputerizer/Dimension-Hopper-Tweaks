package mods.thecomputerizer.dimhoppertweaks.mixin.mods.scalinghealth;

import mods.thecomputerizer.dimhoppertweaks.mixin.access.DelayedModAccess;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.silentchaos512.scalinghealth.config.Config.*;
import net.silentchaos512.scalinghealth.event.ScalingHealthCommonEvents;
import net.silentchaos512.scalinghealth.utils.SHPlayerDataHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Objects;

@Mixin(value = ScalingHealthCommonEvents.class, remap = false)
public class MixinScalingHealthCommonEvents {

    /**
     * @author The_Computerizer
     * @reason Account for gamestage scaling
     */
    @Overwrite
    @SubscribeEvent
    public void onPlayerSleepInBed(PlayerSleepInBedEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        if(!player.world.isRemote && Client.Difficulty.warnWhenSleeping && Difficulty.forSleeping!=0f) {
            double actualIncrease = DelayedModAccess.incrementDifficultyWithStageFactor(player,Difficulty.forSleeping);
            player.sendMessage(new TextComponentTranslation("misc.dimhoppertweaks.sleep_warning",actualIncrease));
        }
    }

    /**
     * @author The_Computerizer
     * @reason Add gamestage based difficulty scaling
     */
    @Overwrite
    @SubscribeEvent
    public void onPlayerWakeUp(PlayerWakeUpEvent event) {
        if(!event.getEntityPlayer().world.isRemote && !event.updateWorld()) {
            EntityPlayer player = event.getEntityPlayer();
            SHPlayerDataHandler.PlayerData data = SHPlayerDataHandler.get(player);
            if(Objects.nonNull(data))
                data.incrementDifficulty(DelayedModAccess.incrementDifficultyWithStageFactor(player,Difficulty.forSleeping),false);
        }

    }
}
