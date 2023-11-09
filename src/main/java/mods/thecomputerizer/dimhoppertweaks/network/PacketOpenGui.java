package mods.thecomputerizer.dimhoppertweaks.network;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.dimhoppertweaks.client.gui.TokenExchangeGui;
import mods.thecomputerizer.theimpossiblelibrary.network.MessageImpl;
import mods.thecomputerizer.theimpossiblelibrary.util.NetworkUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class PacketOpenGui extends MessageImpl {

    private List<String> skills;
    private String currentSkill;
    private int currentLevel;

    public PacketOpenGui() {}

    public PacketOpenGui(List<String> skills, String skill, int level) {
        this.skills = skills;
        this.currentSkill = skill;
        this.currentLevel = level;
    }

    @Override
    public IMessage handle(MessageContext ctx) {
        openGui(this.skills,this.currentSkill,this.currentLevel);
        return null;
    }

    @SideOnly(Side.CLIENT)
    private void openGui(List<String> skills, String skill, int level) {
        Minecraft.getMinecraft().displayGuiScreen(new TokenExchangeGui(skills,skill,level));
    }

    @Override
    public Side getSide() {
        return Side.CLIENT;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.skills = NetworkUtil.readGenericList(buf,NetworkUtil::readString);
        this.currentSkill = (String) buf.readCharSequence(buf.readInt(), StandardCharsets.UTF_8);
        this.currentLevel = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NetworkUtil.writeGenericList(buf,this.skills,NetworkUtil::writeString);
        NetworkUtil.writeString(buf,this.currentSkill);
        buf.writeInt(this.currentLevel);
    }
}
