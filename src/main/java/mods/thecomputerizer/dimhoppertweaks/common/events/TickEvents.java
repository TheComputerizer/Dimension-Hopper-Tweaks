package mods.thecomputerizer.dimhoppertweaks.common.events;

import bedrockcraft.ModWorlds;
import lumien.randomthings.item.ModItems;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.ISkillCapability;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import mods.thecomputerizer.dimhoppertweaks.common.commands.DHDebugCommands;
import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.IItemTimeInABottle;
import mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.EntityFinalBoss;
import mods.thecomputerizer.dimhoppertweaks.util.ItemUtil;
import mods.thecomputerizer.dimhoppertweaks.util.WorldUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.*;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.lang3.mutable.MutableInt;
import zollerngalaxy.core.dimensions.ZGDimensions;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = DHTRef.MODID)
public class TickEvents {

    private static final MutableInt TICK_DELAY = new MutableInt();
    public static int bossSpawnCount = -1;
    private static WorldServer worldServer;
    private static BlockPos blockPos;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if(event.isCanceled()) return;
        if(event.phase==TickEvent.Phase.END) {
            if(event.side==Side.SERVER) {
                if(TICK_DELAY.getAndAdd(1)>=20) {
                    EntityPlayerMP player = (EntityPlayerMP) event.player;
                    if(player.isSprinting()) {
                        int speedFactor = player.isPotionActive(MobEffects.SPEED) ? Objects.requireNonNull(
                                player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier()+2 : 1;
                        SkillWrapper.addActionSP(player,"agility",speedFactor);
                    }
                    ISkillCapability cap = SkillWrapper.getSkillCapability(player);
                    if(Objects.nonNull(cap)) cap.decrementGatheringItems(20);
                    if(player.dimension==ZGDimensions.CALIGRO.getId() && player.posY<0 &&
                            ItemUtil.isHolding(player,bedrockcraft.ModItems.blackTotem))
                        WorldUtil.teleportDimY(player,ModWorlds.VOID_WORLD.getId(),256d);
                    TICK_DELAY.setValue(0);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void serverTick(ServerTickEvent event) {
        if(event.isCanceled()) return;
        if(event.phase.ordinal()==1) {
            if (bossSpawnCount > 0) bossSpawnCount--;
            else if (bossSpawnCount == 0) {
                EntityFinalBoss boss = new EntityFinalBoss(worldServer);
                boss.setPositionAndUpdate(blockPos.getX(),blockPos.getY(),blockPos.getZ());
                worldServer.spawnEntity(boss);
                bossSpawnCount--;
            }
        }
    }

    public static void startSummonBoss(WorldServer world, BlockPos pos) {
        worldServer = world;
        blockPos = pos;
        bossSpawnCount = 162;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void worldTick(WorldTickEvent event) {
        if(event.isCanceled()) return;
        if(event.phase.ordinal()==1) {
            int dim = event.world.provider.getDimension();
            if(dim==44 || dim==45) {
                synchronized(event.world.playerEntities) {
                    EntityPlayerMP wakingUp = null;
                    for(EntityPlayer p : event.world.playerEntities) {
                        EntityPlayerMP player = (EntityPlayerMP) p;
                        int ticks = checkTime(player.getHeldItemOffhand()) || checkTime(player.getHeldItemMainhand()) ? -1 : 1;
                        if(SkillWrapper.ticKDreamer(player, ticks)) {
                            wakingUp = player;
                            break;
                        }
                    }
                    //This needs to be outside the loop to avoid a cmod error since teleporting removes the player from the list
                    if(Objects.nonNull(wakingUp)) wakeUp(wakingUp);
                }
            }
        }
    }

    private static boolean checkTime(ItemStack stack) {
        return stack.getItem()== ModItems.timeInABottle &&
                ((IItemTimeInABottle)stack.getItem()).dimhoppertweaks$hasTime(stack);
    }

    @SuppressWarnings("ConstantValue")
    private static void wakeUp(EntityPlayerMP player) {
        ISkillCapability cap = SkillWrapper.getSkillCapability(player);
        if(Objects.nonNull(cap)) cap.resetDreamTimer();
        int respawnDim = player.getSpawnDimension();
        BlockPos respawnPos = player.getBedLocation(respawnDim);
        if(Objects.isNull(respawnPos)) respawnPos = player.getPosition();
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        server.getCommandManager().executeCommand(server, DHDebugCommands.buildRawCommand("tpx",player.getName(),
                respawnPos.getX(),respawnPos.getY(),respawnPos.getZ(),respawnDim));
    }
}
