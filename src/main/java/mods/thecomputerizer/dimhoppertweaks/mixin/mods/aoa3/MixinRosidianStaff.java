package mods.thecomputerizer.dimhoppertweaks.mixin.mods.aoa3;

import net.tslat.aoa3.item.weapon.staff.RosidianStaff;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = RosidianStaff.class, remap = false)
public class MixinRosidianStaff {

    /**
     * @author The_Computerizer
     * @reason Increase base staff damage
     */
    @Overwrite
    public float getDmg() {
        return 52.5f;
    }
}