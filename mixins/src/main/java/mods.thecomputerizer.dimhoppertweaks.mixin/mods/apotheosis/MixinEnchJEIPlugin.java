package mods.thecomputerizer.dimhoppertweaks.mixin.mods.apotheosis;

import com.google.common.collect.ImmutableMap;
import mezz.jei.api.IModRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import shadows.ench.compat.EnchJEIPlugin;

import static mezz.jei.api.ingredients.VanillaTypes.ITEM;
import static net.minecraft.init.Blocks.ENCHANTING_TABLE;
import static net.minecraft.init.Enchantments.BINDING_CURSE;
import static net.minecraft.init.Enchantments.SHARPNESS;
import static net.minecraft.init.Items.DIAMOND_SWORD;
import static shadows.ApotheosisObjects.PRISMATIC_ALTAR;
import static shadows.ApotheosisObjects.SCRAP_TOME;

@Mixin(value = EnchJEIPlugin.class, remap = false)
public abstract class MixinEnchJEIPlugin {

    /**
     * @author The_Computerizer
     * @reason Remove anvil recipes
     */
    @Overwrite
    public void register(IModRegistry reg) {
        ItemStack enchSword = new ItemStack(DIAMOND_SWORD);
        EnchantmentHelper.setEnchantments(ImmutableMap.of(SHARPNESS,1),enchSword);
        ItemStack cursedSword = new ItemStack(DIAMOND_SWORD);
        EnchantmentHelper.setEnchantments(ImmutableMap.of(BINDING_CURSE,1),cursedSword);
        ItemStack tome = new ItemStack(SCRAP_TOME);
        EnchantmentHelper.setEnchantments(ImmutableMap.of(SHARPNESS,1),tome);
        reg.addIngredientInfo(new ItemStack(ENCHANTING_TABLE),ITEM,"info.apotheosis.enchanting");
        reg.addIngredientInfo(new ItemStack(PRISMATIC_ALTAR),ITEM,"info.apotheosis.altar");
    }
}