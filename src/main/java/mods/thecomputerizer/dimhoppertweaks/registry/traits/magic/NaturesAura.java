package mods.thecomputerizer.dimhoppertweaks.registry.traits.magic;

import mods.thecomputerizer.dimhoppertweaks.registry.traits.ExtendedEventsTrait;
import net.minecraft.util.ResourceLocation;

public class NaturesAura extends ExtendedEventsTrait {

    public NaturesAura() {
        super("natures_aura",3,0,MAGIC,256,"magic|640","void|320","farming|320");
        setIcon(new ResourceLocation("naturesaura","textures/items/aura_trove.png"));
    }
}
