package mods.thecomputerizer.dimensionhoppertweaks.network.gui;

import io.netty.buffer.ByteBuf;
import mods.thecomputerizer.dimensionhoppertweaks.client.gui.TokenExchangeGui;
import net.minecraft.client.gui.GuiScreen;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TokenExchangeConstructor implements IGuiConstructor {

    private final List<String> skills;
    private final String currentSkill;
    private final int currentLevel;

    public TokenExchangeConstructor(List<String> skills, String currentSkill, int currentLevel) {
        this.skills = skills;
        this.currentSkill = currentSkill;
        this.currentLevel = currentLevel;
    }

    public TokenExchangeConstructor(ByteBuf buf) {
        this.skills = new ArrayList<>();
        int skillNums = buf.readInt();
        for(int i=0;i<skillNums;i++)
            this.skills.add((String) buf.readCharSequence(buf.readInt(), StandardCharsets.UTF_8));
        this.currentSkill = (String) buf.readCharSequence(buf.readInt(), StandardCharsets.UTF_8);
        this.currentLevel = buf.readInt();
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeInt(this.skills.size());
        for(String skill : this.skills) {
            buf.writeInt(skill.length());
            buf.writeCharSequence(skill, StandardCharsets.UTF_8);
        }
        buf.writeInt(this.currentSkill.length());
        buf.writeCharSequence(this.currentSkill, StandardCharsets.UTF_8);
        buf.writeInt(this.currentLevel);
    }

    @Override
    public GuiScreen construct() {
        return new TokenExchangeGui(this.skills,this.currentSkill,this.currentLevel);
    }
}
