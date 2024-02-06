package mods.thecomputerizer.dimhoppertweaks.mixin.mods.apotheosis;

import com.google.common.collect.ImmutableMap;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import shadows.ApotheosisObjects;
import shadows.ench.compat.EnchJEIPlugin;

@Mixin(value = EnchJEIPlugin.class, remap = false)
public abstract class MixinEnchJEIPlugin {

    /**
     * @author The_Computerizer
     * @reason Remove anvil recipes
     */
    @Overwrite
    public void register(IModRegistry reg) {
        ItemStack enchSword = new ItemStack(Items.DIAMOND_SWORD);
        EnchantmentHelper.setEnchantments(ImmutableMap.of(Enchantments.SHARPNESS,1),enchSword);
        ItemStack cursedSword = new ItemStack(Items.DIAMOND_SWORD);
        EnchantmentHelper.setEnchantments(ImmutableMap.of(Enchantments.BINDING_CURSE,1),cursedSword);
        ItemStack tome = new ItemStack(ApotheosisObjects.SCRAP_TOME);
        EnchantmentHelper.setEnchantments(ImmutableMap.of(Enchantments.SHARPNESS,1),tome);
        reg.addIngredientInfo(new ItemStack(Blocks.ENCHANTING_TABLE),VanillaTypes.ITEM,"info.apotheosis.enchanting");
        reg.addIngredientInfo(new ItemStack(ApotheosisObjects.PRISMATIC_ALTAR),VanillaTypes.ITEM,"info.apotheosis.altar");
    }
}