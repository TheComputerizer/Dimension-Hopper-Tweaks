package mods.thecomputerizer.dimhoppertweaks.registry;

import codersafterdark.reskillable.api.skill.Skill;
import mods.thecomputerizer.dimhoppertweaks.registry.skills.ResearchSkill;
import mods.thecomputerizer.dimhoppertweaks.registry.skills.VoidSkill;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public final class SkillRegistry {

    private static final List<Skill> ALL_SKILLS = new ArrayList<>();
    public static final Skill RESEARCH = makeSkill(new ResearchSkill());

    private static final Skill VOID = makeSkill(new VoidSkill());

    private static <S extends Skill> S makeSkill(S skill) {
        ALL_SKILLS.add(skill);
        return skill;
    }

    public static Skill[] getSkills() {
        return ALL_SKILLS.toArray(new Skill[0]);
    }
}
