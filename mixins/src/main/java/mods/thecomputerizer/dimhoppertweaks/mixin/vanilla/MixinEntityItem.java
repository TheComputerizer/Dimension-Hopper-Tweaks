package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.mixin.api.IEntity;
import net.minecraft.entity.item.EntityItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(EntityItem.class)
public abstract class MixinEntityItem {

    @Unique
    private EntityItem dimhoppertweaks$cast() {
        return (EntityItem)(Object)this;
    }

    @ModifyConstant(constant = @Constant(doubleValue = 0.03999999910593033D), method = "onUpdate")
    private double dimhoppertweaks$applyGravityFactor(double gravity) {
        return gravity*((IEntity)dimhoppertweaks$cast()).dimhoppertweaks$getGravityFactor();
    }
}