package mods.thecomputerizer.dimhoppertweaks.registry.traits.attack;

import mods.thecomputerizer.dimhoppertweaks.registry.traits.ExtendedEventsTrait;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;

public class LookAtMe extends ExtendedEventsTrait {

    public LookAtMe() {
        super("look_at_me",2,1,ATTACK,40,"attack|96","void|64","magic|32");
        setIcon("items", "ender_eye");
    }

    @Override
    public void onSetTargetToTamed(EntityPlayer player, EntityLiving attacker) {
        attacker.setAttackTarget(player);
    }
}
