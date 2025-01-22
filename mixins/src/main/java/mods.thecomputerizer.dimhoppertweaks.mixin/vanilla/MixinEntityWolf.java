package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static net.minecraft.entity.SharedMonsterAttributes.MAX_HEALTH;

@Mixin(EntityWolf.class)
public abstract class MixinEntityWolf extends EntityTameable {

    public MixinEntityWolf(World world) {
        super(world);
    }

    @ModifyConstant(constant = @Constant(floatValue = 20f, ordinal = 0), method = "processInteract")
    private float dimhoppertweaks$uncapHealing(float constant) {
        return (float)this.getEntityAttribute(MAX_HEALTH).getAttributeValue();
    }
}