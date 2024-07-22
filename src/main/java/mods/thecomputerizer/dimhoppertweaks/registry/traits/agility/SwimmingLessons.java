package mods.thecomputerizer.dimhoppertweaks.registry.traits.agility;

import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import mods.thecomputerizer.dimhoppertweaks.registry.traits.ExtendedEventsTrait;

public class SwimmingLessons extends ExtendedEventsTrait {
    
    public SwimmingLessons() {
        super("swimming_lessons",3,2,AGILITY,18,"agility|48");
        setIcon(DHTRef.res("textures/unlockables/swimming_lessons.png"));
    }
}