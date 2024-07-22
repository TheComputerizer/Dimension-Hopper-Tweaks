package mods.thecomputerizer.dimhoppertweaks.registry.traits.voidtraits;

import mods.thecomputerizer.dimhoppertweaks.registry.traits.ExtendedEventsTrait;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;

import static net.minecraft.init.MobEffects.SPEED;

public class VoidWalker extends ExtendedEventsTrait {

    public VoidWalker() {
        super("void_walker",2,1,VOID,8,"void|16","agility|32");
    }

    @Override
    public void onChangeDimensions(PlayerChangedDimensionEvent ev) {
        double prestigeFactor = SkillWrapper.getPrestigeFactor(ev.player,"void");
        int duration = (int)(300d*prestigeFactor);
        int amplifier = MathHelper.floor(prestigeFactor/4d);
        ev.player.addPotionEffect(new PotionEffect(SPEED,duration,amplifier));
    }
}
