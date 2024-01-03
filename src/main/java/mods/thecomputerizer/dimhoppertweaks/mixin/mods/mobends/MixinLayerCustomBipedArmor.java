package mods.thecomputerizer.dimhoppertweaks.mixin.mods.mobends;

import goblinbob.mobends.standard.client.renderer.entity.layers.LayerCustomBipedArmor;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.LayerCustomBipedArmorAccess;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.ModelArmorInfinityAccess;
import morph.avaritia.client.render.entity.ModelArmorInfinity;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nonnull;

@Mixin(value = LayerCustomBipedArmor.class, remap = false)
public abstract class MixinLayerCustomBipedArmor extends LayerArmorBase<ModelBiped> implements LayerCustomBipedArmorAccess {

    public MixinLayerCustomBipedArmor(RenderLivingBase<?> renderer) {
        super(renderer);
    }

    @Shadow protected abstract void hideModelParts(ModelBiped model);

    @Override
    public ModelBiped dimhoppertweaks$getOverlay(ModelBiped model) {
        return model instanceof ModelArmorInfinity ? ((ModelArmorInfinityAccess)model).dimhoppertweaks$getOverlay() : null;
    }

    @Override
    public ModelBiped dimhoppertweaks$getInvulnOverlay(ModelBiped model) {
        return model instanceof ModelArmorInfinity ? ((ModelArmorInfinityAccess)model).dimhoppertweaks$getInvulnOverlay() : null;
    }

    @Override
    public void dimhoppertweaks$setBetterVisibility(ModelBiped model, ModelBiped model1, ModelBiped model2,
                                                    @Nonnull EntityEquipmentSlot slot) {
        this.hideModelParts(model);
        this.hideModelParts(model1);
        this.hideModelParts(model2);
        switch(slot) {
            case HEAD:
                model.bipedHead.showModel = true;
                model1.bipedHead.showModel = true;
                model2.bipedHead.showModel = true;
                model.bipedHeadwear.showModel = true;
                model1.bipedHeadwear.showModel = true;
                model2.bipedHeadwear.showModel = true;
                break;
            case CHEST:
                model.bipedBody.showModel = true;
                model1.bipedBody.showModel = true;
                model2.bipedBody.showModel = true;
                model.bipedRightArm.showModel = true;
                model1.bipedRightArm.showModel = true;
                model2.bipedRightArm.showModel = true;
                model.bipedLeftArm.showModel = true;
                model1.bipedLeftArm.showModel = true;
                model2.bipedLeftArm.showModel = true;
                break;
            case LEGS:
                model.bipedBody.showModel = true;
                model1.bipedBody.showModel = true;
                model2.bipedBody.showModel = true;
                model.bipedRightLeg.showModel = true;
                model1.bipedRightLeg.showModel = true;
                model2.bipedRightLeg.showModel = true;
                model.bipedLeftLeg.showModel = true;
                model1.bipedLeftLeg.showModel = true;
                model2.bipedLeftLeg.showModel = true;
                break;
            case FEET:
                model.bipedRightLeg.showModel = true;
                model1.bipedRightLeg.showModel = true;
                model2.bipedRightLeg.showModel = true;
                model.bipedLeftLeg.showModel = true;
                model1.bipedLeftLeg.showModel = true;
                model2.bipedLeftLeg.showModel = true;
        }
    }
}
