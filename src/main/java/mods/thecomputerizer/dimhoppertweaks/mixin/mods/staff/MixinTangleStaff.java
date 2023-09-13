package mods.thecomputerizer.dimhoppertweaks.mixin.mods.staff;

import net.tslat.aoa3.item.weapon.staff.TangleStaff;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = TangleStaff.class, remap = false)
public class MixinTangleStaff {

    /**
     * @author The_Computerizer
     * @reason Increase base staff damage
     */
    @Overwrite
    public float getDmg() {
        return 45f;
    }
}
