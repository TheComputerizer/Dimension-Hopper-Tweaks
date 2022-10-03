package mods.thecomputerizer.dimensionhoppertweaks.common.skills;

import mods.thecomputerizer.dimensionhoppertweaks.DimensionHopperTweaks;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SkillCapability implements ISkillCapability {

    private int TICK_COUNTER = 0;

    private final Map<String, SkillWrapper> skillMap = new HashMap<>();
    private String SKILL_TO_DRAIN = "mining";
    private int DRAIN_LEVELS = 1;

    public SkillCapability() {
        for(String skill : SkillCapabilityStorage.SKILLS) this.addDefaultSkillValues(skill);
        setDrainSelection("mining",1);
    }

    private void addDefaultSkillValues(String name) {
        this.setSkillXP(name,0,0);
    }

    private void checkForExistingSkill(String name) {
        if(!this.skillMap.containsKey(name)) {
            DimensionHopperTweaks.LOGGER.error("Could not find "+name+" skill! Substituting with a new level 1 "+name+" skill :)");
            String modid;
            if(name.matches("void") || name.matches("research")) modid = "compatskills";
            else modid = "reskillable";
            this.skillMap.put(name, new SkillWrapper(modid,name,0,100));
        }
    }

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
    public void addSkillXP(String skill, int amount, EntityPlayerMP player) {
        checkForExistingSkill(skill);
        this.skillMap.get(skill).addXP(amount, player);
    }

    @Override
    public void setSkillXP(String skill, int xp, int level) {
        if(level!=0) DimensionHopperTweaks.LOGGER.info("Reading in skill {} at {}/{}",skill,xp,level*100);
        if(this.skillMap.containsKey(skill) && skillMap.get(skill).getLevel()!=0)
            DimensionHopperTweaks.LOGGER.error("Tried to register duplicate "+skill+" skill!");
        else {
            String modid;
            if(skill.matches("void") || skill.matches("research")) modid = "compatskills";
            else modid = "reskillable";
            this.skillMap.put(skill, new SkillWrapper(modid,skill,xp,level));
        }
    }

    @Override
    public int getSkillXP(String skill) {
        checkForExistingSkill(skill);
        return this.skillMap.get(skill).getXP();
    }

    @Override
    public int getSkillLevel(String skill) {
        checkForExistingSkill(skill);
        return this.skillMap.get(skill).getLevel();
    }

    @Override
    public int getSkillLevelXP(String skill) {
        checkForExistingSkill(skill);
        return this.skillMap.get(skill).getLevelXP();
    }

    @Override
    public float getBreakSpeedMultiplier() {
        checkForExistingSkill("mining");
        return 0.2f*(((float)this.skillMap.get("mining").getLevel())/32f);
    }

    @Override
    public float getDamageMultiplier() {
        checkForExistingSkill("attack");
        return 3f*(((float)this.skillMap.get("attack").getLevel())/32f);
    }

    @Override
    public float getDamageReduction() {
        checkForExistingSkill("defense");
        return 2f*(((float)this.skillMap.get("defense").getLevel())/32f);
    }

    @Override
    public float getXPDumpMultiplier() {
        checkForExistingSkill("magic");
        return Math.max(1f,2f*(((float)this.skillMap.get("magic").getLevel())/32f));
    }

    @Override
    public int getSkillXpMultiplier(float initialAmount) {
        checkForExistingSkill("research");
        return (int)(initialAmount*Math.max(1f,2f*(((float)this.skillMap.get("research").getLevel())/32f)));
    }

    @Override
    public Set<Map.Entry<String, SkillWrapper>> getCurrentValues() {
        return this.skillMap.entrySet();
    }

    @Override
    public void syncSkills(EntityPlayerMP player) {
        for(SkillWrapper wrapper : this.skillMap.values()) wrapper.syncLevel(player);
    }

    @Override
    public void setDrainSelection(String skill, int levels) {
        this.SKILL_TO_DRAIN = skill;
        this.DRAIN_LEVELS = levels;
    }

    @Override
    public String getDrainSelection() {
        return this.SKILL_TO_DRAIN;
    }

    @Override
    public int getDrainLevels() {
        return this.DRAIN_LEVELS;
    }

    @Override
    public NBTTagCompound writeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("skills_num",this.skillMap.keySet().size());
        int i = 0;
        for(Map.Entry<String, SkillWrapper> entry : this.skillMap.entrySet()) {
            compound.setString("skill_"+i,entry.getKey());
            compound = entry.getValue().writeNBT(compound);
            i++;
        }
        compound.setString("skill_to_drain",this.SKILL_TO_DRAIN);
        compound.setInteger("drain_levels",this.DRAIN_LEVELS);
        return compound;
    }
}
