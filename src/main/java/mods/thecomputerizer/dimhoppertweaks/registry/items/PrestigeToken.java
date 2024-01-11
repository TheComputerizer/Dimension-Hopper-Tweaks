package mods.thecomputerizer.dimhoppertweaks.registry.items;

import mcp.MethodsReturnNonnullByDefault;
import mods.thecomputerizer.dimhoppertweaks.common.capability.ISkillCapability;
import mods.thecomputerizer.dimhoppertweaks.common.capability.SkillWrapper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PrestigeToken extends EpicItem {

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer p, EnumHand hand) {
        ItemStack stack = p.getHeldItem(hand);
        if(p instanceof EntityPlayerMP && stack.getItem() instanceof PrestigeToken) {
            EntityPlayerMP player = (EntityPlayerMP)p;
            ISkillCapability cap = SkillWrapper.getSkillCapability(player);
            if(Objects.nonNull(cap)) {
                String skill = cap.getDrainSelection();
                int level = getPrestigeLevel(stack);
                if(cap.getPrestigeLevel(skill)<level) {
                    cap.setPrestigeLevel(skill,level);
                    SkillWrapper.updateTokens(player);
                    world.playSound(null,player.posX,player.posY,player.posZ,SoundEvents.ENTITY_PLAYER_LEVELUP,
                            SoundCategory.MASTER, 1f,1f);
                    world.playSound(null,player.posX,player.posY,player.posZ,
                            SoundEvents.UI_TOAST_CHALLENGE_COMPLETE,SoundCategory.MASTER,1f,1f);
                    stack.shrink(1);
                    return ActionResult.newResult(EnumActionResult.SUCCESS,stack);
                }
            }
        } return ActionResult.newResult(EnumActionResult.PASS,stack);
    }

    @Override
    public String getHighlightTip(ItemStack stack, String name) {
        int level = getPrestigeLevel(stack);
        return String.format(name+" %d [%d]",level,(level+1)*32);
    }

    public int getPrestigeLevel(ItemStack stack) {
        return MathHelper.clamp(getTag(stack).getInteger("prestigeLevel"),0,31);
    }
}
