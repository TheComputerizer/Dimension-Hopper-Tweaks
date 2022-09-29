package mods.thecomputerizer.dimensionhoppertweaks.common.skills;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public interface ISkillCapability {

    boolean checkTick();

    void addMiningXP(int amount, EntityPlayerMP player);
    void setMiningXP(int[] args);
    float getBreakSpeedMultiplier();

    void addGatheringXP(int amount, EntityPlayerMP player);
    void setGatheringXP(int[] args);

    void addAttackXP(int amount, EntityPlayerMP player);
    void setAttackXP(int[] args);
    float getDamageMultiplier();

    void addDefenseXP(int amount, EntityPlayerMP player);
    void setDefenseXP(int[] args);
    float getDamageReduction();

    void addBuildingXP(int amount, EntityPlayerMP player);
    void setBuildingXP(int[] args);

    void addAgilityXP(int amount, EntityPlayerMP player);
    void setAgilityXP(int[] args);

    void addFarmingXP(int amount, EntityPlayerMP player);
    void setFarmingXP(int[] args);

    void addMagicXP(int amount, EntityPlayerMP player);
    void setMagicXP(int[] args);
    float getXPDumpMultiplier();

    void addVoidXP(int amount, EntityPlayerMP player);
    void setVoidXP(int[] args);

    void addResearchXP(int amount, EntityPlayerMP player);
    void setResearchXP(int[] args);
    int getSkillXpMultiplier(float initialAmount);

    NBTTagCompound writeNBT();
}
