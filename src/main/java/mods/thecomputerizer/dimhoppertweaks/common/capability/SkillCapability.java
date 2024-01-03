package mods.thecomputerizer.dimhoppertweaks.common.capability;

import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import mods.thecomputerizer.dimhoppertweaks.network.PacketGrayScaleTimer;
import mods.thecomputerizer.dimhoppertweaks.network.PacketSyncCapabilityData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
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
    private List<Tuple<Potion,Integer>> autoPotionWhitelist = new ArrayList<>();
    private String skillToDrain = "mining";
    private int drainLevels = 1;
    private float shieldDamage = 1f;
    private BlockPos twilightRespawn;
    private boolean pressedSkillKey = false;

    public SkillCapability() {
        DHTRef.LOGGER.debug("Initializing skill capability");
    }

    private void checkForExistingSkill(String name) {
        if(!this.skillMap.containsKey(name)) {
            DHTRef.LOGGER.error("Could not find "+name+" skill! Substituting with a new level 1 "+name+" skill :)");
            this.skillMap.put(name.toLowerCase(),new SkillWrapper(name,0,100,0));
        }
    }

    @Override
    public void of(SkillCapability copy, EntityPlayerMP newPlayer) {
        this.skillMap = copy.skillMap;
        this.gatheringCooldown = copy.gatheringCooldown;
        this.autoFeedWhitelist = copy.autoFeedWhitelist;
        this.autoPotionWhitelist = copy.autoPotionWhitelist;
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

    private void syncClientData(EntityPlayerMP player) {
        float breakSpeed = 1f+getBreakSpeedMultiplier();
        if(Objects.nonNull(player.connection))
            new PacketSyncCapabilityData(breakSpeed,this.pressedSkillKey,this.autoFeedWhitelist,this.autoPotionWhitelist)
                    .addPlayers(player).send();
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
    public void togglePassiveFood(EntityPlayerMP player, Item item) {
        if(item instanceof ItemFood) {
            if(this.autoFeedWhitelist.contains(item)) this.autoFeedWhitelist.remove(item);
            else this.autoFeedWhitelist.add(item);
            syncClientData(player);
        } else this.autoFeedWhitelist.remove(item);
    }

    @Override
    public boolean canAutoFeed(Item item) {
        return item instanceof ItemFood && this.autoFeedWhitelist.contains(item);
    }

    @Override
    public void togglePassivePotion(EntityPlayerMP player, ItemStack stack) {
        if(stack.getItem() instanceof ItemPotion) {
            for(PotionEffect effect : PotionUtils.getEffectsFromStack(stack)) {
                Potion potion = effect.getPotion();
                int amplifier = effect.getAmplifier();
                if(containsPotion(potion,amplifier)) removePotion(potion,amplifier);
                else this.autoPotionWhitelist.add(new Tuple<>(potion,amplifier));
            }
            syncClientData(player);
        }
    }

    private void removePotion(Potion potion, int amplifier) {
        this.autoPotionWhitelist.removeIf(validPotion -> potion == validPotion.getFirst() &&
                amplifier == validPotion.getSecond());
    }

    @Override
    public boolean canAutoDrink(EntityPlayerMP player, ItemStack stack) {
        if(stack.getItem() instanceof ItemPotion) {
            for(PotionEffect effect : PotionUtils.getEffectsFromStack(stack)) {
                Potion potion = effect.getPotion();
                if(containsPotion(potion,effect.getAmplifier())) {
                    PotionEffect playerEffect = player.getActivePotionEffect(effect.getPotion());
                    if(Objects.isNull(playerEffect) || playerEffect.getAmplifier()<effect.getAmplifier())
                        return true;
                }
            }
        }
        return false;
    }

    private boolean containsPotion(Potion potion, int amplifier) {
        for(Tuple<Potion,Integer> validPotion : this.autoPotionWhitelist)
            if(potion==validPotion.getFirst() && amplifier==validPotion.getSecond()) return true;
        return false;
    }

    @Override
    public Set<Map.Entry<String,SkillWrapper>> getCurrentValues() {
        return this.skillMap.entrySet();
    }

    @Override
    public void syncSkills(EntityPlayerMP player) {
        for(SkillWrapper wrapper : this.skillMap.values()) wrapper.syncLevel(player);
        syncClientData(player);
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
    public boolean pressedSkillKey() {
        return this.pressedSkillKey;
    }

    @Override
    public void markSkillKeyPressed() {
        this.pressedSkillKey = true;
    }

    @Override
    public NBTTagCompound writeToNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("skills",writeSkills());
        tag.setString("skillToDrain",this.skillToDrain);
        tag.setInteger("drainLevels",this.drainLevels);
        tag.setTag("gatheringTimers",writeGatheringList());
        tag.setTag("autoFeedWhitelist",writeCollection(this.autoFeedWhitelist,this::writeItemTag));
        tag.setTag("autoPotionWhitelist",writeCollection(this.autoPotionWhitelist,this::writePotionTag));
        if(Objects.nonNull(this.twilightRespawn)) tag.setLong("twilightRespawn",this.twilightRespawn.toLong());
        tag.setInteger("dreamTimer",this.dreamTimer.getValue());
        tag.setBoolean("pressedSkillKey",this.pressedSkillKey);
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

    private @Nullable NBTTagCompound writePotionTag(Tuple<Potion,Integer> potionTuple) {
        ResourceLocation potionRes = potionTuple.getFirst().getRegistryName();
        if(Objects.isNull(potionRes)) return null;
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("potionReg",potionRes.toString());
        tag.setInteger("amplifier",potionTuple.getSecond());
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
        readCollection(tag.getTag("autoPotionWhitelist"),this.autoPotionWhitelist,this::readPotionTuple);
        if(tag.hasKey("twilightRespawn")) this.setTwilightRespawn(BlockPos.fromLong(tag.getLong("twilightRespawn")));
        this.dreamTimer.setValue(tag.getInteger("dreamTimer"));
        this.pressedSkillKey = tag.getBoolean("pressedSkillKey");
    }

    private void readSkills(NBTBase tag) {
        if(!(tag instanceof NBTTagList)) return;
        NBTTagList tagList = (NBTTagList)tag;
        for(NBTBase elementTag : tagList) readSkill(elementTag);
    }

    private void readSkill(NBTBase tag) {
        if(!(tag instanceof NBTTagCompound)) return;
        NBTTagCompound skillTag = (NBTTagCompound)tag;
        String name = skillTag.getString("skillName").toLowerCase();
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

    private @Nullable Tuple<Potion,Integer> readPotionTuple(NBTBase base) {
        if(!(base instanceof NBTTagCompound)) return null;
        NBTTagCompound tag = (NBTTagCompound)base;
        String potionString = tag.getString("potionReg");
        if(potionString.isEmpty()) return null;
        ResourceLocation potionRes = new ResourceLocation(potionString);
        if(!ForgeRegistries.POTIONS.containsKey(potionRes)) return null;
        Potion potion = ForgeRegistries.POTIONS.getValue(potionRes);
        return Objects.nonNull(potion) ? new Tuple<>(potion,tag.getInteger("amplifier")) : null;
    }

    private @Nullable Item readItem(NBTBase tag) {
        return tag instanceof NBTTagString ? readItem(((NBTTagString)tag).getString()) : null;
    }

    private @Nullable Item readItem(String itemString) {
        if(itemString.isEmpty()) return null;
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
            String name = tag.getString("skill_"+i).toLowerCase();
            this.skillMap.put(name,new SkillWrapper(name,tag.getInteger(name+"_xp"),
                    tag.getInteger(name+"_level"),tag.getInteger(name+"_prestige")));
        }
    }
}
