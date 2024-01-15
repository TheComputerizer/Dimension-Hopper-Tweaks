package mods.thecomputerizer.dimhoppertweaks.mixin.mods.aoa3;

import net.tslat.aoa3.item.weapon.staff.FireStaff;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = FireStaff.class, remap = false)
public abstract class MixinFireStaff {

    /**
     * @author The_Computerizer
     * @reason Increase base staff damage
     */
    @Overwrite
    public float getDmg() {
        return 25f;
    }
}