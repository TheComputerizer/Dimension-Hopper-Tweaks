package mods.thecomputerizer.dimhoppertweaks.common.skills.attack;

import mods.thecomputerizer.dimhoppertweaks.common.skills.ExtendedEventsTrait;
import mods.thecomputerizer.dimhoppertweaks.common.skills.SkillWrapper;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.Objects;

public class SuperPets extends ExtendedEventsTrait {

    public SuperPets() {
        super("super_pets",3,0,ATTACK,20,"attack|64","defense|32");
    }

    @Override
    public void onTamedHurt(LivingHurtEvent ev) {
        DamageSource source = ev.getSource();
        if(source==DamageSource.FALL || source==DamageSource.IN_FIRE || source==DamageSource.ON_FIRE ||
                source==DamageSource.LAVA || source==DamageSource.CACTUS || source==DamageSource.FALLING_BLOCK ||
                source==DamageSource.HOT_FLOOR || source==DamageSource.CRAMMING || source==DamageSource.DROWN ||
                source==DamageSource.LIGHTNING_BOLT || source==DamageSource.WITHER) {
            ev.setCanceled(true);
        }
    }

    @Override
    public void onTamedDamageOther(LivingDamageEvent ev) {
        EntityTameable tameable = (EntityTameable)ev.getSource().getTrueSource();
        if(Objects.nonNull(tameable) && Objects.nonNull(tameable.getOwner())) {
            double prestigeFactor = SkillWrapper.getPrestigeFactor((EntityPlayer)tameable.getOwner(),"attack");
            ev.setAmount(ev.getAmount()*(float)((prestigeFactor-1d)*3d));
        }
    }
}
