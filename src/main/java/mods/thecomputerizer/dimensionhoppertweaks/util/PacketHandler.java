package mods.thecomputerizer.dimensionhoppertweaks.util;

import mods.thecomputerizer.dimensionhoppertweaks.DimensionHopperTweaks;
import mods.thecomputerizer.dimensionhoppertweaks.util.packets.PacketRenderBossAttack;
import mods.thecomputerizer.dimensionhoppertweaks.util.packets.PacketUpdateBossShield;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {

    public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(DimensionHopperTweaks.MODID);
    private static int id = 0;

    public static void registerPackets() {
        NETWORK.registerMessage(PacketUpdateBossShield.class, PacketUpdateBossShield.PacketUpdateBossShieldMessage.class, id++, Side.CLIENT);
        NETWORK.registerMessage(PacketRenderBossAttack.class, PacketRenderBossAttack.PacketRenderBossAttackMessage.class, id++, Side.CLIENT);
    }
}
