package mods.thecomputerizer.dimhoppertweaks.network;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.theimpossiblelibrary.network.MessageImpl;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketGrayScaleTimer extends MessageImpl {

    private float grayScale;

    public PacketGrayScaleTimer() {}

    public PacketGrayScaleTimer(float scale) {
        this.grayScale = MathHelper.clamp(1f-scale,0,1f);
    }

    @Override
    public IMessage handle(MessageContext messageContext) {
        ClientPacketHandlers.handleGrayScaleOverride(this.grayScale);
        return null;
    }

    @Override
    public Side getSide() {
        return Side.CLIENT;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.grayScale = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeFloat(this.grayScale);
    }
}
