package mods.thecomputerizer.dimhoppertweaks.mixin.mods.aoa3;

import net.tslat.aoa3.item.weapon.staff.WindStaff;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = WindStaff.class, remap = false)
public class MixinWindStaff {

    /**
     * @author The_Computerizer
     * @reason Increase base staff damage
     */
    @Overwrite
    public float getDmg() {
        return 25f;
    }
}
