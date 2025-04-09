package mods.thecomputerizer.dimhoppertweaks.network;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.dimhoppertweaks.client.gui.TokenExchangeGui;
import mods.thecomputerizer.theimpossiblelibrary.api.network.NetworkHelper;
import mods.thecomputerizer.theimpossiblelibrary.api.network.message.MessageAPI;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static net.minecraftforge.fml.relauncher.Side.CLIENT;

public class PacketOpenGui extends MessageAPI<MessageContext> {

    private final List<String> skills;
    private final String currentSkill;
    private final int currentLevel;

    public PacketOpenGui(List<String> skills, String skill, int level) {
        this.skills = skills;
        this.currentSkill = skill;
        this.currentLevel = level;
    }
    
    public PacketOpenGui(ByteBuf buf) {
        this.skills = NetworkHelper.readList(buf,() -> NetworkHelper.readString(buf));
        this.currentSkill = (String) buf.readCharSequence(buf.readInt(),UTF_8);
        this.currentLevel = buf.readInt();
    }
    
    @Override public void encode(ByteBuf buf) {
        NetworkHelper.writeList(buf,this.skills,s -> NetworkHelper.writeString(buf,s));
        NetworkHelper.writeString(buf,this.currentSkill);
        buf.writeInt(this.currentLevel);
    }

    @Override public MessageAPI<MessageContext> handle(MessageContext ctx) {
        openGui(this.skills,this.currentSkill,this.currentLevel);
        return null;
    }

    @SideOnly(CLIENT)
    private void openGui(List<String> skills, String skill, int level) {
        Minecraft.getMinecraft().displayGuiScreen(new TokenExchangeGui(skills,skill,level));
    }
}