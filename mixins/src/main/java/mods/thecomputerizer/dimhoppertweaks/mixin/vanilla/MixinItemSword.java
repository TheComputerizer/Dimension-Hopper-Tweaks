package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

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
        return damage==250f ? 1000f : damage+3f; //Kinda dumb check
    }
}