package mods.thecomputerizer.dimhoppertweaks.registry.traits.mining;

import mods.thecomputerizer.dimhoppertweaks.registry.traits.ExtendedEventsTrait;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class ExpertMiner extends ExtendedEventsTrait {

    public ExpertMiner() {
        super("expert_miner",1,0,MINING, 12,"mining|40");
    }

    @Override
    public void getBreakSpeed(PlayerEvent.BreakSpeed event) {
        event.setNewSpeed(event.getNewSpeed()*2f);
    }
}
