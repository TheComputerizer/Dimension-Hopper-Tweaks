package mods.thecomputerizer.dimhoppertweaks.registry.items;

import mcp.MethodsReturnNonnullByDefault;
import mods.thecomputerizer.dimhoppertweaks.common.skills.ISkillCapability;
import mods.thecomputerizer.dimhoppertweaks.common.skills.SkillWrapper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PrestigeToken extends EpicItem {

    private final int level;
    public PrestigeToken(int level) {
        this.level = level;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer p, EnumHand hand) {
        ItemStack stack = p.getHeldItemMainhand();
        if(p instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)p;
            ISkillCapability cap = SkillWrapper.getSkillCapability(player);
            if(Objects.nonNull(cap)) {
                String skill = cap.getDrainSelection();
                if(cap.getPrestigeLevel(skill)<this.level && hand==EnumHand.MAIN_HAND) {
                    cap.setPrestigeLevel(skill,this.level);
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
}
