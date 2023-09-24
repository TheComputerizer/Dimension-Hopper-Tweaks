package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import com.latmod.mods.projectex.integration.PersonalEMC;
import com.latmod.mods.projectex.net.MessageSyncEMC;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;
import java.util.UUID;

@Mixin(value = PersonalEMC.class, remap = false)
public class MixinPersonalEMC {

    @Shadow @Final private static Object2LongOpenHashMap<UUID> EMC_MAP;

    /**
     * @author The_Computerizer
     * @reason Fix null capabilities
     */
    @Overwrite
    public static IKnowledgeProvider get(EntityPlayer player) {
        return player.getCapability(ProjectEAPI.KNOWLEDGE_CAPABILITY,null);
    }

    /**
     * @author The_Computerizer
     * @reason Fix null capabilities
     */
    @SubscribeEvent
    @Overwrite
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            long prev = EMC_MAP.getLong(event.player.getUniqueID());
            IKnowledgeProvider provider = get(event.player);
            if(Objects.nonNull(provider)) {
                long emc = provider.getEmc();
                if (prev == -1L || prev != emc) {
                    EMC_MAP.put(event.player.getUniqueID(), emc);
                    MessageSyncEMC.sync(event.player, emc);
                }
            }
        }
    }
}
