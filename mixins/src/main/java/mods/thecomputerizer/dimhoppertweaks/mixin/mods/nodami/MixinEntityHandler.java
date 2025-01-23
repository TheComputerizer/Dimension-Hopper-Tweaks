package mods.thecomputerizer.dimhoppertweaks.mixin.mods.nodami;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import mods.thecomputerizer.dimhoppertweaks.registry.traits.ExtendedEventsTrait;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import profhugo.nodami.config.NodamiConfig;
import profhugo.nodami.handlers.EntityHandler;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import static net.minecraftforge.fml.common.eventhandler.EventPriority.LOWEST;

@Mixin(value = EntityHandler.class, remap = false)
public abstract class MixinEntityHandler {

    /**
     * @author The_Computerizer
     * @reason Allow for skill traits to hook the thresholds
     */
    @Overwrite
    @SubscribeEvent(priority = LOWEST)
    public void onPlayerAttack(AttackEntityEvent event) {
        if(!event.isCanceled()) {
            EntityPlayer player = event.getEntityPlayer();
            if(player.getEntityWorld().isRemote) return;
            PlayerData data = PlayerDataHandler.get(player);
            AtomicBoolean skip = new AtomicBoolean(false);
            if(Objects.nonNull(data)) {
                SkillWrapper.executeOnSkills(data,h -> {
                    if(h instanceof ExtendedEventsTrait && ((ExtendedEventsTrait)h).shouldCancelNoDamiThresholds())
                        skip.set(true);
                });
            }
            if(!skip.get()) {
                float str = player.getCooledAttackStrength(0f);
                if(str<=NodamiConfig.thresholds.attackCancelThreshold) {
                    event.setCanceled(true);
                    return;
                }
                if(str<=NodamiConfig.thresholds.knockbackCancelThreshold) player.hurtTime = -1;
            }
        }
    }
}