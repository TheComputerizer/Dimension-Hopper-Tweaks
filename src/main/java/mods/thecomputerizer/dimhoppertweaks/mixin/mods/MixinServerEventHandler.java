package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.asm.mixin.*;
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

@Mixin(value = ServerEventHandler.class, remap = false)
public class MixinServerEventHandler {

    @Shadow @Final private static HashMap<UUID, ItemStack> maskSync;

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
