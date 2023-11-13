package mods.thecomputerizer.dimhoppertweaks.registry.traits.research;

import mods.thecomputerizer.dimhoppertweaks.registry.traits.ExtendedEventsTrait;
import net.minecraft.util.ResourceLocation;

public class LimbonicGenerator extends ExtendedEventsTrait {

    public LimbonicGenerator() {
        super("limbonic_generator",4,2,RESEARCH,384,"research|992","void|992","magic|512");
        setIcon(new ResourceLocation("dimdoors","textures/other/limbo_sun.png"));
    }
}
