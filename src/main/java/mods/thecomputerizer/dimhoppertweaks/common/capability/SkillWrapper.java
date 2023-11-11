package mods.thecomputerizer.dimhoppertweaks.common.capability;

import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerSkillInfo;
import codersafterdark.reskillable.api.event.LevelUpEvent;
import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.api.toast.ToastHelper;
import codersafterdark.reskillable.api.unlockable.IAbilityEventHandler;
import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import mods.thecomputerizer.dimhoppertweaks.registry.items.SkillToken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Consumer;

public class SkillWrapper {

    public static final ResourceLocation SKILL_CAPABILITY = Constants.res("skills");

    @SuppressWarnings("ConstantConditions")
    public static @Nullable ISkillCapability getSkillCapability(EntityPlayer player) {
        return Objects.nonNull(player) && Objects.nonNull(SkillCapabilityProvider.SKILL_CAPABILITY) ?
                player.getCapability(SkillCapabilityProvider.SKILL_CAPABILITY,null) : null;
    }

    public static void addSP(EntityPlayerMP player, String skill, float amount, boolean fromXP) {
        ISkillCapability cap = getSkillCapability(player);
        if(Objects.nonNull(cap)) cap.addSkillXP(skill,(int)withMultiplier(player,amount),player,fromXP);
    }

    public static float withMultiplier(EntityPlayerMP player, float amount) {
        ISkillCapability cap = getSkillCapability(player);
        return Objects.nonNull(cap) ? cap.getSkillXpMultiplier(amount) : 0f;
    }

    public static void shieldHook(EntityPlayerMP player, byte state) {
        ISkillCapability cap = getSkillCapability(player);
        if(Objects.isNull(cap)) return;
        float amount = cap.getShieldedDamage();
        if(state==29 && amount>0) addSP(player,"defense",Math.max(1f,amount/2f),false);
    }

    public static void updateTokens(EntityPlayerMP player) {
        ISkillCapability cap = getSkillCapability(player);
        if(Objects.isNull(cap)) return;
        cap.syncSkills(player);
        for(int i=0; i<player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if(stack.getItem() instanceof SkillToken) {
                SkillToken token = (SkillToken) stack.getItem();
                token.updateSkills(stack,cap.getCurrentValues(),cap.getDrainSelection(),cap.getDrainLevels());
            }
        }
    }

    @SuppressWarnings("ConstantValue")
    public static void forceTwilightRespawn(EntityPlayer player) {
        ISkillCapability cap = getSkillCapability(player);
        if(Objects.isNull(cap)) return;
        BlockPos pos = cap.getTwilightRespawn();
        if(player.dimension==7 && Objects.isNull(player.getBedLocation(7)) && Objects.nonNull(pos))
            player.setSpawnPoint(pos,true);
    }

    public static boolean ticKDreamer(EntityPlayerMP player, int ticks) {
        ISkillCapability cap = getSkillCapability(player);
        if(Objects.isNull(cap)) return false;
        return cap.incrementDreamTimer(player,ticks);
    }

    public static double getPrestigeFactor(EntityPlayer player, String skill) {
        ISkillCapability cap = getSkillCapability(player);
        return Objects.isNull(cap) ? 1d : 1d+(((double)cap.getSkillLevel(skill))/32d);
    }

    public static @Nullable Skill getSkill(String name) {
        ResourceLocation skillRes =  name.matches("research") || name.matches("void") ? Constants.res(name) :
                new ResourceLocation("reskillable",name);
        return ReskillableRegistries.SKILLS.containsKey(skillRes) ? ReskillableRegistries.SKILLS.getValue(skillRes) : null;
    }

