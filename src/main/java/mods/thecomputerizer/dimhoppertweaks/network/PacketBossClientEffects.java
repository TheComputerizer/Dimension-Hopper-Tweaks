package mods.thecomputerizer.dimhoppertweaks.network;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.theimpossiblelibrary.api.network.message.MessageAPI;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketBossClientEffects extends MessageAPI<MessageContext> {

    private final boolean setLimboSky;
    private final float screenShake;

    public PacketBossClientEffects(boolean limboify, float screenShake) {
        this.setLimboSky = limboify;
        this.screenShake = screenShake;
    }
    
    public PacketBossClientEffects(ByteBuf buf) {
        this.setLimboSky = buf.readBoolean();
        this.screenShake = buf.readFloat();
    }
    
    @Override public void encode(ByteBuf buf) {
        buf.writeBoolean(this.setLimboSky);
        buf.writeFloat(this.screenShake);
    }

    @Override public MessageAPI<MessageContext> handle(MessageContext ctx) {
        ClientPacketHandlers.handleBossClientEffects();
        return null;
    }
}
