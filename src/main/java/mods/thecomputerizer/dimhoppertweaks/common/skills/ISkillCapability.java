package mods.thecomputerizer.dimhoppertweaks.common.skills;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
public interface ISkillCapability {

    void of(SkillCapability copy, EntityPlayerMP newPlayer);
    void addSkillXP(String skill, int amount, EntityPlayerMP player, boolean fromXP);
    void setSkillXP(String skill, int xp, int levelXP);
    int getSkillXP(String skill);
    int getSkillLevel(String skill);
    int getSkillLevelXP(String skill);
    void setPrestigeLevel(String skill, int level);
    int getPrestigeLevel(String skill);
    float getBreakSpeedMultiplier();
    float getDamageMultiplier();
    float getDamageReduction();
    void setShieldedDamage(float amount);
    float getShieldedDamage();
    float getXPDumpMultiplier();
    int getSkillXpMultiplier(float initialAmount);
    void decrementGatheringItems(int amount);
    boolean checkGatheringItem(Item item);
    void syncGatheringItems(Map<Item, MutableInt> items);
    Set<Map.Entry<String, SkillWrapper>> getCurrentValues();
    void syncSkills(EntityPlayerMP player);
    void setDrainSelection(String skill, int levels, EntityPlayerMP player);
    String getDrainSelection();
    int getDrainLevels();
    void setTwilightRespawn(BlockPos pos);
    BlockPos getTwilightRespawn();
    NBTTagCompound writeNBT();
}
