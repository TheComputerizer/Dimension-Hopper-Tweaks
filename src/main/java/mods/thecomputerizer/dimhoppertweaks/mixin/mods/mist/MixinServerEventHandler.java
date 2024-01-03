package mods.thecomputerizer.dimhoppertweaks.mixin.mods.mist;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.spongepowered.asm.mixin.*;
import ru.liahim.mist.api.item.IMask;
import ru.liahim.mist.api.registry.MistRegistry;
import ru.liahim.mist.capability.FoodCapability;
import ru.liahim.mist.capability.MistCapability;
import ru.liahim.mist.capability.SkillCapability;
import ru.liahim.mist.capability.handler.IFoodHandler;
import ru.liahim.mist.capability.handler.IMistCapaHandler;
import ru.liahim.mist.capability.handler.ISkillCapaHandler;
import ru.liahim.mist.capability.handler.MistCapaHandler;
import ru.liahim.mist.common.Mist;
import ru.liahim.mist.common.MistTime;
import ru.liahim.mist.entity.ai.EntityAIEatMistGrass;
import ru.liahim.mist.handlers.ServerEventHandler;
import ru.liahim.mist.network.*;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
@Mixin(value = ServerEventHandler.class, remap = false)
public abstract class MixinServerEventHandler {

    @Shadow @Final private static HashMap<UUID, ItemStack> maskSync;

    @Shadow @Final private static HashMap<UUID, Integer> portDelay;

    @Shadow protected abstract boolean isAllPlayersAsleep(World world);

    @Shadow @Final private static HashMap<UUID, Integer> mulchDelay;

    @Unique private IMistCapaHandler dimhoppertweaks$getMistHandler(EntityPlayer player) {
        if(Objects.isNull(player) || Objects.isNull(MistCapability.CAPABILITY_MIST)) return null;
        IMistCapaHandler handler = player.getCapability(MistCapability.CAPABILITY_MIST,null);
        if(Objects.nonNull(handler)) handler.setPlayer(player);
        return handler;
    }

    @Unique private ISkillCapaHandler dimhoppertweaks$getSkillHandler(EntityPlayer player) {
        if(Objects.isNull(player) || Objects.isNull(SkillCapability.CAPABILITY_SKILL)) return null;
        ISkillCapaHandler handler = player.getCapability(SkillCapability.CAPABILITY_SKILL,null);
        if(Objects.nonNull(handler)) handler.setPlayer(player);
        return handler;
    }

