package mods.thecomputerizer.dimhoppertweaks.network.packets;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.dimhoppertweaks.client.gui.TokenExchangeGui;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PacketOpenGui implements IMessageHandler<PacketOpenGui.Message, IMessage> {

    @Override
    public IMessage onMessage(Message message, MessageContext ctx) {
        openGui(message.skills,message.currentSkill, message.currentLevel);
        return null;
    }

    @SideOnly(Side.CLIENT)
    private static void openGui(List<String> skills, String skill, int level) {
        Minecraft.getMinecraft().displayGuiScreen(new TokenExchangeGui(skills,skill,level));
    }

    public static class Message implements IMessage {

        private List<String> skills;
        private String currentSkill;
        private int currentLevel;

        public Message() {
        }

        public Message(List<String> skills, String skill, int level) {
            this.skills = skills;
            this.currentSkill = skill;
            this.currentLevel = level;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            this.skills = new ArrayList<>();
            int skillNums = buf.readInt();
            for(int i=0;i<skillNums;i++)
                this.skills.add((String) buf.readCharSequence(buf.readInt(), StandardCharsets.UTF_8));
            this.currentSkill = (String) buf.readCharSequence(buf.readInt(), StandardCharsets.UTF_8);
            this.currentLevel = buf.readInt();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(this.skills.size());
            for(String skill : this.skills) {
                buf.writeInt(skill.length());
                buf.writeCharSequence(skill, StandardCharsets.UTF_8);
            }
            buf.writeInt(this.currentSkill.length());
            buf.writeCharSequence(this.currentSkill, StandardCharsets.UTF_8);
            buf.writeInt(this.currentLevel);
        }
    }
}