    public static void executeOnSkills(@Nonnull PlayerData data, Consumer<IAbilityEventHandler> consumer) {
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

    public static void executeOnSkill(@Nonnull PlayerData data, @Nullable Skill skill, Consumer<IAbilityEventHandler> consumer) {
        if(Objects.nonNull(skill)) data.getSkillInfo(skill).forEachEventHandler(consumer);
    }

    private final String modid;
    private final String name;
    private final int maxLevel;
    private int xp;
    private int level;
    private int levelXP;
    private int prestigeLevel;

    public SkillWrapper(String name, int xp, int level, int prestigeLevel) {
        this.modid = name.matches("research") || name.matches("void") ? Constants.MODID : "reskillable";
        this.name = name;
        this.xp = xp;
        this.level = level;
        this.levelXP = calculateLevelXP(level);
        Skill skill = getSkill();
        int cap = 1024;
        if(skill!=null) cap = getSkill().getCap();
        this.maxLevel = cap;
        this.prestigeLevel = prestigeLevel;
        Constants.LOGGER.debug("Registered skill {}:{} at level {} with xp {}/{}",modid,name,level,xp,levelXP);
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

    public void setPrestigeLevel(int level) {
        this.prestigeLevel = level;
    }

    public int getPrestigeLevel() {
        return this.prestigeLevel;
    }

    private boolean canLevelUp(int amount) {
        if(this.xp>=this.levelXP && this.level!=0 && !isMaxLevel() && !isMaxLevelForPrestige()) {
            if(((double)this.level)/32d<=this.prestigeLevel+1) return true;
            else this.xp-=amount;
        }
        return false;
    }

    private boolean isMaxLevel() {
        return this.level>=this.maxLevel;
    }

    private boolean isMaxLevelForPrestige() {
        return MathHelper.floor(((double)this.level)/32d)>=this.prestigeLevel+1;
    }

    private int getPrestigeFactor(int amount, boolean fromXP) {
        float pl = (float)this.prestigeLevel*2f;
        float factor = this.prestigeLevel<=0 ? 1f : (fromXP ? 1f/(pl*0.75f) : pl);
        return Math.max(0,MathHelper.floor(((float)amount)*factor));
    }

    public void addXP(int amount, EntityPlayerMP player, boolean fromXP) {
        if(!isMaxLevel() && !isMaxLevelForPrestige() && this.level!=0) {
            amount = getPrestigeFactor(amount,fromXP);
            if(amount>0) {
                this.xp+=amount;
                if(canLevelUp(amount)) levelUpWithOverflow(player,true,fromXP);
            }
        }
    }

    private void levelUpWithOverflow(EntityPlayerMP player, boolean showToast, boolean fromXP) {
        boolean leveledUp = false;
        int amount = this.xp;
        if(!fromXP) amount = 0;
        while(canLevelUp(amount)) {
            this.xp-=this.levelXP;
            this.level++;
            if(isMaxLevel()) {
                this.level = this.maxLevel;
                this.levelXP = 0;
                this.xp = 0;
                break;
            } else if(isMaxLevelForPrestige()) {
                this.xp = 0;
                this.levelXP = calculateLevelXP(this.level);
                break;
            } else this.levelXP = calculateLevelXP(this.level);
            leveledUp = true;
        }
        PlayerData data = PlayerDataHandler.get(player);
        PlayerSkillInfo skillInfo = data.getSkillInfo(getSkill());
        int oldLevel = skillInfo.getLevel();
        skillInfo.setLevel(this.level);
        data.saveAndSync();
        if(showToast) ToastHelper.sendSkillToast(player,getSkill(),skillInfo.getLevel());
        if(leveledUp) {
            World world = player.world;
            SoundEvent levelSound = fromXP ? SoundEvents.ENTITY_PLAYER_LEVELUP : SoundEvents.BLOCK_END_PORTAL_SPAWN;
            world.playSound(null,player.posX,player.posY,player.posZ,levelSound,SoundCategory.MASTER,1f,1f);
            MinecraftForge.EVENT_BUS.post(new LevelUpEvent.Post(player,getSkill(),this.level,oldLevel));
        }
    }

    public void syncLevel(EntityPlayerMP player) {
        if(Objects.nonNull(PlayerDataHandler.get(player)) && Objects.nonNull(PlayerDataHandler.get(player).getSkillInfo(getSkill()))) {
            this.level = PlayerDataHandler.get(player).getSkillInfo(getSkill()).getLevel();
            if (isMaxLevel()) {
                this.level = this.maxLevel;
                this.levelXP = 0;
                this.xp = 0;
            } else this.levelXP = calculateLevelXP(this.level);
            if (this.xp>=this.levelXP) this.levelUpWithOverflow(player,false,true);
        }
    }

    private Skill getSkill() {
        return ReskillableRegistries.SKILLS.getValue(new ResourceLocation(this.modid,this.name));
    }

    //level xp calculations are fun...
    private int calculateLevelXP(double level) {
        double levelXP;
        double multiple = level/32d;
        if(multiple<=1) levelXP = 50d*Math.pow(1.1385d,level);
        else {
            double xpMultiplier = 50d*multipleFactor(((int) multiple)%7)*Math.pow(10,(int)(multiple/7d));
            int levelProgress = (int)(32d*(multiple-((int)multiple)));
            if(levelProgress==0) levelProgress = 32;
            levelXP = xpMultiplier*Math.pow(1.1385d,Math.max(1d,levelProgress));
        }
        return (int)(levelXP*levelFactor());
    }

    private double levelFactor() {
        switch(this.name) {
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
