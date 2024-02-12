package mods.thecomputerizer.dimhoppertweaks.common.capability.player;

import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerSkillInfo;
import codersafterdark.reskillable.api.event.LevelUpEvent;
import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.api.toast.ToastHelper;
import codersafterdark.reskillable.api.unlockable.IAbilityEventHandler;
import codersafterdark.reskillable.api.unlockable.Unlockable;
import com.google.common.collect.ImmutableList;
import mods.thecomputerizer.dimhoppertweaks.common.capability.CommonCapability;
import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import mods.thecomputerizer.dimhoppertweaks.registry.ItemRegistry;
import mods.thecomputerizer.dimhoppertweaks.registry.SoundRegistry;
import mods.thecomputerizer.dimhoppertweaks.registry.TraitRegistry;
import mods.thecomputerizer.dimhoppertweaks.registry.items.SkillToken;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("ConstantValue")
@ParametersAreNonnullByDefault
public class SkillWrapper {

    public static final ResourceLocation SKILL_CAPABILITY = DHTRef.res("skills");
    public static final ImmutableList<String> SKILLS = makeOrderedSkillList();

    private static ImmutableList<String> makeOrderedSkillList() {
        List<String> skills = Arrays.asList("agility","attack","building","defense","farming","gathering","magic",
                "mining","research","void");
        Collections.sort(skills);
        return ImmutableList.copyOf(skills);
    }

    public static void addActionSP(EntityPlayerMP player, String skill, float amount) {
        executeIfPresent(player,cap -> player.addExperience(cap.addSP(skill,(int)amount,player,false)));
    }

    private static void executeIfPresent(EntityPlayer player,Consumer<ISkillCapability> exectuor) {
        CommonCapability.executeIfPresent(player,SkillCapabilityProvider.SKILL_CAPABILITY,exectuor);
    }

    public static void executeOnSkill(PlayerData data, @Nullable Skill skill, Consumer<IAbilityEventHandler> consumer) {
        if(Objects.nonNull(skill)) data.getSkillInfo(skill).forEachEventHandler(consumer);
    }

    public static void executeOnSkills(PlayerData data, Consumer<IAbilityEventHandler> consumer) {
        executeOnSkill(data,getSkill("mining"),consumer);
        executeOnSkill(data,getSkill("gathering"),consumer);
        executeOnSkill(data,getSkill("attack"),consumer);
        executeOnSkill(data,getSkill("defense"),consumer);
        executeOnSkill(data,getSkill("building"),consumer);
        executeOnSkill(data,getSkill("agility"),consumer);
        executeOnSkill(data,getSkill("farming"),consumer);
        executeOnSkill(data,getSkill("magic"),consumer);
        executeOnSkill(data,getSkill("void"),consumer);
        executeOnSkill(data,getSkill("research"),consumer);
    }

    public static void forceTwilightRespawn(EntityPlayer player) {
        executeIfPresent(player,cap -> {
            BlockPos pos = cap.getTwilightRespawn();
            if(player.dimension==7 && Objects.isNull(player.getBedLocation(7)) && Objects.nonNull(pos))
                player.setSpawnPoint(pos,true);
        });
        ISkillCapability cap = getSkillCapability(player);
        if(Objects.isNull(cap)) return;
        BlockPos pos = cap.getTwilightRespawn();
        if(player.dimension==7 && Objects.isNull(player.getBedLocation(7)) && Objects.nonNull(pos))
            player.setSpawnPoint(pos,true);
    }

    public static int getFanUsage(EntityPlayer player) {
        ISkillCapability cap = getSkillCapability(player);
        return Objects.nonNull(cap) ? cap.getFanUsage() : 0;
    }

    public static @Nullable SkillWrapper getInstance(String name, int sp, int level, int prestige, String initFailMsg) {
        name = name.toLowerCase();
        Skill skill = getSkill(name);
        if(Objects.isNull(skill)) {
            DHTRef.LOGGER.error("Failed to initialize unknown skill from `{}`",initFailMsg);
            return null;
        }
        return new SkillWrapper(name,skill).initialize(sp,level,prestige);
    }

