package mods.thecomputerizer.dimhoppertweaks.network;

import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import mods.thecomputerizer.dimhoppertweaks.network.packets.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class NetworkHandler {

    private static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(Constants.MODID);

    public static void registerPackets() {
        Constants.LOGGER.info("Registering packets");
        int id = 0;
        NETWORK.registerMessage(PacketUpdateBossRender.class, PacketUpdateBossRender.Message.class, id++, Side.CLIENT);
        NETWORK.registerMessage(PacketBossClientEffects.class, PacketBossClientEffects.Message.class, id++, Side.CLIENT);
        NETWORK.registerMessage(PacketRenderBossAttack.class, PacketRenderBossAttack.Message.class, id++, Side.CLIENT);
        NETWORK.registerMessage(PacketSyncPlayerHealth.class, PacketSyncPlayerHealth.Message.class, id++, Side.CLIENT);
        NETWORK.registerMessage(PacketOpenGui.class, PacketOpenGui.Message.class, id++, Side.CLIENT);
        NETWORK.registerMessage(PacketSyncGuiData.class, PacketSyncGuiData.Message.class, id, Side.SERVER);
    }

    public static void sendToTracking(IMessage message, @Nonnull Entity entity) {
        NETWORK.sendToAllTracking(message, entity);
    }

    public static void sendToPlayer(IMessage message, @Nonnull EntityPlayerMP player) {
        NETWORK.sendTo(message, player);
    }

    @SideOnly(Side.CLIENT)
    public static void sendToServer(IMessage message) {
        NETWORK.sendToServer(message);
    }
}
