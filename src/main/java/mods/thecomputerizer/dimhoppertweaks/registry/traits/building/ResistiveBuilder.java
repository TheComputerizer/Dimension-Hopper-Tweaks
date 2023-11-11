package mods.thecomputerizer.dimhoppertweaks.registry.traits.building;

import mods.thecomputerizer.dimhoppertweaks.registry.traits.ExtendedEventsTrait;
import mods.thecomputerizer.dimhoppertweaks.common.capability.SkillWrapper;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.world.BlockEvent;

public class ResistiveBuilder extends ExtendedEventsTrait {

    public ResistiveBuilder() {
        super("resistive_builder",3,0,BUILDING,24,"building|64","defense|32");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onBlockPlaced(BlockEvent.PlaceEvent ev) {
        double prestigeFactor = SkillWrapper.getPrestigeFactor(ev.getPlayer(),"building");
        int duration = (int)(30d*prestigeFactor);
        int amplifier = MathHelper.floor(prestigeFactor/8d);
        ev.getPlayer().addPotionEffect(new PotionEffect(MobEffects.RESISTANCE,duration,amplifier));
    }
}
