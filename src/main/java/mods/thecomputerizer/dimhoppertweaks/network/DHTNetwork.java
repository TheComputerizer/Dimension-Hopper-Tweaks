package mods.thecomputerizer.dimhoppertweaks.network;

import mods.thecomputerizer.theimpossiblelibrary.api.network.NetworkHandler;
import mods.thecomputerizer.theimpossiblelibrary.api.network.NetworkHelper;
import mods.thecomputerizer.theimpossiblelibrary.api.network.message.MessageAPI;
import mods.thecomputerizer.theimpossiblelibrary.api.network.message.MessageWrapperAPI;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Collection;
import java.util.Objects;

import static mods.thecomputerizer.dimhoppertweaks.core.DHTRef.LOGGER;

public class DHTNetwork {
    
    public static void initClient() {
        NetworkHandler.registerMsgToServer(PacketAutoInfusion.class, PacketAutoInfusion::new);
        NetworkHandler.registerMsgToServer(PacketSendKeyPressed.class,PacketSendKeyPressed::new);
        NetworkHandler.registerMsgToServer(PacketSyncGuiData.class,PacketSyncGuiData::new);
    }
    
    public static void initCommon() {
        NetworkHandler.registerMsgToClient(PacketBossClientEffects.class,PacketBossClientEffects::new);
        NetworkHandler.registerMsgToClient(PacketGrayScaleTimer.class,PacketGrayScaleTimer::new);
        NetworkHandler.registerMsgToClient(PacketOpenGui.class,PacketOpenGui::new);
        NetworkHandler.registerMsgToClient(PacketQueryGenericClient.class,PacketQueryGenericClient::new);
        NetworkHandler.registerMsgToClient(PacketRenderBossAttack.class,PacketRenderBossAttack::new);
        NetworkHandler.registerMsgToClient(PacketSyncCapabilityData.class,PacketSyncCapabilityData::new);
        NetworkHandler.registerMsgToClient(PacketSyncExtraChunkData.class,PacketSyncExtraChunkData::new);
        NetworkHandler.registerMsgToClient(PacketSyncPlayerHealth.class,PacketSyncPlayerHealth::new);
        NetworkHandler.registerMsgToClient(PacketTileEntityClassQuery.class,PacketTileEntityClassQuery::new);
    }
    
    @SuppressWarnings("unchecked")
    public static void sendToClient(MessageAPI<?> msg, EntityPlayer ... players) {
        if(Objects.isNull(msg) || Objects.isNull(players) || players.length==0) return;
        MessageWrapperAPI<?,?> wrapper = NetworkHelper.wrapMessage(NetworkHelper.getDirToClient(),msg);
        if(Objects.isNull(wrapper)) {
            LOGGER.error("Failed to wrap message from server {}",msg);
            return;
        }
        ((MessageWrapperAPI<EntityPlayer,?>)wrapper).setPlayers(players);
        wrapper.send();
    }
    
    @SuppressWarnings("unchecked")
    public static void sendToClient(MessageAPI<?> msg, Collection<EntityPlayer> players) {
        if(Objects.isNull(msg) || Objects.isNull(players) || players.isEmpty()) return;
        MessageWrapperAPI<?,?> wrapper = NetworkHelper.wrapMessage(NetworkHelper.getDirToClient(),msg);
        if(Objects.isNull(wrapper)) {
            LOGGER.error("Failed to wrap message from server {}",msg);
            return;
        }
        ((MessageWrapperAPI<EntityPlayer,?>)wrapper).setPlayers(players);
        wrapper.send();
    }
    
    public static void sendToServer(MessageAPI<?> msg) {
        if(Objects.isNull(msg)) return;
        MessageWrapperAPI<?,?> wrapper = NetworkHelper.wrapMessage(NetworkHelper.getDirToServer(),msg);
        if(Objects.isNull(wrapper)) {
            LOGGER.error("Failed to wrap message from client {}",msg);
            return;
        }
        wrapper.send();
    }
}