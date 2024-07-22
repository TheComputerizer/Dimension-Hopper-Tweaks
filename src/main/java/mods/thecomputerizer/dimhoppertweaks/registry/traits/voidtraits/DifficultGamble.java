package mods.thecomputerizer.dimhoppertweaks.registry.traits.voidtraits;

import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import mods.thecomputerizer.dimhoppertweaks.registry.traits.ExtendedEventsTrait;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LootingLevelEvent;

public class DifficultGamble extends ExtendedEventsTrait {
    
    public DifficultGamble() {
        super("difficult_gamble",4,1,VOID,16,"void|64","research|64","magic|64");
        setIcon("scalinghealth","items","cursed_heart");
    }
    
    @Override public void onLootingLevel(LootingLevelEvent event) {
        Entity entity = event.getDamageSource().getTrueSource();
        if(entity instanceof EntityPlayerMP) {
            int level = MathHelper.floor(SkillWrapper.getDifficultyFactor((EntityPlayer)entity));
            event.setLootingLevel(event.getLootingLevel()+level);
        }
    }
    
    @Override public void onXPPickup(EntityPlayer player, EntityXPOrb xp) {
        if(player instanceof EntityPlayerMP) {
            double increase = SkillWrapper.getDifficultyFactor(player);
            xp.xpValue = (int)((double)xp.xpValue*increase);
        }
    }
}