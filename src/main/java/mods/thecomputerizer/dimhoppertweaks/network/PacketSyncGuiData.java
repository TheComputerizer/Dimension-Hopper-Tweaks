package mods.thecomputerizer.dimhoppertweaks.network;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.ISkillCapability;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import mods.thecomputerizer.theimpossiblelibrary.api.network.NetworkHelper;
import mods.thecomputerizer.theimpossiblelibrary.api.network.message.MessageAPI;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Objects;

public class PacketSyncGuiData extends MessageAPI<MessageContext> {

    private final String skill;
    private final int level;

    public PacketSyncGuiData(String skill, int level) {
        this.skill = skill;
        this.level = level;
    }
    
    public PacketSyncGuiData(ByteBuf buf) {
        this.skill = NetworkHelper.readString(buf);
        this.level = buf.readInt();
    }
    
    @Override public void encode(ByteBuf buf) {
        NetworkHelper.writeString(buf,this.skill);
        buf.writeInt(this.level);
    }

    @Override public MessageAPI<MessageContext> handle(MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().player;
        ISkillCapability cap = SkillWrapper.getSkillCapability(player);
        if(Objects.nonNull(cap)) cap.setDrainSelection(this.skill,this.level,player);
        return null;
    }
}