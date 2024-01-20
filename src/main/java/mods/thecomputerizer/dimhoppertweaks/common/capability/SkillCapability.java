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
import thebetweenlands.common.herblore.elixir.effects.ElixirEffect;
import thebetweenlands.common.item.herblore.ItemElixir;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
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
    private boolean pressedResourcesKey = false;
    private int fanUsage = 0;

    public SkillCapability() {
        for(String skill : SkillWrapper.SKILLS)
            this.skillMap.put(skill,SkillWrapper.getNewInstance(skill,"Empty Skill"));
        DHTRef.LOGGER.debug("Initializing skill capability with skills {}",this.skillMap.keySet());
    }

    private SkillWrapper addWrapper(String skill, String initFailMsg) {
        SkillWrapper wrapper = SkillWrapper.getNewInstance(skill,initFailMsg);
        this.skillMap.put(skill,wrapper);
        return wrapper;
    }

    private void checkAndApply(String skill, Consumer<SkillWrapper> applier) {
        String name = skill.toLowerCase();
        SkillWrapper wrapper = this.skillMap.get(skill);
        if(Objects.isNull(wrapper)) {
            DHTRef.LOGGER.warn("Could not find skill with name `{}`! Substituting with a new level 1 skill :)",name);
            wrapper = addWrapper(name,"Missing Skill");
        }
        applier.accept(wrapper);
    }

    private <T> T checkAndReturn(String skill, Function<SkillWrapper,T> getter) {
        String name = skill.toLowerCase();
        SkillWrapper wrapper = this.skillMap.get(skill);
        if(Objects.isNull(wrapper)) {
            DHTRef.LOGGER.warn("Could not find skill with name `{}`! Substituting with a new level 1 skill :)",name);
            wrapper = addWrapper(name,"Missing Skill");
        }
        return getter.apply(wrapper);
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
    public void initWrappers() {
        for(String skill : SkillWrapper.SKILLS)
            if(!this.skillMap.containsKey(skill))
                addWrapper(skill,"New Instance");
    }

    /**
     * Returns the SP in regard to the input amount that was NOT added
     */
    @Override
    public int addSP(String skill, int amount, EntityPlayerMP player, boolean fromXP) {
        int ret = 0;
        if(amount>0) {
            ret = checkAndReturn(skill,wrapper -> wrapper.addSP(amount,player,fromXP));
            SkillWrapper.updateTokens(player);
        }
        return ret;
    }

    @Override
    public boolean isCapped(String skill, EntityPlayerMP player) {
        return checkAndReturn(skill,SkillWrapper::isCapped);
    }

    @Override
    public int getSkillSP(String skill) {
        return checkAndReturn(skill,SkillWrapper::getSP);
    }

    @Override
    public int getSkillLevel(String skill) {
        return checkAndReturn(skill,SkillWrapper::getLevel);
    }

    @Override
    public int getSkillLevelSP(String skill) {
        return checkAndReturn(skill,SkillWrapper::getLevelSP);
    }

    @Override
    public void setPrestigeLevel(String skill, int level) {
        checkAndApply(skill,wrapper -> wrapper.setPrestigeLevel(level));
    }

    @Override
    public int getPrestigeLevel(String skill) {
        return checkAndReturn(skill,SkillWrapper::getPrestigeLevel);
    }

    @Override
    public float getBreakSpeedMultiplier() {
        return checkAndReturn("mining",wrapper -> 0.2f*(((float)wrapper.getLevel())/32f));
    }

    private void syncClientData(EntityPlayerMP player) {
        float breakSpeed = 1f+getBreakSpeedMultiplier();
        if(Objects.nonNull(player.connection))
            new PacketSyncCapabilityData(breakSpeed,this.pressedSkillKey,this.pressedResourcesKey,
                    this.autoFeedWhitelist,this.autoPotionWhitelist).addPlayers(player).send();
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
    public float getXPFactor() {
        return checkAndReturn("magic",wrapper -> Math.max(1f,0.2f*(((float)wrapper.getLevel())/32f)));
    }

    @Override
    public int getActionFactor(float initialAmount) {
        return checkAndReturn("research",wrapper -> (int)(initialAmount*Math.max(1f,0.2f*(((float)wrapper.getLevel())/32f))));
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
    public void togglePassiveFood(EntityPlayerMP player, Item item, boolean isEnable) {
        if(item instanceof ItemFood) {
            if(isEnable) this.autoFeedWhitelist.add(item);
            else this.autoFeedWhitelist.remove(item);
            syncClientData(player);
        } else this.autoFeedWhitelist.remove(item);
    }

    @Override
    public boolean canAutoFeed(Item item) {
        return item instanceof ItemFood && this.autoFeedWhitelist.contains(item);
    }

    @Override
    public void togglePassivePotion(EntityPlayerMP player, ItemStack stack, boolean isEnable) {
        Item item = stack.getItem();
        if(item instanceof ItemPotion) {
            for(PotionEffect effect : PotionUtils.getEffectsFromStack(stack)) {
                Potion potion = effect.getPotion();
                int amplifier = effect.getAmplifier();
                if(containsPotion(potion,amplifier) && !isEnable) removePotion(potion,amplifier);
                else if(isEnable && effect.getDuration()>20 && !potion.isInstant())
                    this.autoPotionWhitelist.add(new Tuple<>(potion,amplifier));
            }
        } else if(item instanceof ItemElixir) {
            ItemElixir elixir = (ItemElixir)item;
            Potion potion = elixir.getElixirFromItem(stack).getPotionEffect();
            int amplifier = elixir.getElixirStrength(stack);
            int duration = elixir.getElixirDuration(stack);
            if(containsPotion(potion,amplifier) && !isEnable) removePotion(potion,amplifier);
            else if(isEnable && duration>20 && !potion.isInstant())
                this.autoPotionWhitelist.add(new Tuple<>(potion,amplifier));
        }
        syncClientData(player);
    }

    private void removePotion(Potion potion, int amplifier) {
        this.autoPotionWhitelist.removeIf(validPotion -> potion==validPotion.getFirst() &&
                amplifier==validPotion.getSecond());
    }

    @Override
    public boolean canAutoDrink(EntityPlayerMP player, ItemStack stack) {
        Item item = stack.getItem();
        if(item instanceof ItemPotion) {
            for(PotionEffect effect : PotionUtils.getEffectsFromStack(stack)) {
                Potion potion = effect.getPotion();
                if(containsPotion(potion,effect.getAmplifier()) && effect.getDuration()>20 && !potion.isInstant()) {
                    PotionEffect playerEffect = player.getActivePotionEffect(effect.getPotion());
                    if(Objects.isNull(playerEffect) || playerEffect.getAmplifier()<effect.getAmplifier())
                        return true;
                }
            }
        } else if(item instanceof ItemElixir) {
            ItemElixir elixir = (ItemElixir)item;
            ElixirEffect effect = elixir.getElixirFromItem(stack);
            Potion potion = effect.getPotionEffect();
            int amplifier = elixir.getElixirStrength(stack);
            int duration = elixir.getElixirDuration(stack);
            if(containsPotion(potion,amplifier) && duration>20 && !potion.isInstant()) {
                int strength = effect.getStrength(player);
                return strength>-1 && amplifier>strength;
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
    public void markSkillKeyPressed() {
        this.pressedSkillKey = true;
    }

    @Override
    public void markResourcesKeyPressed() {
        this.pressedResourcesKey = true;
    }

    @Override
    public int getFanUsage() {
        int ret = this.fanUsage;
        this.fanUsage++;
        return ret;
    }

    @Override
    public void resetFanUsage() {
        this.fanUsage = 0;
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
        tag.setBoolean("pressedResourcesKey",this.pressedResourcesKey);
        tag.setInteger("fanUsage",this.fanUsage);
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
        tag.setInteger("skillXp",wrapper.getSP());
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
        this.pressedResourcesKey = tag.getBoolean("pressedResourcesKey");
        this.fanUsage = tag.getInteger("fanUsage");
    }

    private void readSkills(NBTBase tag) {
        if(!(tag instanceof NBTTagList)) return;
        NBTTagList tagList = (NBTTagList)tag;
        for(NBTBase elementTag : tagList) readSkill(elementTag);
    }

    private void readSkill(NBTBase tag) {
        if(!(tag instanceof NBTTagCompound)) return;
        SkillWrapper wrapper = SkillWrapper.getTagInstance((NBTTagCompound)tag);
        if(Objects.nonNull(wrapper)) this.skillMap.put(wrapper.getName(),wrapper);
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
            SkillWrapper wrapper = SkillWrapper.getTagInstance(tag,i);
            if(Objects.nonNull(wrapper)) this.skillMap.put(wrapper.getName(),wrapper);
        }
    }
}
