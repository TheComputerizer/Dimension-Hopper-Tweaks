package mods.thecomputerizer.dimhoppertweaks.mixin.mods.mobends;

import goblinbob.mobends.standard.client.model.armor.PartContainer;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.PartContainerAccess;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = PartContainer.class, remap = false)
public abstract class MixinPartContainer extends ModelRenderer implements PartContainerAccess {

    public MixinPartContainer(ModelBase model, String boxName) {
        super(model,boxName);
    }

    @Override
    public void dimhoppertweaks$setShowModel(boolean isVisible) {
        this.showModel = isVisible;
    }
}
