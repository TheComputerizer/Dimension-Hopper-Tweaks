package mods.thecomputerizer.dimhoppertweaks.registry.traits.attack;

import mods.thecomputerizer.dimhoppertweaks.registry.traits.ExtendedEventsTrait;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.Objects;

import static net.minecraft.util.DamageSource.*;

public class SuperPets extends ExtendedEventsTrait {

    public SuperPets() {
        super("super_pets",3,0,ATTACK,20,"attack|64","defense|32");
    }

    @Override
    public void onTamedHurt(LivingHurtEvent ev) {
        DamageSource source = ev.getSource();
        if(source==FALL || source==IN_FIRE || source==ON_FIRE || source==LAVA || source==CACTUS ||
           source==FALLING_BLOCK || source==HOT_FLOOR || source==CRAMMING || source==DROWN || source==LIGHTNING_BOLT ||
           source==WITHER) ev.setCanceled(true);
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
