package mods.thecomputerizer.dimhoppertweaks.common.skills;

import codersafterdark.reskillable.api.unlockable.Trait;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public abstract class ExtendedEventsTrait extends Trait {

    public ExtendedEventsTrait(ResourceLocation name, int x, int y, ResourceLocation skillName, int cost, String... requirements) {
        super(name,x,y,skillName,cost,requirements);
    }

    public void onChangeDimensions(PlayerEvent.PlayerChangedDimensionEvent ev) {}

    public void onExplosionDetonate(ExplosionEvent.Detonate ev) {}

    public void onLivingKnockback(LivingKnockBackEvent ev) {}

    public void onLootingLevel(LootingLevelEvent ev) {}

    public void onTamedHurt(LivingHurtEvent ev) {}

    public void onTamedDamageOther(LivingDamageEvent ev) {}

    @SuppressWarnings("deprecation")
    public void onBlockPlaced(BlockEvent.PlaceEvent ev) {}

    public boolean shouldCancelNoDamiThresholds() {
        return false;
    }
}
