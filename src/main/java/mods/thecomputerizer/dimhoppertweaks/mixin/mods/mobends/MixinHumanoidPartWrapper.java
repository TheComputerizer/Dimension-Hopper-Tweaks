package mods.thecomputerizer.dimhoppertweaks.mixin.mods.mobends;

import goblinbob.mobends.standard.client.model.armor.HumanoidPartWrapper;
import goblinbob.mobends.standard.client.model.armor.IPartWrapper;
import goblinbob.mobends.standard.client.model.armor.PartContainer;
import mods.thecomputerizer.dimhoppertweaks.client.model.ModelConstructsArmorWrapper;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.HumanoidPartAccess;
import net.minecraft.client.model.ModelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = HumanoidPartWrapper.class, remap = false)
public abstract class MixinHumanoidPartWrapper implements HumanoidPartAccess {

    @Shadow protected IPartWrapper.ModelPartSetter modelPartSetter;

    @Shadow protected PartContainer partContainer;

    @Shadow protected ModelRenderer vanillaPart;

    @Override
    public void dimhoppertweaks$apply(ModelConstructsArmorWrapper wrapper) {
        this.modelPartSetter.replacePart(wrapper, this.partContainer);
        this.modelPartSetter.replacePart(wrapper.getOriginal(),this.partContainer);
        this.partContainer.isHidden = this.vanillaPart.isHidden;
        this.partContainer.showModel = this.vanillaPart.showModel;
    }

    @Override
    public void dimhoppertweaks$deapply(ModelConstructsArmorWrapper wrapper) {
        this.modelPartSetter.replacePart(wrapper,this.vanillaPart);
        this.modelPartSetter.replacePart(wrapper.getOriginal(),this.vanillaPart);
        this.vanillaPart.isHidden = this.partContainer.isHidden;
        this.vanillaPart.showModel = this.partContainer.showModel;
    }
}
