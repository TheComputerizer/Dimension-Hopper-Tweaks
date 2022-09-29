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

public class SkillCapability implements ISkillCapability {

    private int TICK_COUNTER = 0;

    private int MINING_XP;
    private int MINING_MAX;
    private int GATHERING_XP;
    private int GATHERING_MAX;
    private int ATTACK_XP;
    private int ATTACK_MAX;
    private int DEFENSE_XP;
    private int DEFENSE_MAX;
    private int BUILDING_XP;
    private int BUILDING_MAX;
    private int AGILITY_XP;
    private int AGILITY_MAX;
    private int FARMING_XP;
    private int FARMING_MAX;
    private int MAGIC_XP;
    private int MAGIC_MAX;
    private int VOID_XP;
    private int VOID_MAX;
    private int RESEARCH_XP;
    private int RESEARCH_MAX;

    @Override
    public boolean checkTick() {
        if(this.TICK_COUNTER<20) {
            this.TICK_COUNTER++;
            return false;
        }
        this.TICK_COUNTER = 0;
        return true;
    }

    @Override
    public void addMiningXP(int amount, EntityPlayerMP player) {
        this.MINING_XP+=amount;
        if(this.MINING_XP>this.MINING_MAX) {
            Skill skill = ReskillableRegistries.SKILLS.getValue(new ResourceLocation("reskillable", "mining"));
            if(skill!=null) {
                PlayerData data = PlayerDataHandler.get(player);
                PlayerSkillInfo skillInfo = data.getSkillInfo(skill);
                if (skillInfo.getLevel() < skill.getCap()) {
                    this.MINING_XP -= this.MINING_MAX;
                    this.MINING_MAX += 100;
                    skillInfo.setLevel(skillInfo.getLevel()+1);
                    data.saveAndSync();
                    ToastHelper.sendSkillToast(player, skill, skillInfo.getLevel());
                }
            }
        }
    }

    @Override
    public void setMiningXP(int[] args) {
        this.MINING_XP = args[0];
        this.MINING_MAX = args[1];
    }

    @Override
    public float getBreakSpeedMultiplier() {
        return 0.2f*(((float)this.MINING_MAX)/3200f);
    }

    @Override
    public void addGatheringXP(int amount, EntityPlayerMP player) {
        this.GATHERING_XP+=amount;
        if(this.GATHERING_XP>this.GATHERING_MAX) {
            Skill skill = ReskillableRegistries.SKILLS.getValue(new ResourceLocation("reskillable", "gathering"));
            if(skill!=null) {
                PlayerData data = PlayerDataHandler.get(player);
                PlayerSkillInfo skillInfo = data.getSkillInfo(skill);
                if (skillInfo.getLevel() < skill.getCap()) {
                    this.GATHERING_XP -= this.GATHERING_MAX;
                    this.GATHERING_MAX += 100;
                    skillInfo.setLevel(skillInfo.getLevel()+1);
                    data.saveAndSync();
                    ToastHelper.sendSkillToast(player, skill, skillInfo.getLevel());
                }
            }
        }
    }

    @Override
    public void setGatheringXP(int[] args) {
        this.GATHERING_XP = args[0];
        this.GATHERING_MAX = args[1];
    }

    @Override
    public void addAttackXP(int amount, EntityPlayerMP player) {
        this.ATTACK_XP+=amount;
        if(this.ATTACK_XP>this.ATTACK_MAX) {
            Skill skill = ReskillableRegistries.SKILLS.getValue(new ResourceLocation("reskillable", "attack"));
            if(skill!=null) {
                PlayerData data = PlayerDataHandler.get(player);
                PlayerSkillInfo skillInfo = data.getSkillInfo(skill);
                if (skillInfo.getLevel() < skill.getCap()) {
                    this.ATTACK_XP -= this.ATTACK_MAX;
                    this.ATTACK_MAX += 100;
                    skillInfo.setLevel(skillInfo.getLevel()+1);
                    data.saveAndSync();
                    ToastHelper.sendSkillToast(player, skill, skillInfo.getLevel());
                }
            }
        }
    }

    @Override
    public void setAttackXP(int[] args) {
        this.ATTACK_XP = args[0];
        this.ATTACK_MAX = args[1];
    }

    @Override
    public float getDamageMultiplier() {
        return 3f*(((float)this.ATTACK_MAX)/3200f);
    }

