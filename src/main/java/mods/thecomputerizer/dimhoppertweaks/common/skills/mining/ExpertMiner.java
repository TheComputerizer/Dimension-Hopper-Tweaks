package mods.thecomputerizer.dimhoppertweaks.common.skills.mining;

import codersafterdark.reskillable.api.unlockable.Trait;
import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class ExpertMiner extends Trait {

    public ExpertMiner() {
        super(Constants.res("expert_miner"),1,0,new ResourceLocation("reskillable","mining"),
                12,"reskillable:mining|40");
    }

    @Override
    public void getBreakSpeed(PlayerEvent.BreakSpeed event) {
        event.setNewSpeed(event.getNewSpeed()*2f);
    }
}
