package mods.thecomputerizer.dimhoppertweaks.network;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.ISkillCapability;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import mods.thecomputerizer.theimpossiblelibrary.network.MessageImpl;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Objects;

public class PacketSendKeyPressed extends MessageImpl {

    private int keyType;

    public PacketSendKeyPressed() {}

    public PacketSendKeyPressed(int keyType) {
        this.keyType = keyType;
    }
    @Override
    public IMessage handle(MessageContext ctx) {
        ISkillCapability cap = SkillWrapper.getSkillCapability(ctx.getServerHandler().player);
        if(Objects.nonNull(cap)) {
            if(this.keyType==1) cap.markSkillKeyPressed();
            else if(this.keyType==2) cap.markResourcesKeyPressed();
        }
        return null;
    }

    @Override
    public Side getSide() {
        return Side.SERVER;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.keyType = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.keyType);
    }
}
