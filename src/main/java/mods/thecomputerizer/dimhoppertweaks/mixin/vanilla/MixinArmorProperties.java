package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.common.skills.SkillWrapper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.ISpecialArmor.ArmorProperties;
import net.minecraftforge.common.util.FakePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ArmorProperties.class, remap = false)
public class MixinArmorProperties {

    @Unique private static double dimhoppertweaks$prestigeFactor = 0d;

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;getTotalArmorValue()I"),
            method = "applyArmor")
    private static int dimhoppertweaks$armorFactor(EntityLivingBase entity) {
        double baseArmor = entity.getTotalArmorValue();
        if(!(entity instanceof EntityPlayer) || entity instanceof FakePlayer) {
            dimhoppertweaks$prestigeFactor = 0d;
            return(int)baseArmor;
        }
        dimhoppertweaks$prestigeFactor = 1d-SkillWrapper.getPrestigeFactor((EntityPlayer)entity,"defense");
        return (int)(baseArmor*(0.1d*dimhoppertweaks$prestigeFactor));
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/attributes/IAttributeInstance;"+
            "getAttributeValue()D"),method = "applyArmor")
    private static double dimhoppertweaks$toughnessFactor(IAttributeInstance attribute) {
        double original = attribute.getAttributeValue();
        return original+(dimhoppertweaks$prestigeFactor*original*0.05d);
    }
}