    /**
     * @author The_Computerizer
     * @reason Fix null capability stuff in dev environment
     */
    @SubscribeEvent@Overwrite
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(event.phase == TickEvent.Phase.START && event.side == Side.SERVER) {
            EntityPlayer player = event.player;
            if(Objects.nonNull(player)) {
                World world = player.world;
                UUID uuid = player.getUniqueID();
                if(!portDelay.isEmpty() && portDelay.containsKey(uuid)) {
                    int i = portDelay.get(uuid);
                    if (i > 0) portDelay.replace(uuid, i - 1);
                    else {
                        if(player instanceof EntityPlayerMP)
                            ((EntityPlayerMP)player).clearInvulnerableDimensionChange();
                        portDelay.remove(uuid);
                    }
                }
                if(!maskSync.containsKey(uuid)) maskSync.put(uuid,ItemStack.EMPTY);
                IMistCapaHandler maskHandler = dimhoppertweaks$getMistHandler(player);
                if(Objects.nonNull(maskHandler)) {
                    ItemStack stack = maskHandler.getMask();
                    IMask mask = null;
                    if(!stack.isEmpty() && stack.getItem() instanceof IMask) {
                        mask = (IMask)stack.getItem();
                        mask.onWornTick(stack,player);
                    }
                    if(maskHandler.isMaskChanged() || Objects.nonNull(mask) && mask.willAutoSync(stack,player) &&
                            !ItemStack.areItemStacksEqual(stack,maskSync.get(uuid))) {
                        try {
                            if(maskHandler.isGlobalChanged())
                                PacketHandler.INSTANCE.sendToDimension(new PacketMaskSync(player,stack),
                                        player.world.provider.getDimension());
                            else if(player instanceof EntityPlayerMP)
                                PacketHandler.INSTANCE.sendTo(new PacketMaskSync(player,stack),(EntityPlayerMP)player);
                            maskHandler.setMaskChanged(false,false);
                        } catch (Exception ignored) {}
                        maskSync.put(uuid,stack);
                    }
                }
                if(player.isPlayerSleeping()) {
                    WorldServer otherWorld;
                    if(world.provider.getDimension()==0) {
                        if(player.isPlayerSleeping() && player.ticksExisted%90==0) {
                            otherWorld = DimensionManager.getWorld(Mist.getID());
                            if(Objects.nonNull(otherWorld) && !otherWorld.playerEntities.isEmpty() &&
                                    !this.isAllPlayersAsleep(otherWorld)) {
                                player.bedLocation = player.getPosition().down();
                                player.wakeUpPlayer(true, false, false);
                                player.trySleep(player.bedLocation);
                            }
                        }
                    } else if(world.provider.getDimension()==Mist.getID() && player.isPlayerSleeping() &&
                            player.ticksExisted%90==0) {
                        otherWorld = DimensionManager.getWorld(0);
                        if(Objects.nonNull(otherWorld) && !otherWorld.playerEntities.isEmpty() &&
                                !this.isAllPlayersAsleep(otherWorld)) {
                            player.bedLocation = player.getPosition().down();
                            player.wakeUpPlayer(true,false,false);
                            player.trySleep(player.bedLocation);
                        }
                    }
                }
                if (!mulchDelay.isEmpty() && mulchDelay.containsKey(uuid)) {
                    int i = mulchDelay.get(uuid);
                    if(i>0) mulchDelay.replace(uuid,i-1);
                    else mulchDelay.remove(uuid);
                }
            }
        }
    }

    /**
     * @author The_Computerizer
     * @reason Fix null capability stuff in dev environment
     */
    @SubscribeEvent
    @Overwrite
    public void joinEntity(EntityJoinWorldEvent event) {
        if(!event.getWorld().isRemote) {
            if(event.getEntity() instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP)event.getEntity();
                if(event.getWorld().provider.getDimension()==Mist.getID())
                    PacketHandler.INSTANCE.sendTo(new PacketSeedSync(event.getWorld().getSeed()), player);
                PacketHandler.INSTANCE.sendTo(new PacketTimeSync(MistTime.getDay(), MistTime.getMonth(), MistTime.getYear(), MistTime.getTimeOffset()), player);
                IMistCapaHandler maskHandler = dimhoppertweaks$getMistHandler(player);
                if(Objects.nonNull(maskHandler)) maskHandler.setMaskChanged(true, false);

                for(EntityPlayer player1 : player.getEntityWorld().playerEntities) {
                    if(player1.getEntityId() != player1.getEntityId()) {
                        IMistCapaHandler playerMasks = dimhoppertweaks$getMistHandler(player);
                        if(Objects.nonNull(playerMasks)) playerMasks.setMaskChanged(true, true);
                    }
                }
                maskSync.put(player.getUniqueID(), ItemStack.EMPTY);
                ISkillCapaHandler skillHandler = dimhoppertweaks$getSkillHandler(player);
                if(Objects.nonNull(skillHandler))
                    PacketHandler.INSTANCE.sendTo(new PacketSkillSync(skillHandler.getSkillsArray()), player);
                IFoodHandler foodHandler = Objects.nonNull(FoodCapability.CAPABILITY_FOOD) ? IFoodHandler.getHandler(player) : null;
                if(Objects.nonNull(foodHandler)) {
                    PacketHandler.INSTANCE.sendTo(new PacketMushroomSync(foodHandler.getMushroomList(false), false), player);
                    PacketHandler.INSTANCE.sendTo(new PacketMushroomSync(foodHandler.getMushroomList(true), true), player);
                    PacketHandler.INSTANCE.sendTo(new PacketToxicFoodSync(foodHandler.getFoodStudyList()), player);
                }
                if(Objects.nonNull(maskHandler)) {
                    PacketHandler.INSTANCE.sendTo(new PacketToxicSync(maskHandler.getPollution(),MistCapaHandler.HurtType.POLLUTION.getID()), player);
                    PacketHandler.INSTANCE.sendTo(new PacketToxicSync(maskHandler.getToxic(),MistCapaHandler.HurtType.TOXIC.getID()), player);
                }
            } else if(event.getEntity() instanceof EntityLiving) {
                if(event.getWorld().provider.getDimension() == Mist.getID()) {
                    ResourceLocation res = EntityList.getKey(event.getEntity());
                    if(Objects.nonNull(res) && MistRegistry.mobsDimsBlackList.contains(res.getNamespace()) || MistRegistry.mobsBlackList.contains(res))
                        event.setCanceled(true);
                }
                if(event.getEntity() instanceof EntitySheep) {
                    EntitySheep sheep = (EntitySheep)event.getEntity();
                    sheep.tasks.addTask(5, new EntityAIEatMistGrass(sheep, false));
                }
            }
        }
    }
}
