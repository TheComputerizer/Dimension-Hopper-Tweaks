package mods.thecomputerizer.dimhoppertweaks.network;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.theimpossiblelibrary.api.network.message.MessageAPI;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketGrayScaleTimer extends MessageAPI<MessageContext> {

    private final float grayScale;

    public PacketGrayScaleTimer(float scale) {
        this.grayScale = MathHelper.clamp(1f-scale,0,1f);
    }
    
    public PacketGrayScaleTimer(ByteBuf buf) {
        this.grayScale = buf.readFloat();
    }
    
    @Override public void encode(ByteBuf buf) {
        buf.writeFloat(this.grayScale);
    }

    @Override public MessageAPI<MessageContext> handle(MessageContext ctx) {
        ClientPacketHandlers.handleGrayScaleOverride(this.grayScale);
        return null;
    }
}