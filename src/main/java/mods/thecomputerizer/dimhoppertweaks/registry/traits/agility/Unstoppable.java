package mods.thecomputerizer.dimhoppertweaks.registry.traits.agility;

import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import mods.thecomputerizer.dimhoppertweaks.registry.traits.ExtendedEventsTrait;

public class Unstoppable extends ExtendedEventsTrait {
    
    public Unstoppable() {
        super("unstoppable",1,2,AGILITY,12,"agility|64","void|32");
        setIcon(DHTRef.res("textures/unlockables/unstoppable.png"));
    }
}