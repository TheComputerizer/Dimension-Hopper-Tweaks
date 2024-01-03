package mods.thecomputerizer.dimhoppertweaks.registry.items;

import mcp.MethodsReturnNonnullByDefault;
import mods.thecomputerizer.dimhoppertweaks.common.capability.ISkillCapability;
import mods.thecomputerizer.dimhoppertweaks.common.capability.SkillCapabilityStorage;
import mods.thecomputerizer.dimhoppertweaks.common.capability.SkillWrapper;
import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import mods.thecomputerizer.dimhoppertweaks.network.PacketOpenGui;
import mods.thecomputerizer.dimhoppertweaks.util.TextUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Tuple;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SkillToken extends EpicItem {

    public void updateSkills(ItemStack stack, Set<Map.Entry<String,SkillWrapper>> skillSet, String selectedSkill, int drainLevels) {
        NBTTagCompound tag = getTag(stack);
        NBTTagList listTag = new NBTTagList();
        for(Map.Entry<String, SkillWrapper> entry : skillSet)
            listTag.appendTag(updateSkill(entry.getKey(),entry.getValue()));
        tag.setTag("skillData",listTag);
        tag.setString("skillToDrain",selectedSkill);
        tag.setInteger("drainLevels",drainLevels);
    }
    
    private NBTTagCompound updateSkill(String skill, SkillWrapper wrapper) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("name",skill);
        tag.setInteger("xp",wrapper.getXP());
        tag.setInteger("level",wrapper.getLevel());
        tag.setInteger("levelXP",wrapper.getLevelXP());
        tag.setInteger("prestige",wrapper.getPrestigeLevel());
        return tag;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer p, EnumHand hand) {
        if(p instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)p;
            ItemStack stack = player.getHeldItem(hand);
            checkAndUpdate(player,stack);
            NBTTagCompound tag = getTag(stack);
            String skill = tag.getString("skillToDrain");
            int amount = tag.getInteger("drainLevels");
            if(player.isSneaking()) {
                new PacketOpenGui(SkillCapabilityStorage.SKILLS,skill,amount).addPlayers(player).send();
                return new ActionResult<>(EnumActionResult.SUCCESS,stack);
            } else if(player.experienceLevel>=amount) {
                ISkillCapability cap = SkillWrapper.getSkillCapability(player);
                if(Objects.nonNull(cap)) {
                    int prestige = cap.getPrestigeLevel(skill)+1;
                    for(int i=0; i<amount; i++) {
                        int SP = (int)(convertXPToSP(player.experienceLevel)*cap.getXPDumpMultiplier());
                        int currSP = cap.getSkillXP(skill);
                        int levelSP = cap.getSkillLevelXP(skill);
                        int currLevel = cap.getSkillLevel(skill);
                        boolean isWithinPrestigeRange = ((double)currLevel/32d)<prestige;
                        if (currSP+SP<levelSP || (currSP+SP>=levelSP && isWithinPrestigeRange)) {
                            cap.addSkillXP(skill,SP,player,true);
                            player.addExperienceLevel(-1);
                        }
                    }
                    SkillWrapper.updateTokens(player);
                }
                return new ActionResult<>(EnumActionResult.SUCCESS,stack);
            } else return super.onItemRightClick(world,player,hand);
        } else return super.onItemRightClick(world,p,hand);
    }

    //XP calculations are fun...
    private float convertXPToSP(int level) {
        if(level<=16) return ((float)(2*(level-1)+7))/2f;
        else if(level<=31) return ((float)(5*(level-1)-38))/2f;
        return ((float)(9*(level-1)-158))/2f;
    }


    private void checkAndUpdate(EntityPlayerMP player, ItemStack stack) {
        if(!getTag(stack).hasKey("skillToDrain")) SkillWrapper.updateTokens(player);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        NBTTagCompound tag = getTag(stack);
        boolean hasDrainKey = tag.hasKey("skillToDrain");
        String skillToDrain = hasDrainKey ? tag.getString("skillToDrain") : "mining";
        if(tag.hasKey("skillData")) {
            Map<String,String> formattedSkillData = new HashMap<>();
            NBTBase listTag = tag.getTag("skillData");
            if(listTag instanceof NBTTagList) {
                for(NBTBase baseTag : (NBTTagList)listTag) {
                    Tuple<String,String> skillLine = getSkillLine(baseTag,skillToDrain);
                    if(Objects.nonNull(skillLine))
                        formattedSkillData.put(skillLine.getFirst(),skillLine.getSecond());
                }
            }
            for(String skill : SkillCapabilityStorage.SKILLS) {
                String line = formattedSkillData.get(skill);
                if(Objects.nonNull(line)) tooltip.add(line);
            }
        } else addNotSyncedLines(tooltip);
        if(flag.isAdvanced() && hasDrainKey)
            tooltip.add(addXPDrainLine(skillToDrain,tag.getInteger("drainLevels")));
    }

    private @Nullable Tuple<String,String> getSkillLine(NBTBase baseTag, String skillToDrain) {
        if(!(baseTag instanceof NBTTagCompound)) return null;
        NBTTagCompound tag = (NBTTagCompound)baseTag;
        String name = tag.getString("name");
        boolean isDraining = name.matches(skillToDrain);
        name = translateSkill(name);
        int curLevel = tag.getInteger("level");
        boolean isMaxed = curLevel==1024;
        String skillColor = isDraining ? (isMaxed ? TextUtil.ITALICS+TextUtil.BOLD : TextUtil.DARK_RED) :
                (isMaxed ? TextUtil.BOLD : TextUtil.DARK_GRAY);
        if(isMaxed) return new Tuple<>(tag.getString("name"),
                TextUtil.getTranslated("item.dimhoppertweaks.skill_token.skill_maxed",skillColor,name));
        else {
            int xp = tag.getInteger("xp");
            int levelXP = tag.getInteger("levelXP");
            int prestige = tag.getInteger("prestige");
            String pointColor = isDraining ? TextUtil.RED : TextUtil.WHITE;
            return new Tuple<>(tag.getString("name"),TextUtil.getTranslated(
                    "item.dimhoppertweaks.skill_token.skill_normal",skillColor,name,curLevel,curLevel+1,
                    pointColor,xp,levelXP,prestige,32*(prestige+1)));
        }
    }

    private void addNotSyncedLines(List<String> tooltipLines) {
        for(String skill : SkillCapabilityStorage.SKILLS)
            tooltipLines.add(TextUtil.getTranslated("item.dimhoppertweaks.skill_token.not_synced",
                    translateSkill(skill)));
    }

    private String addXPDrainLine(String skill, int levels) {
        return TextUtil.getTranslated("item.dimhoppertweaks.skill_token.xp_drain",levels,
                levels==1 ? "level" : "levels",translateSkill(skill));
    }

    private String translateSkill(String skillName) {
        return TextUtil.getTranslated("skill."+ DHTRef.MODID+"."+skillName);
    }
}
