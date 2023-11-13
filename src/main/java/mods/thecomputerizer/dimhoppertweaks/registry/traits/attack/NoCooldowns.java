package mods.thecomputerizer.dimhoppertweaks.registry.traits.attack;

import mods.thecomputerizer.dimhoppertweaks.registry.traits.ExtendedEventsTrait;
import net.minecraft.util.ResourceLocation;

public class NoCooldowns extends ExtendedEventsTrait {

    public NoCooldowns() {
        super("no_cooldowns",1,0,ATTACK,112,"attack|512","agility|512");
        setIcon(new ResourceLocation("botania","textures/items/life_essence.png"));
    }

    @Override
    public boolean shouldCancelNoDamiThresholds() {
        return true;
    }
}
