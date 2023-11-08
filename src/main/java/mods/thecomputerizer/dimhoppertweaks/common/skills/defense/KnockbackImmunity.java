package mods.thecomputerizer.dimhoppertweaks.common.skills.defense;

import mods.thecomputerizer.dimhoppertweaks.common.skills.ExtendedEventsTrait;
import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;

public class KnockbackImmunity extends ExtendedEventsTrait {

    public KnockbackImmunity() {
        super(Constants.res("knockback_immunity"),0,0,new ResourceLocation("reskillable","defense"),
                40,"reskillable:defense|128");
    }

    @Override
    public void onLivingKnockback(LivingKnockBackEvent ev) {
        ev.setCanceled(true);
    }
}
