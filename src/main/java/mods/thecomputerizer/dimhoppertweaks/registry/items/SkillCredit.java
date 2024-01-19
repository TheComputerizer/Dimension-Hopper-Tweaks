package mods.thecomputerizer.dimhoppertweaks.registry.items;

import mcp.MethodsReturnNonnullByDefault;
import mods.thecomputerizer.dimhoppertweaks.common.capability.ISkillCapability;
import mods.thecomputerizer.dimhoppertweaks.common.capability.SkillWrapper;
import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import mods.thecomputerizer.dimhoppertweaks.util.ItemUtil;
import mods.thecomputerizer.dimhoppertweaks.util.TextUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SkillCredit extends EpicItem {

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer p, EnumHand hand) {
        if(p instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)p;
            ISkillCapability cap = SkillWrapper.getSkillCapability(player);
            ItemStack stack = player.getHeldItem(hand);
            String skill = getSkill(stack);
            int amount = getAmount(stack);
            if(Objects.nonNull(cap) && Objects.nonNull(skill) && amount>0) {
                amount = cap.addSkillSP(skill,amount,player,false);
                if(amount>0) {
                    NBTTagCompound tag = ItemUtil.getOrCreateTag(stack);
                    tag.setInteger("amount",amount);
                } else stack.shrink(1);
                return new ActionResult<>(EnumActionResult.SUCCESS,stack);
            } else return super.onItemRightClick(world,p,hand);
        } else return super.onItemRightClick(world,p,hand);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        int amount = getAmount(stack);
        if(amount==-1) tooltip.add("Unspecified SP amount");
        else tooltip.add(String.format("%d SP",amount));
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String name = super.getItemStackDisplayName(stack);
        String skill = getSkillTranslation(stack);
        return Objects.nonNull(skill) ? String.format("%s [%s]",name,getSkillTranslation(stack)) : name;
    }

    private int getAmount(ItemStack stack) {
        NBTTagCompound tag = ItemUtil.getOrCreateTag(stack);
        int amount = tag.getInteger("amount");
        return amount>0 ? amount : -1;
    }

    private @Nullable String getSkill(ItemStack stack) {
        NBTTagCompound tag = ItemUtil.getOrCreateTag(stack);
        String skill = tag.getString("skill");
        return StringUtils.isNotBlank(skill) && SkillWrapper.isValidSkill(skill) ? skill : null;
    }

    private @Nullable String getSkillTranslation(ItemStack stack) {
        String skill = getSkill(stack);
        return Objects.nonNull(skill) ? TextUtil.getTranslated(String.format("%s.%s.%s","skill",DHTRef.MODID,skill)) : null;
    }
}
