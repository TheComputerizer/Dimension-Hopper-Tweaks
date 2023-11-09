package mods.thecomputerizer.dimhoppertweaks.common.skills;

import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import mods.thecomputerizer.dimhoppertweaks.network.PacketGrayScaleTimer;
import mods.thecomputerizer.dimhoppertweaks.network.PacketSyncBreakSpeed;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.commons.lang3.mutable.MutableInt;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

public class SkillCapability implements ISkillCapability {

    private final MutableInt dreamTimer = new MutableInt();
    private Map<String,SkillWrapper> skillMap = new HashMap<>();
    private Map<Item,MutableInt> gatheringCooldown = new HashMap<>();
    private Set<Item> autoFeedWhitelist = new HashSet<>();
    private String skillToDrain = "mining";
    private int drainLevels = 1;
    private float shieldDamage = 1f;
    private BlockPos twilightRespawn;

    public SkillCapability() {
        Constants.LOGGER.debug("Initializing skill capability");
    }

    private void checkForExistingSkill(String name) {
        if(!this.skillMap.containsKey(name)) {
            Constants.LOGGER.error("Could not find "+name+" skill! Substituting with a new level 1 "+name+" skill :)");
            this.skillMap.put(name,new SkillWrapper(name,0,100,0));
        }
    }

    @Override
    public void of(SkillCapability copy, EntityPlayerMP newPlayer) {
        this.skillMap = copy.skillMap;
        this.gatheringCooldown = copy.gatheringCooldown;
        this.autoFeedWhitelist = copy.autoFeedWhitelist;
        this.skillToDrain = copy.skillToDrain;
        this.drainLevels = copy.drainLevels;
        this.twilightRespawn = copy.twilightRespawn;
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
    public void syncBreakSpeed(EntityPlayerMP player) {
        float speed = 1f+getBreakSpeedMultiplier();
        if(Objects.nonNull(player.connection)) new PacketSyncBreakSpeed(speed).addPlayers(player).send();
    }

    @Override
    public float getDamageMultiplier() {
        checkForExistingSkill("attack");
        return 3f*(((float)this.skillMap.get("attack").getLevel())/32f);
    }

    @Override
    public void setShieldedDamage(float amount) {
        this.shieldDamage = amount;
    }

    @Override
    public float getShieldedDamage() {
        float damage = Math.max(0f,this.shieldDamage);
        this.shieldDamage = 1f;
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
        this.gatheringCooldown.entrySet().removeIf(entry -> entry.getValue().addAndGet(-1*amount)<=0);
    }

    @Override
    public boolean checkGatheringItem(Item item) {
        if(this.gatheringCooldown.containsKey(item)) return false;
        this.gatheringCooldown.put(item,new MutableInt(100));
        return true;
    }

    @Override
    public void togglePassiveFood(Item item) {
        if(item instanceof ItemFood) {
            if(this.autoFeedWhitelist.contains(item)) this.autoFeedWhitelist.remove(item);
            else this.autoFeedWhitelist.add(item);
        } else this.autoFeedWhitelist.remove(item);
    }

    @Override
    public boolean canAutoFeed(Item item) {
        return item instanceof ItemFood && this.autoFeedWhitelist.contains(item);
    }

    @Override
    public Set<Map.Entry<String, SkillWrapper>> getCurrentValues() {
        return this.skillMap.entrySet();
    }

    @Override
    public void syncSkills(EntityPlayerMP player) {
        for(SkillWrapper wrapper : this.skillMap.values()) wrapper.syncLevel(player);
        syncBreakSpeed(player);
    }

    @Override
    public void setDrainSelection(String skill, int levels, EntityPlayerMP player) {
        this.skillToDrain = skill;
        this.drainLevels = levels;
        if(Objects.nonNull(player)) SkillWrapper.updateTokens(player);
    }

    @Override
    public String getDrainSelection() {
        return this.skillToDrain;
    }

    @Override
    public int getDrainLevels() {
        return this.drainLevels;
    }

    @Override
    public void setTwilightRespawn(BlockPos pos) {
        this.twilightRespawn = pos;
    }
    @Override
    public BlockPos getTwilightRespawn() {
        return this.twilightRespawn;
    }

    @Override
    public boolean incrementDreamTimer(EntityPlayerMP player, int time) {
        int val = this.dreamTimer.addAndGet(time);
        if(val<0) resetDreamTimer();
        if(val%5==0) new PacketGrayScaleTimer(((float)val)/18000f).addPlayers(player).send();
        return val>=18000;
    }

    @Override
    public void resetDreamTimer() {
        this.dreamTimer.setValue(0);
    }

    @Override
    public NBTTagCompound writeToNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("skills",writeSkills());
        tag.setString("skillToDrain",this.skillToDrain);
        tag.setInteger("drainLevels",this.drainLevels);
        tag.setTag("gatheringTimers",writeGatheringList());
        tag.setTag("autoFeedWhitelist",writeCollection(this.autoFeedWhitelist,this::writeItemTag));
        if(Objects.nonNull(this.twilightRespawn)) tag.setLong("twilightRespawn",this.twilightRespawn.toLong());
        tag.setInteger("dreamTimer",this.dreamTimer.getValue());
        return tag;
    }

