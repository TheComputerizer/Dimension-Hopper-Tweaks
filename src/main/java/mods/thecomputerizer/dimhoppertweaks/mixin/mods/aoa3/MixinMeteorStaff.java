package mods.thecomputerizer.dimhoppertweaks.mixin.mods.aoa3;

import net.tslat.aoa3.item.weapon.staff.MeteorStaff;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = MeteorStaff.class, remap = false)
public class MixinMeteorStaff {

    /**
     * @author The_Computerizer
     * @reason Increase base staff damage
     */
    @Overwrite
    public float getDmg() {
        return 70f;
    }
}
