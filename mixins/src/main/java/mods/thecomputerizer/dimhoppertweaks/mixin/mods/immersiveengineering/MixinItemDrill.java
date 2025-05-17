package mods.thecomputerizer.dimhoppertweaks.mixin.mods.immersiveengineering;

import blusunrize.immersiveengineering.api.tool.IDrillHead;
import blusunrize.immersiveengineering.common.items.ItemDrill;
import blusunrize.immersiveengineering.common.items.ItemUpgradeableTool;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mixin(value = ItemDrill.class, remap = false)
public abstract class MixinItemDrill extends ItemUpgradeableTool {
    
    @Shadow public abstract ItemStack getHead(ItemStack drill);
    
    public MixinItemDrill(String name, int stackSize, String upgradeType, String... subNames) {
        super(name,stackSize,upgradeType,subNames);
    }
    
    @Override public int getHarvestLevel(@Nonnull ItemStack stack, @Nonnull String tool, @Nullable EntityPlayer player,
            @Nullable IBlockState state) {
        ItemStack head = getHead(stack);
        int level = !head.isEmpty() ?
                ((IDrillHead)head.getItem()).getMiningLevel(head)+ItemNBTHelper.getInt(stack,"harvestLevel") : 0;
        if(SkillWrapper.hasMiningLevelUprade(player)) level++;
        return level;
    }
}