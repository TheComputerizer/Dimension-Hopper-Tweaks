package mods.thecomputerizer.dimhoppertweaks.common.capability;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
public interface ISkillCapability {

    void of(SkillCapability copy, EntityPlayerMP newPlayer);
    void addSkillXP(String skill, int amount, EntityPlayerMP player, boolean fromXP);
    int getSkillXP(String skill);
    int getSkillLevel(String skill);
    int getSkillLevelXP(String skill);
    void setPrestigeLevel(String skill, int level);
    int getPrestigeLevel(String skill);
    float getBreakSpeedMultiplier();
    void setShieldedDamage(float amount);
    float getShieldedDamage();
    float getXPDumpMultiplier();
    int getSkillXpMultiplier(float initialAmount);
    void decrementGatheringItems(int amount);
    boolean checkGatheringItem(Item item);
    void togglePassiveFood(EntityPlayerMP player, Item item, boolean isEnable);
    boolean canAutoFeed(Item item);
    void togglePassivePotion(EntityPlayerMP player, ItemStack stack, boolean isEnable);
    boolean canAutoDrink(EntityPlayerMP player, ItemStack stack);
    Set<Map.Entry<String, SkillWrapper>> getCurrentValues();
    void syncSkills(EntityPlayerMP player);
    void setDrainSelection(String skill, int levels, EntityPlayerMP player);
    String getDrainSelection();
    int getDrainLevels();
    void setTwilightRespawn(BlockPos pos);
    BlockPos getTwilightRespawn();
    boolean incrementDreamTimer(EntityPlayerMP player, int time);
    void resetDreamTimer();
    boolean pressedSkillKey();
    void markSkillKeyPressed();
    int getFanUsage();
    void resetFanUsage();
    NBTTagCompound writeToNBT();
    void readFromNBT(NBTTagCompound tag);
}
