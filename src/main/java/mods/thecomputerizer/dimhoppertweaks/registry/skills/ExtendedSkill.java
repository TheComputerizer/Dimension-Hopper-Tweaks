package mods.thecomputerizer.dimhoppertweaks.registry.skills;

import codersafterdark.reskillable.api.skill.Skill;
import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import net.minecraft.util.ResourceLocation;

public abstract class ExtendedSkill extends Skill {

    private static ResourceLocation getIcon(String name) {
        return DHTRef.res("textures/skills/"+name+".png");
    }

    private static ResourceLocation getBackground(String modid, String block) {
        return new ResourceLocation(modid,"textures/blocks/"+block+".png");
    }

    protected ExtendedSkill(String name, String backgroundModId, String backgroundBlock) {
        super(DHTRef.res(name),getBackground(backgroundModId,backgroundBlock));
        ResourceLocation iconRes = getIcon(name);
        for(int i=0; i<9; i++) setCustomSprite(i,iconRes);
    }
}
