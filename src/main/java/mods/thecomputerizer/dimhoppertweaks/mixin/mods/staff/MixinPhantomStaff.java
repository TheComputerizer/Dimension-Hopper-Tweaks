package mods.thecomputerizer.dimhoppertweaks.mixin.mods.staff;

import net.tslat.aoa3.item.weapon.staff.PhantomStaff;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = PhantomStaff.class, remap = false)
public class MixinPhantomStaff {

    /**
     * @author The_Computerizer
     * @reason Increase base staff damage
     */
    @Overwrite
    public float getDmg() {
        return 60f;
    }
}
