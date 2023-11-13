package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.mixin.access.DelayedModAccess;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.LayerCustomBipedArmorAccess;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.*;

@Mixin(LayerArmorBase.class)
public abstract class MixinLayerArmorBase<T extends ModelBase> {

    @Shadow public abstract T getModelFromSlot(EntityEquipmentSlot slot);

    @Shadow(remap = false) protected abstract T getArmorModelHook(EntityLivingBase entity, ItemStack stack, EntityEquipmentSlot slot, T model);

    @Shadow @Final private RenderLivingBase<?> renderer;

    @Shadow protected abstract void setModelSlotVisible(T model, EntityEquipmentSlot slot);

    @Shadow protected abstract boolean isLegSlot(EntityEquipmentSlot slotIn);

    @Shadow(remap = false) public abstract ResourceLocation getArmorResource(Entity entity, ItemStack stack, EntityEquipmentSlot slot, String type);

    @Shadow private float colorR;

    @Shadow private float colorG;

    @Shadow private float colorB;

    @Shadow private float alpha;

    @Shadow private boolean skipRenderGlint;

    @Shadow
    public static void renderEnchantedGlint(RenderLivingBase<?> renderer, EntityLivingBase entity, ModelBase model,
                                            float f1, float f2, float f3, float f4, float f5, float f6, float f7) {
    }

    /**
     * @author The_Computerizer
     * @reason Try to add Mo Bends support for more armor
     */
    @Overwrite
    public void renderArmorLayer(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks,
                                  float ageInTicks, float netHeadYaw, float headPitch, float scale, EntityEquipmentSlot slot) {
        ItemStack stack = entity.getItemStackFromSlot(slot);
        if(stack.getItem() instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor)stack.getItem();
            if(armor.getEquipmentSlot()==slot) {
                T model = this.getModelFromSlot(slot);
                if(DelayedModAccess.isInfinityArmorModel(model) && DelayedModAccess.isMoBendsArmorLayer((LayerArmorBase<?>)(Object)this)) {
                    LayerCustomBipedArmorAccess layer = (LayerCustomBipedArmorAccess)this;
                    ModelBiped model1 = DelayedModAccess.fixOverlay(entity,layer.dimhoppertweaks$getOverlay((ModelBiped)model));
                    ModelBiped model2 = DelayedModAccess.fixOverlay(entity,layer.dimhoppertweaks$getInvulnOverlay((ModelBiped)model));
                    model = getArmorModelHook(entity,stack,slot,model);
                    layer.dimhoppertweaks$setBetterVisibility((ModelBiped)model,model1,model2,slot);
                    dimhoppertweaks$renderArmorModel(entity,limbSwing,limbSwingAmount,partialTicks,ageInTicks,
                            netHeadYaw,headPitch,scale,slot,stack,model,false);
                    dimhoppertweaks$renderArmorModel(entity,limbSwing,limbSwingAmount,partialTicks,ageInTicks,
                            netHeadYaw,headPitch,scale,slot,stack,model1,false);
                    dimhoppertweaks$renderArmorModel(entity,limbSwing,limbSwingAmount,partialTicks,ageInTicks,
                            netHeadYaw,headPitch,scale,slot,stack,model2,false);
                }
                else dimhoppertweaks$renderArmorModel(entity,limbSwing,limbSwingAmount,partialTicks,ageInTicks,
                        netHeadYaw,headPitch,scale,slot,stack,getArmorModelHook(entity,stack,slot,model),true);
            }
        }
    }

    @SuppressWarnings({"unchecked", "unused"})
    @Unique void dimhoppertweaks$renderArmorModel(
            EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw,
            float headPitch, float scale, EntityEquipmentSlot slot, ItemStack stack, ModelBase model, boolean normal) {
        ItemArmor armor = (ItemArmor)stack.getItem();
        model.setModelAttributes(this.renderer.getMainModel());
        model.setLivingAnimations(entity,limbSwing,limbSwingAmount,partialTicks);
        if(normal) this.setModelSlotVisible((T)model,slot);
        boolean isLeg = this.isLegSlot(slot);
        this.renderer.bindTexture(this.getArmorResource(entity,stack,slot,null));{
            if(armor.hasOverlay(stack)) {
                int i = armor.getColor(stack);
                float redFactor = (float)(i >> 16 & 255) / 255f;
                float greenFactor = (float)(i >> 8 & 255) / 255f;
                float blueFactor = (float)(i & 255) / 255.0F;
                GlStateManager.color(this.colorR*redFactor,this.colorG*greenFactor,
                        this.colorB*blueFactor,this.alpha);
                model.render(entity,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scale);
                this.renderer.bindTexture(this.getArmorResource(entity,stack,slot,"overlay"));
            }
            {
                GlStateManager.color(this.colorR,this.colorG,this.colorB,this.alpha);
                model.render(entity,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scale);
            }
            if(!this.skipRenderGlint && stack.hasEffect())
                renderEnchantedGlint(this.renderer,entity,model,limbSwing,limbSwingAmount,partialTicks,ageInTicks,netHeadYaw,headPitch,scale);
        }
    }
}
