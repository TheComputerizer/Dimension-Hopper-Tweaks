package mods.thecomputerizer.dimhoppertweaks.network;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.ISkillCapability;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import mods.thecomputerizer.theimpossiblelibrary.api.network.message.MessageAPI;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Objects;

public class PacketSendKeyPressed extends MessageAPI<MessageContext> {

    private final int keyType;

    public PacketSendKeyPressed(int keyType) {
        this.keyType = keyType;
    }
    
    public PacketSendKeyPressed(ByteBuf buf) {
        this.keyType = buf.readInt();
    }
    
    @Override public void encode(ByteBuf buf) {
        buf.writeInt(this.keyType);
    }
    
    @Override public MessageAPI<MessageContext> handle(MessageContext ctx) {
        ISkillCapability cap = SkillWrapper.getSkillCapability(ctx.getServerHandler().player);
        if(Objects.nonNull(cap)) {
            if(this.keyType==1) cap.markSkillKeyPressed();
            else if(this.keyType==2) cap.markResourcesKeyPressed();
        }
        return null;
    }
}