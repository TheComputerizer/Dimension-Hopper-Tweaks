package mods.thecomputerizer.dimensionhoppertweaks.common;

import mods.thecomputerizer.dimensionhoppertweaks.DimensionHopperTweaks;
import mods.thecomputerizer.dimensionhoppertweaks.common.objects.DimensionHopperSounds;
import mods.thecomputerizer.dimensionhoppertweaks.common.objects.entity.EntityFinalBoss;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber(modid = DimensionHopperTweaks.MODID)
public class CommonEvents {

    public static int bossSpawnCounter = -1;
    private static WorldServer worldS;
    private static BlockPos posS;

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent ev) {
        if(ev.getEntityLiving() instanceof EntityFinalBoss) {
            if(!ev.getSource().damageType.matches("infinity")) {
                ev.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void serverTick(TickEvent.ServerTickEvent ev) {
        if(ev.phase==TickEvent.Phase.END) {
            if (bossSpawnCounter > 0) bossSpawnCounter--;
            else if (bossSpawnCounter == 0) {
                EntityFinalBoss boss = new EntityFinalBoss(worldS);
                boss.setPositionAndUpdate(posS.getX(), posS.getY(), posS.getZ());
                worldS.spawnEntity(boss);
                bossSpawnCounter--;
            }
        }
    }

    public static void startSummonBoss(WorldServer world, BlockPos pos) {
        worldS = world;
        posS = pos;
        bossSpawnCounter = 302;
        world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), DimensionHopperSounds.SPAWN, SoundCategory.MASTER, 1.0F, 1.0F);
    }
}
