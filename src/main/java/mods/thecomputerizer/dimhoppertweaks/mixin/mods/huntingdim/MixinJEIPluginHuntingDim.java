package mods.thecomputerizer.dimhoppertweaks.mixin.mods.huntingdim;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import net.darkhax.bookshelf.registry.IVariant;
import net.darkhax.huntingdim.HuntingDimension;
import net.darkhax.huntingdim.addon.jei.JEIPluginHuntingDim;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nonnull;

@Mixin(value = JEIPluginHuntingDim.class, remap = false)
public abstract class MixinJEIPluginHuntingDim implements IModPlugin {

    @Override
    public void register(@Nonnull IModRegistry registry) {
        for(Item item : HuntingDimension.REGISTRY.getItems()) {
            if(!(item instanceof IVariant)) {
                String key = "jei."+ item.getTranslationKey();
                registry.addIngredientInfo(new ItemStack(item,1,32767),ItemStack.class,key);
            }
        }
    }
}
