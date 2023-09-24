package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import moze_intel.projecte.PECore;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.capabilities.IAlchBagProvider;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import moze_intel.projecte.config.ProjectEConfig;
import moze_intel.projecte.events.PlayerEvents;
import moze_intel.projecte.network.PacketHandler;
import moze_intel.projecte.network.packets.CheckUpdatePKT;
import moze_intel.projecte.network.packets.SyncCovalencePKT;
import moze_intel.projecte.utils.PlayerHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Objects;

@Mixin(value = PlayerEvents.class, remap = false)
public class MixinPlayerEvents {

    /**
     * @author The_Computerizer
     * @reason Fix null capabilities
     */
    @SubscribeEvent
    @Overwrite
    public static void playerConnect(PlayerEvent.PlayerLoggedInEvent event) {
        EntityPlayerMP player = (EntityPlayerMP)event.player;
        PacketHandler.sendFragmentedEmcPacket(player);
        PacketHandler.sendTo(new CheckUpdatePKT(), player);
        IKnowledgeProvider knowledge = player.getCapability(ProjectEAPI.KNOWLEDGE_CAPABILITY,null);
        if(Objects.nonNull(knowledge)) {
            knowledge.sync(player);
            PlayerHelper.updateScore(player,PlayerHelper.SCOREBOARD_EMC,MathHelper.floor((float)knowledge.getEmc()));
        }
        IAlchBagProvider bag = player.getCapability(ProjectEAPI.ALCH_BAG_CAPABILITY, null);
        if(Objects.nonNull(bag)) bag.sync(null,player);
        PacketHandler.sendTo(new SyncCovalencePKT(ProjectEConfig.difficulty.covalenceLoss,ProjectEConfig.difficulty.covalenceLossRounding),player);
        PECore.debugLog("Sent knowledge and bag data to {}",player.getName());
    }
}
