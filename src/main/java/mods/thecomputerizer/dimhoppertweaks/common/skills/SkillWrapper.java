package mods.thecomputerizer.dimhoppertweaks.common.skills;

import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerSkillInfo;
import codersafterdark.reskillable.api.event.LevelUpEvent;
import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.api.toast.ToastHelper;
import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import mods.thecomputerizer.dimhoppertweaks.common.objects.items.SkillToken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.Objects;

public class SkillWrapper {

    public static final ResourceLocation SKILL_CAPABILITY = new ResourceLocation(Constants.MODID, "skills");

    @SuppressWarnings("ConstantConditions")
    public static ISkillCapability getSkillCapability(EntityPlayer player) {
        return player.getCapability(SkillCapabilityProvider.SKILL_CAPABILITY,null);
    }

    public static void addSP(EntityPlayerMP player, String skill, float amount, boolean fromXP) {
        getSkillCapability(player).addSkillXP(skill, (int)withMultiplier(player, amount), player,fromXP);
    }

    public static float withMultiplier(EntityPlayerMP player, float amount) {
        return getSkillCapability(player).getSkillXpMultiplier(amount);
    }

    public static void shieldHook(EntityPlayerMP player, byte state) {
        float amount = getSkillCapability(player).getShieldedDamage();
        if(state==29 && amount>0) addSP(player,"defense",Math.max(1f,amount/2f),false);
    }

    public static void updateTokens(EntityPlayerMP player) {
        getSkillCapability(player).syncSkills(player);
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (stack.getItem() instanceof SkillToken) {
                SkillToken token = (SkillToken) stack.getItem();
                ISkillCapability cap = getSkillCapability(player);
                token.updateSkills(stack, cap.getCurrentValues(), cap.getDrainSelection(), cap.getDrainLevels());
            }
        }
    }

    @SuppressWarnings("ConstantValue")
    public static void forceTwilightRespawn(EntityPlayer player) {
        BlockPos pos = getSkillCapability(player).getTwilightRespawn();
        if(player.dimension==7 && Objects.isNull(player.getBedLocation(7)) && Objects.nonNull(pos))
            player.setSpawnPoint(pos,true);
    }

    private final String modid;
    private final String name;
    private final int maxLevel;
    private int xp;
    private int level;
    private int levelXP;
    private int prestigeLevel;

    public SkillWrapper(String modid, String name, int xp, int level) {
        this.modid = modid;
        this.name = name;
        this.xp = xp;
        this.level = level;
        this.levelXP = calculateLevelXP(level);
        Skill skill = getSkill();
        int cap = 1024;
        if(skill!=null) cap = getSkill().getCap();
        this.maxLevel = cap;
        this.prestigeLevel = 0;
        Constants.LOGGER.info("Registered skill {}:{} at level {} with xp {}/{}",modid,name,level,xp,levelXP);
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
        if(this.xp>=this.levelXP && this.level!=0 && !isMaxLevel()) {
            if (((double) this.level) / 32d <= this.prestigeLevel+1) return true;
            else this.xp-=amount;
        }
        return false;
    }

    private boolean isMaxLevel() {
        return this.level>=this.maxLevel;
    }

    public void addXP(int amount, EntityPlayerMP player, boolean fromXP) {
        if(!isMaxLevel() && this.level!=0) {
            this.xp+=amount;
            if(canLevelUp(amount)) levelUpWithOverflow(player, true, fromXP);
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
            } else this.levelXP = calculateLevelXP(this.level);
            leveledUp = true;
        }
        PlayerData data = PlayerDataHandler.get(player);
        PlayerSkillInfo skillInfo = data.getSkillInfo(getSkill());
        int oldLevel = skillInfo.getLevel();
        skillInfo.setLevel(this.level);
        data.saveAndSync();
        if(showToast) ToastHelper.sendSkillToast(player, getSkill(), skillInfo.getLevel());
        if(leveledUp) {
            World world = player.world;
            SoundEvent levelSound = SoundEvents.BLOCK_END_PORTAL_SPAWN;
            if(fromXP) levelSound = SoundEvents.ENTITY_PLAYER_LEVELUP;
            world.playSound(null, player.posX, player.posY, player.posZ, levelSound, SoundCategory.MASTER, 1.0F, 1.0F);
            MinecraftForge.EVENT_BUS.post(new LevelUpEvent.Post(player, getSkill(), this.level, oldLevel));
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
            if (this.xp >= this.levelXP) this.levelUpWithOverflow(player, false, true);
        }
    }

    private Skill getSkill() {
        return ReskillableRegistries.SKILLS.getValue(new ResourceLocation(this.modid, this.name));
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
        return (int) (levelXP*levelFactor());
    }

    private double levelFactor() {
        switch (this.name) {
            case "mining":
            case "building":
                return 2d;
            case "gathering":
            case "magic":
                return 3d;
            case "attack":
            case "defense":
            case "farming":
                return 1.5d;
            case "agility": return 10d;
            case "void": return 0.75d;
            case "research": return 1.1d;
            default: return 1d;
        }
    }

    private double multipleFactor(int remainder) {
        switch (remainder) {
            case 1 : return 1.5d;
            case 2 : return 2d;
            case 3 : return 3d;
            case 4 : return 4d;
            case 5 : return 5d;
            case 6 : return 6d;
            default : return 1d;
        }
    }

    public NBTTagCompound writeNBT(NBTTagCompound compound) {
        compound.setInteger(this.name+"_xp",this.xp);
        compound.setInteger(this.name+"_level",this.level);
        compound.setInteger(this.name+"_prestige",this.prestigeLevel);
        return compound;
    }
}
