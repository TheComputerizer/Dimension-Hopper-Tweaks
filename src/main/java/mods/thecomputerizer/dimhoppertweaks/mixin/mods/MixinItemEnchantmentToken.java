package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.key.KeyTracker;
import net.silentchaos512.gems.item.ItemEnchantmentToken;
import org.spongepowered.asm.mixin.*;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@Mixin(value = ItemEnchantmentToken.class, remap = false)
public abstract class MixinItemEnchantmentToken {

    @Shadow private static Map<Enchantment, Integer> getEnchantmentMap(ItemStack stack) {
        return null;
    }

    @Shadow @Final private static Map<Enchantment, String> RECIPE_MAP;

    @Unique ItemEnchantmentToken dimhoppertweaks$cast() {
        return (ItemEnchantmentToken)(Object)this;
    }

    /**
     * @author The_Computerizer
     * @reason Fix null issue with JEI
     */
    @SideOnly(Side.CLIENT)
    @Overwrite
    public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag) {
        if(Objects.isNull(stack) || stack.isEmpty()) return;
        Map<Enchantment, Integer> enchants = getEnchantmentMap(stack);
        if(Objects.isNull(enchants)) return;
        String str;
        if(enchants.size() == 1) {
            Enchantment ench = enchants.keySet().iterator().next();
            if(Objects.nonNull(ench)) {
                list.add(SilentGems.i18n.subText(dimhoppertweaks$cast(),"maxLevel",ench.getMaxLevel()));
                if (KeyTracker.isControlDown()) {
                    list.add(SilentGems.i18n.subText(dimhoppertweaks$cast(), "materials"));
                    String recipeString = RECIPE_MAP.get(ench);
                    if (recipeString != null && !recipeString.isEmpty()) {
                        String[] var8 = recipeString.split(";");
                        for (String s : var8) {
                            str = s;
                            list.add("  " + str);
                        }
                    }
                } else list.add(SilentGems.i18n.subText(dimhoppertweaks$cast(),"pressCtrl"));
                if (KeyTracker.isAltDown()) {
                    ResourceLocation registryName = ench.getRegistryName();
                    if(Objects.nonNull(registryName))
                        list.add(TextFormatting.DARK_GRAY+(registryName).toString());
                }
            }
        }
        for(Map.Entry<Enchantment, Integer> enchantmentIntegerEntry : enchants.entrySet()) {
            Enchantment e = enchantmentIntegerEntry.getKey();
            String enchName = e.getTranslatedName(enchantmentIntegerEntry.getValue());
            ResourceLocation registryName = e.getRegistryName();
            if(Objects.nonNull(registryName)) {
                str = Loader.instance().getIndexedModList().get(registryName.getNamespace()).getName();
                list.add(SilentGems.i18n.subText(dimhoppertweaks$cast(), "enchNameWithMod", enchName, str));
                String descKey = e.getName().replaceAll(":", ".").toLowerCase(Locale.ROOT) + ".desc";
                String desc = SilentGems.i18n.translate(descKey);
                if(!desc.equals(descKey)) list.add(TextFormatting.ITALIC + "  " + desc);
            }
        }

    }
}