    @Override
    public void addDefenseXP(int amount, EntityPlayerMP player) {
        this.DEFENSE_XP+=amount;
        if(this.DEFENSE_XP>this.DEFENSE_MAX) {
            Skill skill = ReskillableRegistries.SKILLS.getValue(new ResourceLocation("reskillable", "defense"));
            if(skill!=null) {
                PlayerData data = PlayerDataHandler.get(player);
                PlayerSkillInfo skillInfo = data.getSkillInfo(skill);
                if (skillInfo.getLevel() < skill.getCap()) {
                    this.DEFENSE_XP -= this.DEFENSE_MAX;
                    this.DEFENSE_MAX += 100;
                    skillInfo.setLevel(skillInfo.getLevel()+1);
                    data.saveAndSync();
                    ToastHelper.sendSkillToast(player, skill, skillInfo.getLevel());
                }
            }
        }
    }

    @Override
    public void setDefenseXP(int[] args) {
        this.DEFENSE_XP = args[0];
        this.DEFENSE_MAX = args[1];
    }

    @Override
    public float getDamageReduction() {
        return 2f*(((float)this.DEFENSE_MAX)/3200f);
    }

    @Override
    public void addBuildingXP(int amount, EntityPlayerMP player) {
        this.BUILDING_XP+=amount;
        if(this.BUILDING_XP>this.BUILDING_MAX) {
            Skill skill = ReskillableRegistries.SKILLS.getValue(new ResourceLocation("reskillable", "building"));
            if(skill!=null) {
                PlayerData data = PlayerDataHandler.get(player);
                PlayerSkillInfo skillInfo = data.getSkillInfo(skill);
                if (skillInfo.getLevel() < skill.getCap()) {
                    this.BUILDING_XP -= this.BUILDING_MAX;
                    this.BUILDING_MAX += 100;
                    skillInfo.setLevel(skillInfo.getLevel()+1);
                    data.saveAndSync();
                    ToastHelper.sendSkillToast(player, skill, skillInfo.getLevel());
                }
            }
        }
    }

    @Override
    public void setBuildingXP(int[] args) {
        this.BUILDING_XP = args[0];
        this.BUILDING_MAX = args[1];
    }

    @Override
    public void addAgilityXP(int amount, EntityPlayerMP player) {
        this.AGILITY_XP+=amount;
        if(this.AGILITY_XP>this.AGILITY_MAX) {
            Skill skill = ReskillableRegistries.SKILLS.getValue(new ResourceLocation("reskillable", "agility"));
            if(skill!=null) {
                PlayerData data = PlayerDataHandler.get(player);
                PlayerSkillInfo skillInfo = data.getSkillInfo(skill);
                if (skillInfo.getLevel() < skill.getCap()) {
                    this.AGILITY_XP -= this.AGILITY_MAX;
                    this.AGILITY_MAX += 100;
                    skillInfo.setLevel(skillInfo.getLevel()+1);
                    data.saveAndSync();
                    ToastHelper.sendSkillToast(player, skill, skillInfo.getLevel());
                }
            }
        }
    }

    @Override
    public void setAgilityXP(int[] args) {
        this.AGILITY_XP = args[0];
        this.AGILITY_MAX = args[1];
    }

    @Override
    public void addFarmingXP(int amount, EntityPlayerMP player) {
        this.FARMING_XP+=amount;
        if(this.FARMING_XP>this.FARMING_MAX) {
            Skill skill = ReskillableRegistries.SKILLS.getValue(new ResourceLocation("reskillable", "farming"));
            if(skill!=null) {
                PlayerData data = PlayerDataHandler.get(player);
                PlayerSkillInfo skillInfo = data.getSkillInfo(skill);
                if (skillInfo.getLevel() < skill.getCap()) {
                    this.FARMING_XP -= this.FARMING_MAX;
                    this.FARMING_MAX += 100;
                    skillInfo.setLevel(skillInfo.getLevel()+1);
                    data.saveAndSync();
                    ToastHelper.sendSkillToast(player, skill, skillInfo.getLevel());
                }
            }
        }
    }

    @Override
    public void setFarmingXP(int[] args) {
        this.FARMING_XP = args[0];
        this.FARMING_MAX = args[1];
    }

    @Override
    public void addMagicXP(int amount, EntityPlayerMP player) {
        this.MAGIC_XP+=amount;
        if(this.MAGIC_XP>this.MAGIC_MAX) {
            Skill skill = ReskillableRegistries.SKILLS.getValue(new ResourceLocation("reskillable", "magic"));
            if(skill!=null) {
                PlayerData data = PlayerDataHandler.get(player);
                PlayerSkillInfo skillInfo = data.getSkillInfo(skill);
                if (skillInfo.getLevel() < skill.getCap()) {
                    this.MAGIC_XP -= this.MAGIC_MAX;
                    this.MAGIC_MAX += 100;
                    skillInfo.setLevel(skillInfo.getLevel()+1);
                    data.saveAndSync();
                    ToastHelper.sendSkillToast(player, skill, skillInfo.getLevel());
                }
            }
        }
    }

