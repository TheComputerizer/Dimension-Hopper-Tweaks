package mods.thecomputerizer.dimhoppertweaks.registry.traits.gathering;

import mods.thecomputerizer.dimhoppertweaks.registry.traits.ExtendedEventsTrait;
import mods.thecomputerizer.dimhoppertweaks.common.capability.SkillWrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LootingLevelEvent;

public class LuckyAura extends ExtendedEventsTrait {

    public LuckyAura() {
        super("lucky_aura",3,0,GATHERING,16,"gathering|40","magic|32");
    }

    @Override
    public void onLootingLevel(LootingLevelEvent event) {
        Entity entity = event.getDamageSource().getTrueSource();
        if(entity instanceof EntityPlayerMP) {
            double prestigeFactor = SkillWrapper.getPrestigeFactor((EntityPlayerMP)entity,"gathering");
            event.setLootingLevel(event.getLootingLevel()+MathHelper.floor((prestigeFactor-1d)*2d));
        }
    }
}
