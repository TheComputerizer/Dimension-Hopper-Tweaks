package mods.thecomputerizer.dimhoppertweaks.network;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.theimpossiblelibrary.network.MessageImpl;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketSyncBreakSpeed extends MessageImpl {

    private float miningSpeed;

    public PacketSyncBreakSpeed() {}

    public PacketSyncBreakSpeed(float miningSpeed) {
        this.miningSpeed = miningSpeed;
    }

    @Override
    public IMessage handle(MessageContext messageContext) {
        ClientPacketHandlers.handleMiningSpeed(this.miningSpeed);
        return null;
    }

    @Override
    public Side getSide() {
        return Side.CLIENT;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.miningSpeed = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeFloat(this.miningSpeed);
    }
}
