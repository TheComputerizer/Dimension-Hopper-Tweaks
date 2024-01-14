package mods.thecomputerizer.dimhoppertweaks.mixin.mods.mobends;

import c4.conarm.client.models.ModelConstructsArmor;
import goblinbob.mobends.core.data.EntityData;
import goblinbob.mobends.core.data.EntityDatabase;
import goblinbob.mobends.standard.client.model.armor.ArmorModelFactory;
import goblinbob.mobends.standard.client.model.armor.MalformedArmorModelException;
import goblinbob.mobends.standard.client.renderer.entity.layers.LayerCustomBipedArmor;
import goblinbob.mobends.standard.data.BipedEntityData;
import goblinbob.mobends.standard.main.ModConfig;
import mods.thecomputerizer.dimhoppertweaks.client.model.ModelConstructsArmorWrapper;
import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = LayerCustomBipedArmor.class, remap = false)
public class MixinLayerCustomBipedArmor {

    /**
     * @author The_Computerizer
     * @reason Tinkers armor
     */
    @Overwrite
    protected ModelBiped getArmorModelHook(EntityLivingBase entity, ItemStack stack, EntityEquipmentSlot slot, ModelBiped model) {
        EntityData<?> data = EntityDatabase.instance.get(entity);
        ModelBiped suggestedModel = ForgeHooksClient.getArmorModel(entity,stack,slot,model);
        boolean shouldBeMutated = !ModConfig.shouldKeepArmorAsVanilla(stack.getItem()) && data instanceof BipedEntityData;
        try {
            return suggestedModel instanceof ModelConstructsArmor ?
                    ModelConstructsArmorWrapper.getInstance((ModelConstructsArmor)suggestedModel,slot) :
                    ArmorModelFactory.getArmorModel(suggestedModel,shouldBeMutated);
        } catch (MalformedArmorModelException ex) {
            DHTRef.LOGGER.error("Malformed armor model or something",ex);
            return suggestedModel;
        }
    }
}
