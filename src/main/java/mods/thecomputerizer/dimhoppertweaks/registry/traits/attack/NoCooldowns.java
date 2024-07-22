package mods.thecomputerizer.dimhoppertweaks.registry.traits.attack;

import mods.thecomputerizer.dimhoppertweaks.registry.traits.ExtendedEventsTrait;

public class NoCooldowns extends ExtendedEventsTrait {

    public NoCooldowns() {
        super("no_cooldowns",1,0,ATTACK,112,"attack|512","agility|512");
        setIcon("botania","items","life_essence");
    }

    @Override
    public boolean shouldCancelNoDamiThresholds() {
        return true;
    }
}
