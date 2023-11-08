package mods.thecomputerizer.dimhoppertweaks.common.skills.attack;

import mods.thecomputerizer.dimhoppertweaks.common.skills.ExtendedEventsTrait;
import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import net.minecraft.util.ResourceLocation;

public class NoCooldowns extends ExtendedEventsTrait {

    public NoCooldowns() {
        super(Constants.res("no_cooldowns"),1,0,new ResourceLocation("reskillable","attack"),
                112,"reskillable:attack|256","reskillable:agility|256");
    }

    @Override
    public boolean shouldCancelNoDamiThresholds() {
        return true;
    }
}
