package mods.thecomputerizer.dimhoppertweaks.common.skills;

import mods.thecomputerizer.dimhoppertweaks.Constants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.mutable.MutableInt;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class SkillCapability implements ISkillCapability {

    private Map<String, SkillWrapper> skillMap = new HashMap<>();
    private String SKILL_TO_DRAIN = "mining";
    private int DRAIN_LEVELS = 1;
    private float SHIELD_DAMAGE = 1f;
    private BlockPos TWILIGHT_RESPAWN;
    private Map<Item, MutableInt> GATHERING_LIST = new HashMap<>();

    public SkillCapability() {
        Constants.LOGGER.info("Initializing skill capability with {} skills",SkillCapabilityStorage.SKILLS.size());
        for(String skill : SkillCapabilityStorage.SKILLS) this.addDefaultSkillValues(skill);
        setDrainSelection("mining",1, null);
        this.TWILIGHT_RESPAWN = null;
    }

    private void addDefaultSkillValues(String name) {
        this.setSkillXP(name,0,0);
    }

    private void checkForExistingSkill(String name) {
        if(!this.skillMap.containsKey(name)) {
            Constants.LOGGER.error("Could not find "+name+" skill! Substituting with a new level 1 "+name+" skill :)");
            String modid;
            if(name.matches("void") || name.matches("research")) modid = "compatskills";
            else modid = "reskillable";
            this.skillMap.put(name, new SkillWrapper(modid,name,0,100));
        }
    }

    @Override
    public void of(SkillCapability copy, EntityPlayerMP newPlayer) {
        this.skillMap = copy.skillMap;
        this.SKILL_TO_DRAIN = copy.SKILL_TO_DRAIN;
        this.DRAIN_LEVELS = copy.DRAIN_LEVELS;
        this.TWILIGHT_RESPAWN = copy.TWILIGHT_RESPAWN;
        this.GATHERING_LIST = copy.GATHERING_LIST;
        SkillWrapper.updateTokens(newPlayer);
    }

    @Override
    public void addSkillXP(String skill, int amount, EntityPlayerMP player, boolean fromXP) {
        if(amount>0) {
            checkForExistingSkill(skill);
            this.skillMap.get(skill).addXP(amount, player, fromXP);
            SkillWrapper.updateTokens(player);
        }
    }

    @Override
    public void setSkillXP(String skill, int xp, int level) {
        if(this.skillMap.containsKey(skill) && skillMap.get(skill).getLevel()!=0)
            Constants.LOGGER.error("Tried to register duplicate "+skill+" skill!");
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
    public void setPrestigeLevel(String skill, int level) {
        checkForExistingSkill(skill);
        this.skillMap.get(skill).setPrestigeLevel(level);
    }

    @Override
    public int getPrestigeLevel(String skill) {
        checkForExistingSkill(skill);
        return this.skillMap.get(skill).getPrestigeLevel();
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
    public void setShieldedDamage(float amount) {
        this.SHIELD_DAMAGE = amount;
    }

    @Override
    public float getShieldedDamage() {
        float damage = Math.max(0f,this.SHIELD_DAMAGE);
        this.SHIELD_DAMAGE = 1f;
        return damage;
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
    public void decrementGatheringItems(int amount) {
        this.GATHERING_LIST.entrySet().removeIf(entry -> entry.getValue().addAndGet(-1*amount)<=0);
    }

    @Override
    public boolean checkGatheringItem(Item item) {
        if(this.GATHERING_LIST.containsKey(item)) return false;
        this.GATHERING_LIST.put(item,new MutableInt(100));
        return true;
    }

    @Override
    public void syncGatheringItems(Map<Item, MutableInt> items) {
        this.GATHERING_LIST = items;
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
    public void setDrainSelection(String skill, int levels, EntityPlayerMP player) {
        this.SKILL_TO_DRAIN = skill;
        this.DRAIN_LEVELS = levels;
        if(player!=null) SkillWrapper.updateTokens(player);
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
    public void setTwilightRespawn(BlockPos pos) {
        this.TWILIGHT_RESPAWN = pos;
    }
    @Override
    public BlockPos getTwilightRespawn() {
        return this.TWILIGHT_RESPAWN;
    }

    @Override
    public NBTTagCompound writeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("skills_num",this.skillMap.size());
        int i = 0;
        for(Map.Entry<String, SkillWrapper> entry : this.skillMap.entrySet()) {
            compound.setString("skill_"+i,entry.getKey());
            compound = entry.getValue().writeNBT(compound);
            i++;
        }
        compound.setString("skill_to_drain",this.SKILL_TO_DRAIN);
        compound.setInteger("drain_levels",this.DRAIN_LEVELS);
        compound.setInteger("recent_gathering_num",this.GATHERING_LIST.size());
        i = 0;
        for(Map.Entry<Item, MutableInt> entry : this.GATHERING_LIST.entrySet()) {
            ResourceLocation resource = Objects.nonNull(entry.getKey().getRegistryName()) ?
                    entry.getKey().getRegistryName() : new ResourceLocation("bedrock");
            compound.setString("recent_gathering_resource_"+i,resource.toString());
            compound.setInteger("recent_gathering_counter_"+i,entry.getValue().getValue());
            i++;
        }
        if(this.TWILIGHT_RESPAWN!=null) compound.setLong("twilight_respawn",this.TWILIGHT_RESPAWN.toLong());
        return compound;
    }
}