    private NBTTagList writeSkills() {
        NBTTagList tagList = new NBTTagList();
        for(Map.Entry<String,SkillWrapper> skillEntry : this.skillMap.entrySet())
            tagList.appendTag(writeSkill(skillEntry.getKey(),skillEntry.getValue()));
        return tagList;
    }

    private NBTTagCompound writeSkill(String name, SkillWrapper wrapper) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("skillName",name);
        tag.setInteger("skillXp",wrapper.getXP());
        tag.setInteger("skillLevel",wrapper.getLevel());
        tag.setInteger("skillPrestige",wrapper.getPrestigeLevel());
        return tag;
    }

    private NBTTagList writeGatheringList() {
        NBTTagList tagList = new NBTTagList();
        for(Map.Entry<Item,MutableInt> skillEntry : this.gatheringCooldown.entrySet())
            tagList.appendTag(writeGatheringItem(skillEntry.getKey(),skillEntry.getValue().getValue()));
        return tagList;
    }

    private NBTTagCompound writeGatheringItem(Item item, int time) {
        NBTTagCompound tag = new NBTTagCompound();
        String itemString = writeItemString(item);
        if(Objects.nonNull(itemString)) {
            tag.setString("item",itemString);
            tag.setInteger("cooldown",time);
        }
        return tag;
    }

    private @Nullable NBTTagString writeItemTag(Item item) {
        String itemString = writeItemString(item);
        return Objects.nonNull(itemString) ? new NBTTagString(itemString) : null;
    }

    private @Nullable String writeItemString(Item item) {
        ResourceLocation itemRes = item.getRegistryName();
        return Objects.nonNull(itemRes) ? itemRes.toString() : null;
    }

    private <E> NBTTagList writeCollection(Collection<E> collection, Function<E,NBTBase> toNBT) {
        NBTTagList tagList = new NBTTagList();
        for(E element : collection) {
            NBTBase base = toNBT.apply(element);
            if(Objects.nonNull(base)) tagList.appendTag(base);
        }
        return tagList;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        if(tag.hasKey("skills_num")) readOldSkills(tag);
        else readSkills(tag.getTag("skills"));
        readGatheringTimers(tag.getTag("gatheringTimers"));
        if(tag.hasKey("skillToDrain")) {
            this.skillToDrain = tag.getString("skillToDrain");
            this.drainLevels = tag.getInteger("drainLevels");
        }
        readCollection(tag.getTag("autoFeedWhitelist"),this.autoFeedWhitelist,this::readItem);
        if(tag.hasKey("twilightRespawn")) this.setTwilightRespawn(BlockPos.fromLong(tag.getLong("twilightRespawn")));
        this.dreamTimer.setValue(tag.getInteger("dreamTimer"));
    }

    private void readSkills(NBTBase tag) {
        if(!(tag instanceof NBTTagList)) return;
        NBTTagList tagList = (NBTTagList)tag;
        for(NBTBase elementTag : tagList) readSkill(elementTag);
    }

    private void readSkill(NBTBase tag) {
        if(!(tag instanceof NBTTagCompound)) return;
        NBTTagCompound skillTag = (NBTTagCompound)tag;
        String name = skillTag.getString("skillName");
        this.skillMap.put(name,new SkillWrapper(name,skillTag.getInteger("skillXp"),
                skillTag.getInteger("skillLevel"),skillTag.getInteger("skillPrestige")));
    }

    private void readGatheringTimers(NBTBase tag) {
        if(!(tag instanceof NBTTagList)) return;
        NBTTagList tagList = (NBTTagList)tag;
        for(NBTBase elementTag : tagList) readGatheringTimer(elementTag);
    }

    private void readGatheringTimer(NBTBase tag) {
        if(!(tag instanceof NBTTagCompound)) return;
        NBTTagCompound gatheringTag = (NBTTagCompound)tag;
        Item item = readItem(gatheringTag.getString("item"));
        if(Objects.nonNull(item))
            this.gatheringCooldown.put(item,new MutableInt(gatheringTag.getInteger("cooldown")));
    }

    private @Nullable Item readItem(NBTBase tag) {
        return tag instanceof NBTTagString ? readItem(((NBTTagString)tag).getString()) : null;
    }

    private @Nullable Item readItem(String itemString) {
        ResourceLocation itemRes = new ResourceLocation(itemString);
        return ForgeRegistries.ITEMS.containsKey(itemRes) ? ForgeRegistries.ITEMS.getValue(itemRes) : null;
    }

    private <E> void readCollection(NBTBase tag, Collection<E> collection, Function<NBTBase,E> toElement) {
        if(!(tag instanceof NBTTagList)) return;
        NBTTagList tagList = (NBTTagList)tag;
        for(NBTBase elementTag : tagList) {
            E element = toElement.apply(elementTag);
            if(Objects.nonNull(element)) collection.add(element);
        }
    }

    private void readOldSkills(NBTTagCompound tag) {
        for(int i=0; i<tag.getInteger("skills_num"); i++) {
            String name = tag.getString("skill_"+i);
            this.skillMap.put(name,new SkillWrapper(name,tag.getInteger(name+"_xp"),
                    tag.getInteger(name+"_level"),tag.getInteger(name+"_prestige")));
        }
    }
}
