package mods.thecomputerizer.dimhoppertweaks.mixin.mods.tconstruct;

import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import slimeknights.tconstruct.library.tinkering.TinkersItem;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.library.utils.ToolHelper;

import javax.annotation.Nullable;

@Mixin(value = ToolCore.class, remap = false)
public abstract class MixinToolCore extends TinkersItem {
    
    @Override
    public int getHarvestLevel(
            ItemStack stack, String tool, @Nullable EntityPlayer player, @Nullable IBlockState state) {
        if(ToolHelper.isBroken(stack)) return -1;
        else {
            if(getToolClasses(stack).contains(tool)) {
                int level = ToolHelper.getHarvestLevelStat(stack);
                if(SkillWrapper.hasMiningLevelUprade(player)) level++;
                return level;
            }
            return super.getHarvestLevel(stack,tool,player,state);
        }
    }
}