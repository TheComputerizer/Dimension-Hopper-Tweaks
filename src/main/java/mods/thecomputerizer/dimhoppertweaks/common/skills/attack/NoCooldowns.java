package mods.thecomputerizer.dimhoppertweaks.common.skills.attack;

import mods.thecomputerizer.dimhoppertweaks.common.skills.ExtendedEventsTrait;

public class NoCooldowns extends ExtendedEventsTrait {

    public NoCooldowns() {
        super("no_cooldowns",1,0,ATTACK,112,"attack|256","agility|256");
    }

    @Override
    public boolean shouldCancelNoDamiThresholds() {
        return true;
    }
}
