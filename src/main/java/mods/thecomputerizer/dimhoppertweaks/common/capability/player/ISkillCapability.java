package mods.thecomputerizer.dimhoppertweaks.common.capability.player;

import mods.thecomputerizer.dimhoppertweaks.common.capability.ICommonCapability;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
public interface ISkillCapability extends ICommonCapability {

    void of(SkillCapability copy, EntityPlayerMP newPlayer);
    void initWrappers();
    int addSP(String skill, int amount, EntityPlayerMP player, boolean fromXP);
    boolean isCapped(String skill, EntityPlayerMP player);
    int getSkillSP(String skill);
    int getSkillLevel(String skill);
    int getSkillLevelSP(String skill);
    void setPrestigeLevel(String skill, int level);
    int getPrestigeLevel(String skill);
    float getBreakSpeedMultiplier();
    void setShieldedDamage(float amount);
    float getShieldedDamage();
    float getXPFactor();
    int getActionFactor(float initialAmount);
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
    void markSkillKeyPressed();
    void markResourcesKeyPressed();
    int getFanUsage();
    void resetFanUsage();
}
