package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(EntityWolf.class)
public abstract class MixinEntityWolf extends EntityTameable {

    public MixinEntityWolf(World world) {
        super(world);
    }

    @ModifyConstant(constant = @Constant(floatValue = 20f, ordinal = 0), method = "processInteract")
    private float dimhoppertweaks$uncapHealing(float constant) {
        return (float)this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue();
    }

    /*
    @ModifyConstant(constant = @Constant(floatValue = 20f, ordinal = 1), method = "processInteract")
    private float dimhoppertweaks$uncapTamedHealth(float constant) {
        return (float)this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue();
    }
     */
}