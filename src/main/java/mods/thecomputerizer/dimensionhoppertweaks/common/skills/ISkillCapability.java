package mods.thecomputerizer.dimensionhoppertweaks.common.skills;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Map;
import java.util.Set;

public interface ISkillCapability {

    boolean checkTick();
    void addSkillXP(String skill, int amount, EntityPlayerMP player);
    void setSkillXP(String skill, int xp, int levelXP);
    int getSkillXP(String skill);
    int getSkillLevel(String skill);
    int getSkillLevelXP(String skill);
    float getBreakSpeedMultiplier();
    float getDamageMultiplier();
    float getDamageReduction();
    float getXPDumpMultiplier();
    int getSkillXpMultiplier(float initialAmount);
    Set<Map.Entry<String, SkillWrapper>> getCurrentValues();
    void syncSkills(EntityPlayerMP player);
    void setDrainSelection(String skill, int levels);
    String getDrainSelection();
    int getDrainLevels();
    NBTTagCompound writeNBT();
}
