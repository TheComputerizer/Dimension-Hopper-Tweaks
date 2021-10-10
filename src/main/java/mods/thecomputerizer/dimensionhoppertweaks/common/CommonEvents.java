package mods.thecomputerizer.dimensionhoppertweaks.common;

import mods.thecomputerizer.dimensionhoppertweaks.DimensionHopperTweaks;
import mods.thecomputerizer.dimensionhoppertweaks.common.objects.entity.EntityFinalBoss;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = DimensionHopperTweaks.MODID)
public class CommonEvents {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent ev) {
        if(ev.getEntityLiving() instanceof EntityFinalBoss) {
            if(!ev.getSource().damageType.matches("infinity")) {
                ev.setCanceled(true);
            }
        }
    }
}
