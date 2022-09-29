package mods.thecomputerizer.dimensionhoppertweaks.common.objects.items;

import mods.thecomputerizer.dimensionhoppertweaks.DimensionHopperTweaks;
import mods.thecomputerizer.dimensionhoppertweaks.util.ItemUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class SkillToken extends EpicItem {

    private static final String WHITE = ""+TextFormatting.WHITE;
    private static final String DARK_GRAY = ""+TextFormatting.DARK_GRAY;
    private static final String RED = ""+TextFormatting.RED;
    private static final String DARK_RED = ""+TextFormatting.DARK_RED;
    private static final String ITALICS = ""+TextFormatting.ITALIC;
    private static final String BOLD = ""+TextFormatting.ITALIC;
    private static final String RESET = ""+TextFormatting.RESET;

    public void updateSkills(ItemStack stack, int[] values) {
        NBTTagCompound tag = getTag(stack);
        tag.setInteger("mining_xp",values[0]);
        tag.setInteger("gathering_xp",values[1]);
        tag.setInteger("attack_xp",values[2]);
        tag.setInteger("defense_xp",values[3]);
        tag.setInteger("building_xp",values[4]);
        tag.setInteger("agility_xp",values[5]);
        tag.setInteger("farming_xp",values[6]);
        tag.setInteger("magic_xp",values[7]);
        tag.setInteger("void_xp",values[8]);
        tag.setInteger("research_xp",values[9]);
        tag.setInteger("mining_max",values[10]);
        tag.setInteger("gathering_max",values[11]);
        tag.setInteger("attack_max",values[12]);
        tag.setInteger("defense_max",values[13]);
        tag.setInteger("building_max",values[14]);
        tag.setInteger("agility_max",values[15]);
        tag.setInteger("farming_max",values[16]);
        tag.setInteger("magic_max",values[17]);
        tag.setInteger("void_max",values[18]);
        tag.setInteger("research_max",values[19]);
        if(tag.hasKey("drain_selection")) {
            tag.setString("drain_selection","mining");
            tag.setInteger("drain_amount",1);
        }
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
        if(flag.isAdvanced() && ItemUtil.getOrCreateTag(stack).hasKey("drain_selection"))
            tooltip.add(""+TextFormatting.ITALIC+TextFormatting.GRAY+
                    getDrainingTranslation(getTag(stack).getInteger("drain_amount"),
                            getTag(stack).getString("drain_selection")));
    }

    private String getFormattedSkillLine(ItemStack stack, String skill) {
        NBTTagCompound nbt = getTag(stack);
        if(nbt.hasKey(skill+"_xp") && nbt.hasKey(skill+"_max")) {
            String skill_color = DARK_GRAY;
            String point_color = WHITE;
            if(nbt.hasKey("drain_selection") && nbt.getString("drain_selection").matches(skill)) {
                skill_color = DARK_RED;
                point_color = RED;
            }
            int currentPoints = nbt.getInteger(skill+"_xp");
            int neededPoints = nbt.getInteger(skill+"_max");
            int currentLevel = neededPoints/100;
            return skill_color+BOLD+getSkillTranslation(skill)+"["+currentLevel+"->"+currentLevel+1+"]: "+RESET+point_color+currentPoints+"/"+neededPoints;
        } return ITALICS+BOLD+getNotSyncedTranslation(skill);
    }

    private String getSkillTranslation(String skill) {
        return ItemUtil.getTranslationForType("skill",skill);
    }

    private String getDrainingTranslation(int levels, String skill) {
        return I18n.format("skill."+ DimensionHopperTweaks.MODID+".draining")+" "+levels+
                I18n.format("skill."+ DimensionHopperTweaks.MODID+".levels")+" "+getSkillTranslation(skill);
    }

    private String getNotSyncedTranslation(String skill) {
        return I18n.format("skill."+ DimensionHopperTweaks.MODID+".info")+" "+getSkillTranslation(skill)+" "+
                I18n.format("skill."+ DimensionHopperTweaks.MODID+".synced");
    }
}
