package mods.thecomputerizer.dimhoppertweaks.mixin.mods.conarm;

import c4.conarm.client.models.ModelArmorBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = ModelArmorBase.class, remap = false)
public interface MixinModelArmorBase {

    @Accessor EntityEquipmentSlot getSlot();
}
