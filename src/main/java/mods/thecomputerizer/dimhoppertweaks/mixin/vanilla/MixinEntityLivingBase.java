package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.mixin.access.EntityLivinBaseAccess;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityLivingBase.class)
public class MixinEntityLivingBase implements EntityLivinBaseAccess {

    @Unique private boolean dimhoppertweaks$isBlighted;

    @Override
    public void dimhoppertweaks$setBlight(boolean isBlighted) {
        this.dimhoppertweaks$isBlighted = isBlighted;
    }

    @Override
    public boolean dimhoppertweaks$isBlighted() {
        return this.dimhoppertweaks$isBlighted;
    }
}
