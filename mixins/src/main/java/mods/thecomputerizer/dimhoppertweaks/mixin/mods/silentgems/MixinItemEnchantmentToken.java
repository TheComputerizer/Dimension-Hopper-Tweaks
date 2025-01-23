package mods.thecomputerizer.dimhoppertweaks.mixin.mods.silentgems;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.silentchaos512.gems.client.key.KeyTracker;
import net.silentchaos512.gems.item.ItemEnchantmentToken;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Locale.ROOT;
import static net.minecraft.util.text.TextFormatting.DARK_GRAY;
import static net.minecraft.util.text.TextFormatting.ITALIC;
import static net.silentchaos512.gems.SilentGems.i18n;

@ParametersAreNonnullByDefault
@Mixin(value = ItemEnchantmentToken.class, remap = false)
public abstract class MixinItemEnchantmentToken extends Item {

    @Shadow
    private static Map<Enchantment,Integer> getEnchantmentMap(ItemStack stack) {
        return null;
    }

    @Shadow @Final private static Map<Enchantment,String> RECIPE_MAP;

    @Unique
    ItemEnchantmentToken dimhoppertweaks$cast() {
        return (ItemEnchantmentToken)(Object)this;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag) {
        if(stack.isEmpty()) return;
        Map<Enchantment,Integer> enchants = getEnchantmentMap(stack);
        if(Objects.isNull(enchants)) return;
        ItemEnchantmentToken instance = dimhoppertweaks$cast();
        String str;
        if(enchants.size()==1) {
            Enchantment ench = enchants.keySet().iterator().next();
            if(Objects.nonNull(ench)) {
                list.add(i18n.subText(instance,"maxLevel",ench.getMaxLevel()));
                if(KeyTracker.isControlDown()) {
                    list.add(i18n.subText(instance, "materials"));
                    String recipeString = RECIPE_MAP.get(ench);
                    if(Objects.nonNull(recipeString) && !recipeString.isEmpty()) {
                        String[] var8 = recipeString.split(";");
                        for(String s : var8) {
                            str = s;
                            list.add("  "+str);
                        }
                    }
                } else list.add(i18n.subText(instance,"pressCtrl"));
                if(KeyTracker.isAltDown()) {
                    ResourceLocation registryName = ench.getRegistryName();
                    if(Objects.nonNull(registryName)) list.add(DARK_GRAY+(registryName).toString());
                }
            }
        }
        for(Map.Entry<Enchantment,Integer> enchantmentIntegerEntry : enchants.entrySet()) {
            Enchantment e = enchantmentIntegerEntry.getKey();
            String enchName = e.getTranslatedName(enchantmentIntegerEntry.getValue());
            ResourceLocation registryName = e.getRegistryName();
            if(Objects.nonNull(registryName)) {
                String modid = registryName.getNamespace();
                if(Loader.isModLoaded(modid)) {
                    str = Loader.instance().getIndexedModList().get(modid).getName();
                    list.add(i18n.subText(instance,"enchNameWithMod",enchName,str));
                    String descKey = e.getName().replaceAll(":",".").toLowerCase(ROOT)+".desc";
                    String desc = i18n.translate(descKey);
                    if(!desc.equals(descKey)) list.add(ITALIC+"  "+desc);
                }
            }
        }
    }
}