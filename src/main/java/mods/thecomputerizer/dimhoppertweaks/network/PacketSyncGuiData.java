package mods.thecomputerizer.dimhoppertweaks.network;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.dimhoppertweaks.common.skills.SkillWrapper;
import mods.thecomputerizer.theimpossiblelibrary.network.MessageImpl;
import mods.thecomputerizer.theimpossiblelibrary.util.NetworkUtil;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerList;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class PacketSyncGuiData extends MessageImpl {

    private String skill;
    private int level;
    private UUID playerUUID;

    public PacketSyncGuiData() {}

    public PacketSyncGuiData(String skill, int level, UUID player) {
        this.skill = skill;
        this.level = level;
        this.playerUUID = player;
    }

    @Override
    public IMessage handle(MessageContext messageContext) {
        PlayerList players = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();
        EntityPlayerMP player = players.getPlayerByUUID(this.playerUUID);
        SkillWrapper.getSkillCapability(player).setDrainSelection(this.skill,this.level,player);
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
        this.playerUUID = UUID.fromString(NetworkUtil.readString(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NetworkUtil.writeString(buf,this.skill);
        buf.writeInt(this.level);
        NetworkUtil.writeString(buf,this.playerUUID.toString());
    }
}
