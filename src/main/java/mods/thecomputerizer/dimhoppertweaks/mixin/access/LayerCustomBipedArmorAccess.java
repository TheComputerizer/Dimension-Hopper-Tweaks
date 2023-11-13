package mods.thecomputerizer.dimhoppertweaks.mixin.access;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.inventory.EntityEquipmentSlot;

public interface LayerCustomBipedArmorAccess {

    ModelBiped dimhoppertweaks$getOverlay(ModelBiped model);
    ModelBiped dimhoppertweaks$getInvulnOverlay(ModelBiped model);
    void dimhoppertweaks$setBetterVisibility(ModelBiped model, ModelBiped model1, ModelBiped model2, EntityEquipmentSlot slot);
}
