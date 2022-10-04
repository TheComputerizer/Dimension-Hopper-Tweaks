package mods.thecomputerizer.dimensionhoppertweaks.common.skills;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
public interface ISkillCapability {

    void of(SkillCapability copy, EntityPlayerMP newPlayer);
    boolean checkTick();
    void addSkillXP(String skill, int amount, EntityPlayerMP player, boolean fromXP);
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
    void setDrainSelection(String skill, int levels, EntityPlayerMP player);
    String getDrainSelection();
    int getDrainLevels();
    void setTwilightRespawn(BlockPos pos);
    BlockPos getTwilightRespawn();
    NBTTagCompound writeNBT();
}
