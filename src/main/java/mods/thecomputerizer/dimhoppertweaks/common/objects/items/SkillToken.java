package mods.thecomputerizer.dimhoppertweaks.common.objects.items;

import mods.thecomputerizer.dimhoppertweaks.DimHopperTweaks;
import mods.thecomputerizer.dimhoppertweaks.common.skills.ISkillCapability;
import mods.thecomputerizer.dimhoppertweaks.common.skills.SkillCapabilityStorage;
import mods.thecomputerizer.dimhoppertweaks.common.skills.SkillWrapper;
import mods.thecomputerizer.dimhoppertweaks.network.PacketHandler;
import mods.thecomputerizer.dimhoppertweaks.network.packets.PacketOpenGui;
import mods.thecomputerizer.dimhoppertweaks.util.ItemUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SkillToken extends EpicItem {

    private static final String WHITE = ""+TextFormatting.WHITE;
    private static final String DARK_GRAY = ""+TextFormatting.DARK_GRAY;
    private static final String GRAY = ""+TextFormatting.GRAY;
    private static final String RED = ""+TextFormatting.RED;
    private static final String DARK_RED = ""+TextFormatting.DARK_RED;
    private static final String GOLD = ""+TextFormatting.GOLD;
    private static final String ITALICS = ""+TextFormatting.ITALIC;
    private static final String BOLD = ""+TextFormatting.ITALIC;
    private static final String RESET = ""+TextFormatting.RESET;

    public void updateSkills(ItemStack stack, Set<Map.Entry<String, SkillWrapper>> skillSet, String selectedSkill, int drainLevels) {
        NBTTagCompound tag = getTag(stack);
        for(Map.Entry<String, SkillWrapper> entry : skillSet) {
            tag.setInteger(entry.getKey()+"_xp",entry.getValue().getXP());
            tag.setInteger(entry.getKey()+"_level",entry.getValue().getLevel());
            tag.setInteger(entry.getKey()+"_level_xp",entry.getValue().getLevelXP());
            tag.setInteger(entry.getKey()+"_prestige_level",entry.getValue().getPrestigeLevel());
        }
        tag.setString("drain_selection",selectedSkill);
        tag.setInteger("drain_amount",drainLevels);
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull EntityPlayer playerIn, @Nonnull EnumHand hand) {
        if(playerIn instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)playerIn;
            ItemStack stack = player.getHeldItem(hand);
            checkAndUpdate(player, stack, "drain_selection");
            NBTTagCompound tag = getTag(stack);
            String skill = tag.getString("drain_selection");
            int amount = tag.getInteger("drain_amount");
            if(player.isSneaking()) {
                PacketHandler.NETWORK.sendTo(new PacketOpenGui.PacketOpenGuiMessage(SkillCapabilityStorage.SKILLS, skill,amount), player);
                return new ActionResult<>(EnumActionResult.SUCCESS, stack);
            } else if(player.experienceLevel>=amount) {
                ISkillCapability cap = SkillWrapper.getSkillCapability(player);
                int prestige = cap.getPrestigeLevel(skill)+1;
                for(int i=0;i<amount;i++) {
                    int SP = (int)(convertXPToSP(player.experienceLevel)*SkillWrapper.getSkillCapability(player).getXPDumpMultiplier());
                    int currSP = cap.getSkillXP(skill);
                    int levelSP = cap.getSkillLevelXP(skill);
                    int currLevel = cap.getSkillLevel(skill);
                    boolean isWithinPrestigeRange = ((double)currLevel/32d)<prestige;
                    if(currSP+SP<levelSP || (currSP+SP>=levelSP && isWithinPrestigeRange)) {
                        cap.addSkillXP(skill, SP, player, true);
                        player.addExperienceLevel(-1);
                    }
                }
                SkillWrapper.updateTokens(player);
                return new ActionResult<>(EnumActionResult.SUCCESS, stack);
            } else return super.onItemRightClick(world, player, hand);
        } else return super.onItemRightClick(world, playerIn, hand);
    }

    //XP calculations are fun...
    private float convertXPToSP(int level) {
        if (level <= 16) return ((float)(2*(level-1)+7))/2f;
        else if (level <= 31) return ((float)(5*(level-1)-38))/2f;
        return ((float)(9*(level-1)-158))/2f;
    }

    @SuppressWarnings("SameParameterValue")
    private void checkAndUpdate(EntityPlayerMP player, ItemStack stack, String data) {
        if(!getTag(stack).hasKey(data)) SkillWrapper.updateTokens(player);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flag) {
        tooltip.add(getFormattedSkillLine(stack,"mining"));
        tooltip.add(getFormattedSkillLine(stack,"gathering"));
        tooltip.add(getFormattedSkillLine(stack,"attack"));
        tooltip.add(getFormattedSkillLine(stack,"defense"));
        tooltip.add(getFormattedSkillLine(stack,"building"));
        tooltip.add(getFormattedSkillLine(stack,"agility"));
        tooltip.add(getFormattedSkillLine(stack,"farming"));
        tooltip.add(getFormattedSkillLine(stack,"magic"));
        tooltip.add(getFormattedSkillLine(stack,"void"));
        tooltip.add(getFormattedSkillLine(stack,"research"));
        if(flag.isAdvanced() && getTag(stack).hasKey("drain_selection"))
            tooltip.add(RESET+ITALICS+GRAY+ getDrainingTranslation(getTag(stack).getInteger("drain_amount"),
                            getTag(stack).getString("drain_selection")));
    }

    private String getFormattedSkillLine(ItemStack stack, String skill) {
        NBTTagCompound nbt = getTag(stack);
        if(nbt.hasKey(skill+"_xp") && nbt.hasKey(skill+"_level") && nbt.hasKey(skill+"_level_xp")) {
            String skill_color = DARK_GRAY;
            String point_color = WHITE;
            if(nbt.hasKey("drain_selection") && nbt.getString("drain_selection").matches(skill)) {
                skill_color = DARK_RED;
                point_color = RED;
            }
            int currentPoints = nbt.getInteger(skill+"_xp");
            int currentLevel = nbt.getInteger(skill+"_level");
            int prestigeLevel = nbt.getInteger(skill+"_prestige_level");
            if(currentLevel<1024) {
                int nextLevel = currentLevel + 1;
                int neededPoints = nbt.getInteger(skill+"_level_xp");
                return skill_color + BOLD + getSkillTranslation(skill) + "[" + currentLevel + "->" + nextLevel + "]: " +
                        RESET + point_color + currentPoints + "/" + neededPoints+GOLD+" {"+prestigeLevel+"}";
            } else return RESET + skill_color + BOLD + getSkillTranslation(skill) + "[" + currentLevel +"]";
        } return ITALICS+BOLD+getNotSyncedTranslation(skill);
    }

    private String getSkillTranslation(String skill) {
        return ItemUtil.getTranslationForType("skill",skill);
    }

    private String getDrainingTranslation(int levels, String skill) {
        return I18n.format("skill."+ DimHopperTweaks.MODID+".draining")+" "+levels+" "+
                I18n.format("skill."+ DimHopperTweaks.MODID+".levels")+" "+getSkillTranslation(skill);
    }

    private String getNotSyncedTranslation(String skill) {
        return I18n.format("skill."+ DimHopperTweaks.MODID+".info")+" "+getSkillTranslation(skill)+" "+
                I18n.format("skill."+ DimHopperTweaks.MODID+".synced");
    }
}
