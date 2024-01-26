package mods.thecomputerizer.dimhoppertweaks.network;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.ISkillCapability;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import mods.thecomputerizer.theimpossiblelibrary.network.MessageImpl;
import mods.thecomputerizer.theimpossiblelibrary.util.NetworkUtil;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Objects;

public class PacketSyncGuiData extends MessageImpl {

    private String skill;
    private int level;

    public PacketSyncGuiData() {}

    public PacketSyncGuiData(String skill, int level) {
        this.skill = skill;
        this.level = level;
    }

    @Override
    public IMessage handle(MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().player;
        ISkillCapability cap = SkillWrapper.getSkillCapability(player);
        if(Objects.nonNull(cap)) cap.setDrainSelection(this.skill,this.level,player);
        return null;
    }

    @Override
    public Side getSide() {
        return Side.SERVER;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.skill = NetworkUtil.readString(buf);
        this.level = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NetworkUtil.writeString(buf,this.skill);
        buf.writeInt(this.level);
    }
}
