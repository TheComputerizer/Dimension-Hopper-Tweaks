package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.common.skills.SkillWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemHoe.class)
public class MixinItemHoe {

    @Inject(at = @At(value = "HEAD"), method = "setBlock")
    private void dimhoppertweaks_setBlock(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos,
                                          IBlockState state, CallbackInfo info) {
        if(player instanceof EntityPlayerMP && state.getBlock()==Blocks.FARMLAND) {
            SkillWrapper.addSP((EntityPlayerMP) player,"farming",3f,false);
        }
    }
}
