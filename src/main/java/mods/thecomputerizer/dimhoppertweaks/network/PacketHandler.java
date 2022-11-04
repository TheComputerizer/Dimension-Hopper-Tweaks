package mods.thecomputerizer.dimhoppertweaks.network;

import mods.thecomputerizer.dimhoppertweaks.DimHopperTweaks;
import mods.thecomputerizer.dimhoppertweaks.network.packets.PacketOpenGui;
import mods.thecomputerizer.dimhoppertweaks.network.packets.PacketRenderBossAttack;
import mods.thecomputerizer.dimhoppertweaks.network.packets.PacketSyncGuiData;
import mods.thecomputerizer.dimhoppertweaks.network.packets.PacketUpdateBossShield;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {

    public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(DimHopperTweaks.MODID);
    private static int id = 0;

    public static void registerPackets() {
        NETWORK.registerMessage(PacketUpdateBossShield.class, PacketUpdateBossShield.PacketUpdateBossShieldMessage.class, id++, Side.CLIENT);
        NETWORK.registerMessage(PacketRenderBossAttack.class, PacketRenderBossAttack.PacketRenderBossAttackMessage.class, id++, Side.CLIENT);
        NETWORK.registerMessage(PacketOpenGui.class, PacketOpenGui.PacketOpenGuiMessage.class, id++, Side.CLIENT);
        NETWORK.registerMessage(PacketSyncGuiData.class, PacketSyncGuiData.PacketSyncGuiDataMessage.class, id++, Side.SERVER);
    }
}
