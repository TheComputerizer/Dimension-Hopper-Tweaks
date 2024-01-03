package mods.thecomputerizer.dimhoppertweaks.mixin.mods.aoa3;

import net.tslat.aoa3.item.weapon.staff.FirestormStaff;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = FirestormStaff.class, remap = false)
public class MixinFirestormStaff {

    /**
     * @author The_Computerizer
     * @reason Increase base staff damage
     */
    @Overwrite
    public float getDmg() {
        return 80f;
    }
}