    public static @Nullable SkillWrapper getNewInstance(String name, String initFailMsg) {
        return getInstance(name,0,1,0,initFailMsg);
    }

    public static <T> T getOrDefault(EntityPlayerMP player, Function<ISkillCapability,T> getter, T defVal) {
        return CommonCapability.getOrDefault(player,SkillCapabilityProvider.SKILL_CAPABILITY,getter,defVal);
    }

    public static @Nullable SkillWrapper getTagInstance(NBTTagCompound skillTag) {
        String name = skillTag.getString("skillName");
        int sp = skillTag.getInteger("skillXp");
        int level = skillTag.getInteger("skillLevel");
        int prestige = skillTag.getInteger("skillPrestige");
        return getInstance(name,sp,level,prestige,skillTag.toString());
    }

    /**
     * Old storage method for skill data
     */
    public static @Nullable SkillWrapper getTagInstance(NBTTagCompound oldSkillTag, int index) {
        String name = oldSkillTag.getString("skill_"+index);
        int sp = oldSkillTag.getInteger(name+"_xp");
        int level = oldSkillTag.getInteger(name+"_level");
        int prestige = oldSkillTag.getInteger(name+"_prestige");
        return getInstance(name,sp,level,prestige,oldSkillTag.toString());
    }

    public static @Nullable Skill getSkill(String name) {
        if(StringUtils.isBlank(name) || !isValidSkill(name)) return null;
        ResourceLocation skillRes =  name.matches("research") || name.matches("void") ? DHTRef.res(name) :
                new ResourceLocation("reskillable",name);
        return ReskillableRegistries.SKILLS.containsKey(skillRes) ? ReskillableRegistries.SKILLS.getValue(skillRes) : null;
    }

    public static @Nullable ISkillCapability getSkillCapability(EntityPlayer player) {
        return CommonCapability.getCapability(player,SkillCapabilityProvider.SKILL_CAPABILITY);
    }

    public static double getPrestigeFactor(EntityPlayer player, String skill) {
        return getOrDefault((EntityPlayerMP)player,cap -> 1d+(((double)cap.getSkillLevel(skill))/32d),1d);
    }

    public static boolean hasTrait(@Nullable PlayerData data, String skillName, Unlockable trait) {
        Skill skill = getSkill(skillName);
        return Objects.nonNull(data) && Objects.nonNull(skill) && data.getSkillInfo(skill).isUnlocked(trait);
    }

    public static boolean isValidSkill(String skill) {
        return SKILLS.contains(skill);
    }

    public static boolean makesChunksFast(EntityPlayer player) {
        return hasTrait(PlayerDataHandler.get(player),"magic",TraitRegistry.NATURES_AURA);
    }

    public static void onPlayerJoin(EntityPlayerMP player) {
        executeIfPresent(player,cap -> {
            cap.initWrappers();
            BlockPos pos = cap.getTwilightRespawn();
            if(player.dimension==7 && Objects.isNull(player.getBedLocation(7)) && Objects.nonNull(pos))
                player.setSpawnPoint(pos,true);
            updateTokens(player);
        });
    }

    public static void resetFanUsage(Entity entity) {
        if(entity instanceof EntityPlayer) executeIfPresent((EntityPlayer)entity,ISkillCapability::resetFanUsage);
    }

    public static void shieldHook(EntityPlayerMP player, byte state) {
        executeIfPresent(player,cap -> {
            float amount = cap.getShieldedDamage();
            if(state==29 && amount>0) addActionSP(player,"defense",Math.max(1f,amount/2f));
        });
    }

    public static boolean ticKDreamer(EntityPlayerMP player, int ticks) {
        return getOrDefault(player,cap -> cap.incrementDreamTimer(player,ticks),false);
    }

    public static void updateTokens(EntityPlayerMP player) {
        executeIfPresent(player,cap -> {
            cap.syncSkills(player);
            for(int i=0; i<player.inventory.getSizeInventory(); i++) {
                ItemStack stack = player.inventory.getStackInSlot(i);
                if(stack.getItem() instanceof SkillToken) {
                    SkillToken token = (SkillToken) stack.getItem();
                    token.updateSkills(stack,cap.getCurrentValues(),cap.getDrainSelection(),cap.getDrainLevels());
                }
            }
        });
    }

