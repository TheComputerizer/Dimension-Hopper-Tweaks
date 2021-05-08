package mods.thecomputerizer.dimensionhoppertweaks.common.hook;

import mods.thecomputerizer.dimensionhoppertweaks.common.objects.entity.EntityFinalBoss;
import morph.avaritia.item.tools.ItemSwordInfinity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
public final class AvaritiaInfinitySwordHook {
    private AvaritiaInfinitySwordHook() {}

    // To be called via ASM
    public static boolean handleFinalBossEntityAttack(final ItemSwordInfinity $this, final ItemStack stack, final EntityFinalBoss boss, final EntityLivingBase player) {
        System.out.println("Hooked");
        return true;
    }
}
