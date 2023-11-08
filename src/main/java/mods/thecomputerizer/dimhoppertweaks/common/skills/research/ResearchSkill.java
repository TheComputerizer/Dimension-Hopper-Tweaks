package mods.thecomputerizer.dimhoppertweaks.common.skills.research;

import codersafterdark.reskillable.api.skill.Skill;
import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import net.minecraft.util.ResourceLocation;

public class ResearchSkill extends Skill {

    private static final ResourceLocation BACKGROUND = new ResourceLocation("appliedenergistics2:textures/blocks/dense_energy_cell7.png");
    private static final ResourceLocation SKILL_ICON = Constants.res("textures/skills/research.png");

    public ResearchSkill() {
        super(Constants.res("research"),BACKGROUND);
        for(int i=0; i<9; i++) setCustomSprite(i,SKILL_ICON);
    }
}
