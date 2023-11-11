package mods.thecomputerizer.dimhoppertweaks.registry.traits.defense;

import mods.thecomputerizer.dimhoppertweaks.registry.traits.ExtendedEventsTrait;
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
