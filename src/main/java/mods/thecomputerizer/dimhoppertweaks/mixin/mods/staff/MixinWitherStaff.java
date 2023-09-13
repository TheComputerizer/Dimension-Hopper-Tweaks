package mods.thecomputerizer.dimhoppertweaks.mixin.mods.staff;

import net.tslat.aoa3.item.weapon.staff.WitherStaff;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = WitherStaff.class, remap = false)
public class MixinWitherStaff {

    /**
     * @author The_Computerizer
     * @reason Increase base staff damage
     */
    @Overwrite
    public float getDmg() {
        return 25f;
    }
}
