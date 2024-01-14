package mods.thecomputerizer.dimhoppertweaks.mixin.mods.conarm;

import c4.conarm.client.models.ModelArmorBase;
import c4.conarm.client.models.ModelConstructsArmor;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.ModelConstructsArmorAccess;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@SuppressWarnings("DataFlowIssue")
@Mixin(value = ModelConstructsArmor.class, remap = false)
public abstract class MixinModelConstructsArmor extends ModelArmorBase implements ModelConstructsArmorAccess {
    @Shadow public ModelRenderer headAnchor;

    @Shadow public ModelRenderer chestAnchor;

    @Shadow public ModelRenderer armRightAnchor;

    @Shadow public ModelRenderer armLeftAnchor;

    @Shadow public ModelRenderer pantsAnchor;

    @Shadow public ModelRenderer legLeftAnchor;

    @Shadow public ModelRenderer legRightAnchor;

    @Shadow public ModelRenderer bootLeftAnchor;

    @Shadow public ModelRenderer bootRightAnchor;

    public MixinModelConstructsArmor(EntityEquipmentSlot slot) {
        super(slot);
    }

    @Override
    public void dimhoppertweaks$assignRenders() {
        this.headAnchor.showModel = this.slot == EntityEquipmentSlot.HEAD;
        this.chestAnchor.showModel = this.slot == EntityEquipmentSlot.CHEST;
        this.armRightAnchor.showModel = this.slot == EntityEquipmentSlot.CHEST;
        this.armLeftAnchor.showModel = this.slot == EntityEquipmentSlot.CHEST;
        this.pantsAnchor.showModel = this.slot == EntityEquipmentSlot.LEGS;
        this.legLeftAnchor.showModel = this.slot == EntityEquipmentSlot.LEGS;
        this.legRightAnchor.showModel = this.slot == EntityEquipmentSlot.LEGS;
        this.bootLeftAnchor.showModel = this.slot == EntityEquipmentSlot.FEET;
        this.bootRightAnchor.showModel = this.slot == EntityEquipmentSlot.FEET;
        this.bipedHeadwear.showModel = false;
        this.bipedHead = this.headAnchor;
        this.bipedBody = this.chestAnchor;
        this.bipedRightArm = this.armRightAnchor;
        this.bipedLeftArm = this.armLeftAnchor;
        if (this.slot == EntityEquipmentSlot.LEGS) {
            this.bipedBody = this.pantsAnchor;
            this.bipedLeftLeg = this.legLeftAnchor;
            this.bipedRightLeg = this.legRightAnchor;
        } else {
            this.bipedLeftLeg = this.bootLeftAnchor;
            this.bipedRightLeg = this.bootRightAnchor;
        }
    }

    @Override
    public void dimhoppertweaks$render(@Nullable Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity,f,f1,f2,f3,f4,f5);
    }
}
