package mods.thecomputerizer.dimhoppertweaks.registry.traits.mining;

import mods.thecomputerizer.dimhoppertweaks.registry.traits.ExtendedEventsTrait;

public class ArbitraryUpgrade extends ExtendedEventsTrait {
    
    public ArbitraryUpgrade() {
        super("arbitrary_upgrade",4,0,MINING,2,"mining|1024");
        setIcon("bedrockcraft","items","bedrock_pickaxe");
    }
}
