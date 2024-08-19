package mods.thecomputerizer.dimhoppertweaks.mixin.mods.projecte;

import moze_intel.projecte.gameObjs.items.ItemPE;
import moze_intel.projecte.gameObjs.items.Matter;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nonnull;

@Mixin(value = Matter.class, remap = false)
public abstract class MixinMatter extends ItemPE {
    
    @Shadow @Final private String[] names;
    
    @Override
    public @Nonnull String getTranslationKey(ItemStack stack) {
        int meta = Math.min(this.names.length-1,stack.getItemDamage());
        return super.getTranslationKey()+"_"+this.names[meta];
    }
}
