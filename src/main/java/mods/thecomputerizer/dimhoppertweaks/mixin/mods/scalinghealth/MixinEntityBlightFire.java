package mods.thecomputerizer.dimhoppertweaks.mixin.mods.scalinghealth;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.silentchaos512.scalinghealth.entity.EntityBlightFire;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = EntityBlightFire.class, remap = false)
public abstract class MixinEntityBlightFire extends Entity {

    public MixinEntityBlightFire(World world) {
        super(world);
    }

    /**
     * @author The_Computerizer
     * @reason Remove any existing blight fire entities from the world since the custom renderer doesn't need them
     */
    @Overwrite
    public void onUpdate() {
        if(this.world.isRemote) this.setDead();
    }
}