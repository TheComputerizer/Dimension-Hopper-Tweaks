package mods.thecomputerizer.dimhoppertweaks.mixin.mods.blueskies;

import com.legacy.blue_skies.entities.hostile.boss.EntityAlchemist;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static net.minecraft.entity.SharedMonsterAttributes.MAX_HEALTH;

@Mixin(EntityAlchemist.class)
public abstract class MixinEntityAlchemist extends EntityMob {

    public MixinEntityAlchemist(World world) {
        super(world);
    }

    @ModifyConstant(constant = @Constant(floatValue = 500f, ordinal = 0), method = "onUpdate")
    private float dimhoppertweaks$fixHealthHardcoding1(float constant) {
        return (float)this.getEntityAttribute(MAX_HEALTH).getAttributeValue();
    }

    @ModifyConstant(constant = @Constant(floatValue = 500f, ordinal = 1), method = "onUpdate")
    private float dimhoppertweaks$fixHealthHardcoding2(float constant) {
        return (float)this.getEntityAttribute(MAX_HEALTH).getAttributeValue();
    }
}