package mods.thecomputerizer.dimhoppertweaks.mixin.mods.infernalmobs;

import atomicstryker.infernalmobs.common.InfernalMobsCore;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Random;

import static net.minecraftforge.fml.common.registry.ForgeRegistries.ENCHANTMENTS;

@Mixin(value = InfernalMobsCore.class, remap = false)
public abstract class MixinInfernalMobsCore {

    @Shadow private static InfernalMobsCore instance;

    /**
     * @author The_Computerizer
     * @reason Only give level 1 shimmer
     */
    @Overwrite
    private EnchantmentData getRandomEnchantment(Random rand) {
        Enchantment shimmer = dimhoppertweaks$getShimmerEnchant();
        return Objects.nonNull(shimmer) ? new EnchantmentData(shimmer,1) : null;
    }

    /**
     * @author The_Computerizer
     * @reason Only give level 1 shimmer
     */
    @Overwrite
    private void enchantRandomly(Random rand, ItemStack stack, int itemEnchantability, int modStr) {
        if((modStr+1)/2>0) {
            Enchantment shimmer = dimhoppertweaks$getShimmerEnchant();
            if(Objects.nonNull(shimmer)) stack.addEnchantment(shimmer,1);
        }
    }

    @Unique
    private @Nullable Enchantment dimhoppertweaks$getShimmerEnchant() {
        ResourceLocation res = new ResourceLocation("enderio","shimmer");
        return ENCHANTMENTS.containsKey(res) ? ENCHANTMENTS.getValue(res) : null;
    }
}