package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static mods.thecomputerizer.dimhoppertweaks.registry.TraitRegistry.ARBITRARY_UPGRADE;

@Mixin(Item.class)
public abstract class MixinItem {
    
    @Inject(at = @At("RETURN"), method = "getHarvestLevel", cancellable = true)
    private void dimhoppertweaks$getHarvestLevel(
            ItemStack stack, String tool, EntityPlayer player, IBlockState state, CallbackInfoReturnable<Integer> cir) {
        int val = cir.getReturnValueI();
        if(val!=-1 && SkillWrapper.hasTrait(player,"mining",ARBITRARY_UPGRADE)) cir.setReturnValue(val+1);
    }
}