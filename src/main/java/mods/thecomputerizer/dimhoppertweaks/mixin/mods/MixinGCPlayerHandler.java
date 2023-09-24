package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerHandler;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Objects;

@Mixin(value = GCPlayerHandler.class, remap = false)
public class MixinGCPlayerHandler {

    /**
     * @author The_Computerizer
     * @reason Fix null capability issues
     */
    @Overwrite
    private void onPlayerLogin(EntityPlayerMP player) {
        GCPlayerStats stats = GCPlayerStats.get(player);
        if(Objects.nonNull(stats)) {
            GalacticraftCore.packetPipeline.sendTo(new PacketSimple(PacketSimple.EnumSimplePacket.C_GET_CELESTIAL_BODY_LIST, GCCoreUtil.getDimensionID(player.world), new Object[]{}), player);
            int repeatCount = stats.getBuildFlags() >> 9;
            if (repeatCount < 3) stats.setBuildFlags(stats.getBuildFlags() & 1536);
            GalacticraftCore.packetPipeline.sendTo(new PacketSimple(PacketSimple.EnumSimplePacket.C_UPDATE_STATS, GCCoreUtil.getDimensionID(player.world), stats.getMiscNetworkedStats()), player);
            ColorUtil.sendUpdatedColorsToPlayer(stats);
        }
    }
}
