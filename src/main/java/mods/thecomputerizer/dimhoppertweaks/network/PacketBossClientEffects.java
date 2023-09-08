package mods.thecomputerizer.dimhoppertweaks.network;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.theimpossiblelibrary.network.MessageImpl;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketBossClientEffects extends MessageImpl {

    private boolean setLimboSky;
    private float screenShake;

    public PacketBossClientEffects() {}

    public PacketBossClientEffects(boolean limboify, float screenShake) {
        this.setLimboSky = limboify;
        this.screenShake = screenShake;
    }

    @Override
    public IMessage handle(MessageContext messageContext) {
        ClientPacketHandlers.handleBossClientEffects();
        return null;
    }

    @Override
    public Side getSide() {
        return Side.CLIENT;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.setLimboSky = buf.readBoolean();
        this.screenShake = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(this.setLimboSky);
        buf.writeFloat(this.screenShake);
    }
}
