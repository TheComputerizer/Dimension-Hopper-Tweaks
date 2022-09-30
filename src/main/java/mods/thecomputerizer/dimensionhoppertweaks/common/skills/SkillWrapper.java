package mods.thecomputerizer.dimensionhoppertweaks.common.skills;

import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerSkillInfo;
import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.api.toast.ToastHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class SkillWrapper {

    private final String modid;
    private final String name;
    private final int maxLevel;
    private int xp;
    private int level;
    private int levelXP;

    public SkillWrapper(String modid, String name, int xp, int level) {
        this.modid = modid;
        this.name = name;
        this.xp = xp;
        this.level = level;
        this.levelXP = level*100;
        Skill skill = getSkill();
        int cap = Integer.MAX_VALUE;
        if(skill!=null) cap = getSkill().getCap();
        this.maxLevel = cap;
    }

    public int getXP() {
        return this.xp;
    }

    public int getLevel() {
        return this.level;
    }

    public int getLevelXP() {
        return this.levelXP;
    }

    private boolean canLevelUp() {
        return this.xp>=this.levelXP && this.level!=0;
    }

    private boolean isMaxLevel() {
        return this.level>=this.maxLevel;
    }

    public void addXP(int amount, EntityPlayerMP player) {
        if(this.level<this.maxLevel && this.level!=0) {
            this.xp+=amount;
            if(canLevelUp()) levelUpWithOverflow(player);
        }
    }

    private void levelUpWithOverflow(EntityPlayerMP player) {
        while(canLevelUp()) {
            this.xp-=this.levelXP;
            this.level++;
            if(isMaxLevel()) {
                this.level = this.maxLevel;
                this.levelXP = 0;
                this.xp = 0;
                break;
            }
            this.levelXP = this.level*100;
        }
        PlayerData data = PlayerDataHandler.get(player);
        PlayerSkillInfo skillInfo = data.getSkillInfo(getSkill());
        skillInfo.setLevel(this.level);
        data.saveAndSync();
        ToastHelper.sendSkillToast(player, getSkill(), skillInfo.getLevel());
    }

    public void syncLevel(EntityPlayerMP player) {
        this.level = PlayerDataHandler.get(player).getSkillInfo(getSkill()).getLevel();
        if(isMaxLevel()) {
            this.level = this.maxLevel;
            this.levelXP = 0;
            this.xp = 0;
        } else this.levelXP = level * 100;
        if(this.xp>=this.levelXP) this.levelUpWithOverflow(player);
    }

    private Skill getSkill() {
        return ReskillableRegistries.SKILLS.getValue(new ResourceLocation(this.modid, this.name));
    }

    public NBTTagCompound writeNBT(NBTTagCompound compound) {
        compound.setInteger(this.name+"_xp",this.xp);
        compound.setInteger(this.name+"_level",this.level);
        return compound;
    }
}
