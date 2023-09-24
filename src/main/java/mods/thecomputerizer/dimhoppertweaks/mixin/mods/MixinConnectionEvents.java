package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRace;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRaceManager;
import micdoodle8.mods.galacticraft.core.dimension.SpaceStationWorldData;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderSpaceStation;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.network.ConnectionEvents;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.core.world.ChunkLoadingCallback;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Objects;

@Mixin(value = ConnectionEvents.class, remap = false)
public class MixinConnectionEvents {

    /**
     * @author The_Computerizer
     * @reason Fix null capability issues
     */
    @SubscribeEvent
    @Overwrite
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        ChunkLoadingCallback.onPlayerLogin(event.player);
        if (event.player instanceof EntityPlayerMP) {
            EntityPlayerMP thePlayer = (EntityPlayerMP) event.player;
            GCPlayerStats stats = GCPlayerStats.get(thePlayer);
            if(Objects.nonNull(stats)) {
                SpaceStationWorldData.checkAllStations(thePlayer, stats);
                GalacticraftCore.packetPipeline.sendTo(new PacketSimple(PacketSimple.EnumSimplePacket.C_UPDATE_SPACESTATION_CLIENT_ID,
                        GCCoreUtil.getDimensionID(thePlayer.world), new Object[]
                        {WorldUtil.spaceStationDataToString(stats.getSpaceStationDimensionData())}), thePlayer);
            }
            SpaceRace raceForPlayer = SpaceRaceManager.getSpaceRaceFromPlayer(PlayerUtil.getName(thePlayer));
            if (Objects.nonNull(raceForPlayer))
                SpaceRaceManager.sendSpaceRaceData(thePlayer.server, thePlayer, raceForPlayer);
        }
        if (event.player.world.provider instanceof WorldProviderSpaceStation && event.player instanceof EntityPlayerMP)
            ((WorldProviderSpaceStation) event.player.world.provider).getSpinManager().sendPackets((EntityPlayerMP) event.player);
    }
}