    private final String name;
    private final Skill skill;
    private final int maxLevel;
    private final double levelFactor;
    private int sp;
    private int level;
    private int levelSP;
    private int prestigeLevel;

    private SkillWrapper(String name, Skill skill) {
        this.name = name;
        this.skill = skill;
        this.maxLevel = skill.getCap();
        this.levelFactor = getLevelFactor();
    }

    public SkillWrapper initialize(int sp, int level, int prestigeLevel) {
        this.sp = sp;
        this.level = level;
        calculateLevelSP();
        this.prestigeLevel = prestigeLevel;
        DHTRef.LOGGER.debug("Initialized skill {} at level {} with xp {}/{}",this.skill.getRegistryName(),level,
                sp,this.levelSP);
        return this;
    }

    private double getLevelFactor() {
        ResourceLocation res = this.skill.getRegistryName();
        if(Objects.isNull(res)) return 0d;
        switch(res.getPath()) {
            case "gathering":
            case "magic":
                return 3d;
            case "attack":
            case "mining":
            case "building":
                return 1.5d;
            case "agility": return 7.5d;
            case "void": return 0.75d;
            case "defense":
            case "farming":
            case "research": return 1.1d;
            default: return 1d;
        }
    }

    public int getSP() {
        return this.sp;
    }

    public int getLevel() {
        return this.level;
    }

    public int getLevelSP() {
        return this.levelSP;
    }

    public int getMaxLevel() {
        return Math.min(this.maxLevel,(this.prestigeLevel+1)*32);
    }

    public String getName() {
        return this.name;
    }

    public int getPrestigeLevel() {
        return this.prestigeLevel;
    }

    public void setPrestigeLevel(int level) {
        this.prestigeLevel = level;
    }

    public Skill getSkill() {
        return this.skill;
    }

    private boolean canLevelUp() {
        if(isCapped()) {
            this.sp = 0;
            return false;
        }
        return this.sp>=this.levelSP;
    }

    public boolean isCapped() {
        return this.level<=0 || this.level>=getMaxLevel();
    }

    private int getPrestigeFactor(EntityPlayerMP player, int amount, boolean fromXP) {
        float pl = (float)this.prestigeLevel*2f;
        float factor = this.prestigeLevel<=0 ? 1f : (fromXP ? 1f/(pl*0.75f) : pl);
        return checkSkillEvent(player,Math.max(1,MathHelper.floor(((float)amount)*factor)),fromXP);
    }

    /**
     * Returns the SP in regard to the input amount that was NOT added
     */
    public int addSP(int amount, EntityPlayerMP player, boolean fromXP) {
        if(!isCapped()) {
            int amountAdded = getPrestigeFactor(player,amount,fromXP);
            if(amountAdded>0) {
                int amountNotAdded = 0;
                double factor = ((double)amount/(double)amountAdded);
                this.sp+=amountAdded;
                if(canLevelUp()) {
                    int leftover = levelUpWithOverflow(player,true,fromXP);
                    amountNotAdded+=leftover;
                } else if(this.sp==0) amountNotAdded = amountAdded;
                return Math.min(amount,(int)((double)amountNotAdded*factor));
            } else return amount;
        }
        return amount;
    }

    private int checkSkillEvent(EntityPlayerMP player, int amount, boolean fromXP) {
        if(fromXP && amount==1) return amount;
        PlayerData data = PlayerDataHandler.get(player);
        if(Objects.nonNull(data) && data.getSkillInfo(getSkill("research")).isUnlocked(TraitRegistry.TOKEN_GAMBLE)) {
            boolean foundToken = false;
            for(ItemStack stack : player.inventory.mainInventory) {
                if(stack.getItem()==ItemRegistry.SKILL_TOKEN) {
                    if(foundToken) {
                        foundToken = false;
                        break;
                    } else foundToken = true;
                }
            }
            if(foundToken) {
                int highestLevel = this.level;
                int lowestLevel = this.level;
                ISkillCapability cap = getSkillCapability(player);
                if(Objects.nonNull(cap)) {
                    for(String skill : SKILLS) {
                        int otherLevel = cap.getSkillLevel(skill);
                        if(otherLevel>highestLevel) highestLevel = otherLevel;
                        if(otherLevel<lowestLevel) lowestLevel = otherLevel;
                    }
                }
                return calculateAmount(highestLevel,lowestLevel,amount,fromXP);
            }
        }
        return amount;
    }

