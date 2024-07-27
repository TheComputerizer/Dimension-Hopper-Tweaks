package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemSword;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemSword.class)
public class MixinItemSword {
    
    @Redirect(at =@At(value="INVOKE", target="Lnet/minecraft/item/Item$ToolMaterial;getAttackDamage()F"),
            method = "<init>")
    private float dimhoppertweaks$fixDamage(ToolMaterial material) {
        float damage = material.getAttackDamage();
        if(DelayedModAccess.isBedrockMaterial(material)) damage = (damage*4f)-3f;
        return damage;
    }
}