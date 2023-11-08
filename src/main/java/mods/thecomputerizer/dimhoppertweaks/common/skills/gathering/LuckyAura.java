package mods.thecomputerizer.dimhoppertweaks.common.skills.gathering;

import mods.thecomputerizer.dimhoppertweaks.common.skills.ExtendedEventsTrait;
import mods.thecomputerizer.dimhoppertweaks.common.skills.SkillWrapper;
import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LootingLevelEvent;

public class LuckyAura extends ExtendedEventsTrait {

    public LuckyAura() {
        super(Constants.res("lucky_aura"),3,0,new ResourceLocation("reskillable","gathering"),
                16,"reskillable:gathering|40","reskillable:magic|32");
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
