package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static mods.thecomputerizer.dimhoppertweaks.registry.TraitRegistry.ARBITRARY_UPGRADE;

@Mixin(Item.class)
public abstract class MixinItem {
    
    @Unique private Item dimhoppertweaks$cast() {
        return (Item)(Object)this;
    }
    
    @Inject(at = @At("RETURN"), method = "getHarvestLevel", cancellable = true, remap = false)
    private void dimhoppertweaks$getHarvestLevel(
            ItemStack stack, String tool, EntityPlayer player, IBlockState state, CallbackInfoReturnable<Integer> cir) {
        int val = cir.getReturnValueI();
        if(SkillWrapper.hasTrait(player,"mining",ARBITRARY_UPGRADE)) {
            if(val!=-1) cir.setReturnValue(val+1);
            else {
                Item instance = dimhoppertweaks$cast();
                if(instance instanceof ItemTool)
                    cir.setReturnValue(((ItemTool)instance).toolMaterial.getHarvestLevel()+1);
            }
        }
    }
}