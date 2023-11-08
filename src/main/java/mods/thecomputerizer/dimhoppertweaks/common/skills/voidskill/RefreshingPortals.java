package mods.thecomputerizer.dimhoppertweaks.common.skills.voidskill;

import mods.thecomputerizer.dimhoppertweaks.common.skills.ExtendedEventsTrait;
import mods.thecomputerizer.dimhoppertweaks.common.skills.SkillWrapper;
import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class RefreshingPortals extends ExtendedEventsTrait {

    public RefreshingPortals() {
        super(Constants.res("refreshing_portals"),2,3,Constants.res("void"),8,
                "dimhoppertweaks:void|16","reskillable:magic|32");
    }

    @Override
    public void onChangeDimensions(PlayerEvent.PlayerChangedDimensionEvent ev) {
        double prestigeFactor = SkillWrapper.getPrestigeFactor(ev.player,"void");
        int duration = (int)(300d*prestigeFactor);
        int amplifier = MathHelper.floor(prestigeFactor/4d);
        ev.player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION,duration,amplifier));
        ev.player.addPotionEffect(new PotionEffect(MobEffects.HEALTH_BOOST,duration,amplifier));
    }
}