    @Override
    public void setMagicXP(int[] args) {
        this.MAGIC_XP = args[0];
        this.MAGIC_MAX = args[1];
    }

    @Override
    public float getXPDumpMultiplier() {
        return 2f*(((float)this.MAGIC_MAX)/3200f);
    }

    @Override
    public void addVoidXP(int amount, EntityPlayerMP player) {
        this.VOID_XP+=amount;
        if(this.VOID_XP>this.VOID_MAX) {
            Skill skill = ReskillableRegistries.SKILLS.getValue(new ResourceLocation("compatskills", "void"));
            if(skill!=null) {
                PlayerData data = PlayerDataHandler.get(player);
                PlayerSkillInfo skillInfo = data.getSkillInfo(skill);
                if (skillInfo.getLevel() < skill.getCap()) {
                    this.VOID_XP -= this.VOID_MAX;
                    this.VOID_MAX += 100;
                    skillInfo.setLevel(skillInfo.getLevel()+1);
                    data.saveAndSync();
                    ToastHelper.sendSkillToast(player, skill, skillInfo.getLevel());
                }
            }
        }
    }

    @Override
    public void setVoidXP(int[] args) {
        this.VOID_XP = args[0];
        this.VOID_MAX = args[1];
    }

    @Override
    public void addResearchXP(int amount, EntityPlayerMP player) {
        this.RESEARCH_XP+=amount;
        if(this.RESEARCH_XP>this.RESEARCH_MAX) {
            Skill skill = ReskillableRegistries.SKILLS.getValue(new ResourceLocation("compatskills", "research"));
            if(skill!=null) {
                PlayerData data = PlayerDataHandler.get(player);
                PlayerSkillInfo skillInfo = data.getSkillInfo(skill);
                if (skillInfo.getLevel() < skill.getCap()) {
                    this.RESEARCH_XP -= this.RESEARCH_MAX;
                    this.RESEARCH_MAX += 100;
                    skillInfo.setLevel(skillInfo.getLevel()+1);
                    data.saveAndSync();
                    ToastHelper.sendSkillToast(player, skill, skillInfo.getLevel());
                }
            }
        }
    }

    @Override
    public void setResearchXP(int[] args) {
        this.RESEARCH_XP = args[0];
        this.RESEARCH_MAX = args[1];
    }

    @Override
    public int getSkillXpMultiplier(float initialAmount) {
        return (int)Math.floor(initialAmount*(1f+2f*(((float)this.MAGIC_MAX)/3200f)));
    }
    
    public int[] getCurrentValues() {
        int[] ret = new int[20];
        ret[0] = this.MINING_XP;
        ret[10] = this.MINING_MAX;
        ret[1] = this.GATHERING_XP;
        ret[11] = this.GATHERING_MAX;
        ret[2] = this.ATTACK_XP;
        ret[12] = this.ATTACK_MAX;
        ret[3] = this.DEFENSE_XP;
        ret[13] = this.DEFENSE_MAX;
        ret[4] = this.BUILDING_XP;
        ret[14] = this.BUILDING_MAX;
        ret[5] = this.AGILITY_XP;
        ret[15] = this.AGILITY_MAX;
        ret[6] = this.FARMING_XP;
        ret[16] = this.FARMING_MAX;
        ret[7] = this.MAGIC_XP;
        ret[17] = this.MAGIC_MAX;
        ret[8] = this.VOID_XP;
        ret[18] = this.VOID_MAX;
        ret[9] = this.RESEARCH_XP;
        ret[19] = this.RESEARCH_MAX;
        return ret;
    }

    @Override
    public NBTTagCompound writeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("mining_xp",this.MINING_XP);
        compound.setInteger("gathering_xp",this.GATHERING_XP);
        compound.setInteger("attack_xp",this.ATTACK_XP);
        compound.setInteger("defense_xp",this.DEFENSE_XP);
        compound.setInteger("building_xp",this.BUILDING_XP);
        compound.setInteger("agility_xp",this.AGILITY_XP);
        compound.setInteger("farming_xp",this.FARMING_XP);
        compound.setInteger("magic_xp",this.MAGIC_XP);
        compound.setInteger("void_xp",this.VOID_XP);
        compound.setInteger("research_xp",this.RESEARCH_XP);
        return compound;
    }
}
