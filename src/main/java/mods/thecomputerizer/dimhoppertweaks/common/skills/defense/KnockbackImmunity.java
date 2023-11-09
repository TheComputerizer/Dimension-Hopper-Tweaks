package mods.thecomputerizer.dimhoppertweaks.common.skills.defense;

import mods.thecomputerizer.dimhoppertweaks.common.skills.ExtendedEventsTrait;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;

public class KnockbackImmunity extends ExtendedEventsTrait {

    public KnockbackImmunity() {
        super("knockback_immunity",0,0,DEFENSE,40,"defense|128");
    }

    @Override
    public void onLivingKnockback(LivingKnockBackEvent ev) {
        ev.setCanceled(true);
    }
}
