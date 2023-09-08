package mods.thecomputerizer.dimhoppertweaks.network;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.theimpossiblelibrary.network.MessageImpl;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketSyncPlayerHealth extends MessageImpl {

    private double health;

    public PacketSyncPlayerHealth() {}

    public PacketSyncPlayerHealth(double health) {
        this.health = health;
    }


    @Override
    public IMessage handle(MessageContext messageContext) {
        ClientPacketHandlers.handleSyncPlayerHealth(this.health);
        return null;
    }

    @Override
    public Side getSide() {
        return Side.CLIENT;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.health = buf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(this.health);
    }
}
