package mods.thecomputerizer.dimhoppertweaks.registry.traits.voidtraits;

import mods.thecomputerizer.dimhoppertweaks.registry.traits.ExtendedEventsTrait;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;

import static net.minecraft.init.MobEffects.HEALTH_BOOST;
import static net.minecraft.init.MobEffects.REGENERATION;

public class RefreshingPortals extends ExtendedEventsTrait {

    public RefreshingPortals() {
        super("refreshing_portals",2,3,VOID,8,"void|16","magic|32");
    }

    @Override
    public void onChangeDimensions(PlayerChangedDimensionEvent ev) {
        double prestigeFactor = SkillWrapper.getPrestigeFactor(ev.player,"void");
        int duration = (int)(300d*prestigeFactor);
        int amplifier = MathHelper.floor(prestigeFactor/4d);
        ev.player.addPotionEffect(new PotionEffect(REGENERATION,duration,amplifier));
        ev.player.addPotionEffect(new PotionEffect(HEALTH_BOOST,duration,amplifier));
    }
}
