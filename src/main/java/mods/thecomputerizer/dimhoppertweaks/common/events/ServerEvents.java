package mods.thecomputerizer.dimhoppertweaks.common.events;

import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.boss.EntityFinalBoss;
import mods.thecomputerizer.dimhoppertweaks.common.skills.SkillCapabilityProvider;
import mods.thecomputerizer.dimhoppertweaks.common.skills.SkillWrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class ServerEvents {
    public static int BOSS_SPAWN_COUNT = -1;
    private static WorldServer WORLD_SERVER;
    private static BlockPos POS_SERVER;

    @SubscribeEvent
    public static void serverTick(TickEvent.ServerTickEvent ev) {
        if(ev.phase==TickEvent.Phase.END) {
            if (BOSS_SPAWN_COUNT > 0) BOSS_SPAWN_COUNT--;
            else if (BOSS_SPAWN_COUNT == 0) {
                EntityFinalBoss boss = new EntityFinalBoss(WORLD_SERVER);
                boss.setPositionAndUpdate(POS_SERVER.getX(), POS_SERVER.getY(), POS_SERVER.getZ());
                WORLD_SERVER.spawnEntity(boss);
                BOSS_SPAWN_COUNT--;
            }
        }
    }

    public static void startSummonBoss(WorldServer world, BlockPos pos) {
        WORLD_SERVER = world;
        POS_SERVER = pos;
        BOSS_SPAWN_COUNT = 162;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof EntityPlayerMP)
            event.addCapability(SkillWrapper.SKILL_CAPABILITY, new SkillCapabilityProvider());
    }

    @SuppressWarnings("ConstantValue")
    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        EntityPlayer player = event.player;
        BlockPos pos = SkillWrapper.getSkillCapability(player).getTwilightRespawn();
        if(player.dimension==7 && Objects.isNull(player.getBedLocation(7)) && Objects.nonNull(pos))
            player.setSpawnPoint(pos,true);
    }
}
