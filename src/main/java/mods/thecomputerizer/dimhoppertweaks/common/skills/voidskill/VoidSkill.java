package mods.thecomputerizer.dimhoppertweaks.common.skills.voidskill;

import codersafterdark.reskillable.api.skill.Skill;
import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import net.minecraft.util.ResourceLocation;

public class VoidSkill extends Skill {

    private static final ResourceLocation BACKGROUND = new ResourceLocation("mekanism:textures/blocks/steelblock.png");
    private static final ResourceLocation SKILL_ICON = Constants.res("textures/skills/void.png");

    public VoidSkill() {
        super(Constants.res("void"),BACKGROUND);
        for(int i=0; i<9; i++) setCustomSprite(i,SKILL_ICON);
    }
}
