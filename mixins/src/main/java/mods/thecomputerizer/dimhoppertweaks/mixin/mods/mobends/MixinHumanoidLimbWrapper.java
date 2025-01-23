package mods.thecomputerizer.dimhoppertweaks.mixin.mods.mobends;

import goblinbob.mobends.core.client.model.ModelPart;
import goblinbob.mobends.standard.client.model.armor.HumanoidLimbWrapper;
import goblinbob.mobends.standard.client.model.armor.IPartWrapper;
import mods.thecomputerizer.dimhoppertweaks.client.model.ModelConstructsArmorWrapper;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.IHumanoidPart;
import net.minecraft.client.model.ModelRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = HumanoidLimbWrapper.class, remap = false)
public abstract class MixinHumanoidLimbWrapper implements IHumanoidPart {

    @Shadow @Final private ModelRenderer vanillaPart;
    @Shadow protected IPartWrapper.ModelPartSetter modelPartSetter;
    @Shadow private ModelPart upperPart;

    @Override
    public void dimhoppertweaks$apply(ModelConstructsArmorWrapper wrapper) {
        this.modelPartSetter.replacePart(wrapper,this.upperPart);
        this.modelPartSetter.replacePart(wrapper.getOriginal(),this.upperPart);
        this.upperPart.isHidden = this.vanillaPart.isHidden;
        this.upperPart.showModel = this.vanillaPart.showModel;
    }

    @Override
    public void dimhoppertweaks$deapply(ModelConstructsArmorWrapper wrapper) {
        this.modelPartSetter.replacePart(wrapper,this.vanillaPart);
        this.modelPartSetter.replacePart(wrapper.getOriginal(),this.vanillaPart);
        this.vanillaPart.isHidden = this.upperPart.isHidden;
        this.vanillaPart.showModel = this.upperPart.showModel;
    }
}