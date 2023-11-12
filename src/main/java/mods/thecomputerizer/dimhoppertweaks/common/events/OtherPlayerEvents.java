package mods.thecomputerizer.dimhoppertweaks.common.events;

import mods.thecomputerizer.dimhoppertweaks.client.render.ClientEffects;
import mods.thecomputerizer.dimhoppertweaks.common.capability.ISkillCapability;
import mods.thecomputerizer.dimhoppertweaks.common.capability.SkillCapability;
import mods.thecomputerizer.dimhoppertweaks.common.capability.SkillWrapper;
import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class OtherPlayerEvents {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void breakSpeed(BreakSpeed event) {
        if(event.isCanceled()) return;
        if(Objects.nonNull(event.getEntityPlayer())) {
            float speedFactor = 1;
            if(event.getEntityPlayer() instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP)event.getEntityPlayer();
                if (Objects.nonNull(player)) {
                    ISkillCapability cap = SkillWrapper.getSkillCapability(player);
                    if(Objects.nonNull(cap)) speedFactor = 1f+cap.getBreakSpeedMultiplier();
                }
            } else speedFactor = ClientEffects.MINING_SPEED;
            event.setNewSpeed(event.getNewSpeed()*speedFactor);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerClone(Clone event) {
        if(event.isCanceled()) return;
        if(event.getEntityPlayer() instanceof EntityPlayerMP) {
            EntityPlayerMP to = (EntityPlayerMP)event.getEntityPlayer();
            ISkillCapability capTo = SkillWrapper.getSkillCapability(to);
            if(Objects.nonNull(capTo)) {
                EntityPlayerMP from = (EntityPlayerMP)event.getOriginal();
                ISkillCapability capFrom = SkillWrapper.getSkillCapability(from);
                if(Objects.nonNull(capFrom)) capTo.of((SkillCapability)capFrom,to);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBoneMeal(BonemealEvent event) {
        if(!event.getWorld().isRemote) {
            double prestigeFactor = (SkillWrapper.getPrestigeFactor(event.getEntityPlayer(),"farming")-1d)/32d;
            if(event.getWorld().rand.nextDouble()<=prestigeFactor) event.getStack().grow(1);
        }
    }
}
