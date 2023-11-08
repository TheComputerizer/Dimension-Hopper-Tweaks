package mods.thecomputerizer.dimhoppertweaks.common.skills.magic;

import mods.thecomputerizer.dimhoppertweaks.common.skills.ExtendedEventsTrait;
import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import net.minecraft.util.ResourceLocation;

public class NaturesAura extends ExtendedEventsTrait {

    public NaturesAura() {
        super(Constants.res("natures_aura"),3,0,new ResourceLocation("reskillable","magic"),
                256,"reskillable:magic|640","dimhoppertweaks:void|320","reskillable:farming|320");
    }
}