    private int calculateAmount(double highest, double lowest, double amount, boolean fromXP) {
        int ret = MathHelper.floor(amount*(fromXP ? 1d/(1d+highest/128d) : 1d+lowest/128d));
        return fromXP && ret<1 ? 1 : ret;
    }

    private int levelUpWithOverflow(EntityPlayerMP player, boolean showToast, boolean fromXP) {
        int amountNotAdded = 0;
        boolean leveledUp = false;
        boolean hitCap = false;
        while(canLevelUp()) {
            this.sp-=this.levelSP;
            this.level++;
            hitCap = isCapped();
            if(hitCap) {
                amountNotAdded = this.sp;
                this.level = getMaxLevel();
                this.sp = 0;
            }
            calculateLevelSP();
            leveledUp = true;
            if(hitCap) break;
        }
        PlayerData data = PlayerDataHandler.get(player);
        PlayerSkillInfo skillInfo = data.getSkillInfo(this.skill);
        int oldLevel = skillInfo.getLevel();
        skillInfo.setLevel(this.level);
        data.saveAndSync();
        if(showToast) ToastHelper.sendSkillToast(player,this.skill,skillInfo.getLevel());
        if(leveledUp) {
            World world = player.world;
            SoundEvent sound = levelUpSound(fromXP,hitCap);
            world.playSound(null,player.posX,player.posY,player.posZ,sound,SoundCategory.MASTER,1f,1f);
            MinecraftForge.EVENT_BUS.post(new LevelUpEvent.Post(player,this.skill,this.level,oldLevel));
        }
        return amountNotAdded;
    }

    private SoundEvent levelUpSound(boolean fromXP, boolean hitCap) {
        return hitCap ? SoundRegistry.BELL :
                (fromXP ? SoundEvents.ENTITY_PLAYER_LEVELUP : SoundEvents.BLOCK_END_PORTAL_SPAWN);
    }

    public void syncLevel(EntityPlayerMP player) {
        PlayerData data = PlayerDataHandler.get(player);
        if(Objects.nonNull(data)) {
            PlayerSkillInfo info = data.getSkillInfo(this.skill);
            if(Objects.nonNull(info)) {
                this.level = data.getSkillInfo(this.skill).getLevel();
                if(isCapped()) {
                    this.level = getMaxLevel();
                    this.sp = 0;
                }
                calculateLevelSP();
                if(this.sp>=this.levelSP) this.levelUpWithOverflow(player,false,true);
            }
        }
    }

    /**
     * Level SP calculations are fun...
     */
    private void calculateLevelSP() {
        if(this.level>=this.maxLevel) {
            this.levelSP = 0;
            return;
        }
        double newLevelSP;
        double multiple = ((double)this.level)/32d;
        if(multiple<=1) newLevelSP = 50d*Math.pow(1.1385d,this.level);
        else {
            double spMultiplier = 50d*multipleFactor(((int)multiple)%7)*Math.pow(10,(int)(multiple/7d));
            int levelProgress = (int)(32d*(multiple-((int)multiple)));
            if(levelProgress==0) levelProgress = 32;
            newLevelSP = spMultiplier*Math.pow(1.1385d,Math.max(1d,levelProgress));
        }
        this.levelSP = (int)(newLevelSP*this.levelFactor);
    }

    private double multipleFactor(int remainder) {
        switch(remainder) {
            case 1 : return 1.5d;
            case 2 : return 2d;
            case 3 : return 3d;
            case 4 : return 4d;
            case 5 : return 5d;
            case 6 : return 6d;
            default : return 1d;
        }
    }
}
