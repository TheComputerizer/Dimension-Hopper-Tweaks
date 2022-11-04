package mods.thecomputerizer.dimhoppertweaks.common.objects;

import mods.thecomputerizer.dimhoppertweaks.DimHopperTweaks;
import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.EntityFinalBoss;
import mods.thecomputerizer.dimhoppertweaks.common.objects.entity.HomingProjectile;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

@Mod.EventBusSubscriber(modid = DimHopperTweaks.MODID)
public final class DimensionHopperEntities {
    private static int globalUniqueEntityId = 0;

    private DimensionHopperEntities() {}

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> entities) {
        registerEntity("dimension_hopper_final_boss", EntityFinalBoss.class, 100, 0x0, 0x0);
        registerEntity("homing_projectile", HomingProjectile.class, 100, 0x0, 0x0);
    }

    private static void registerEntity(String name, Class <? extends Entity> entity, int trackingRange, int spawnEggFirstColor, int spawnEggSecondColor) {
        EntityRegistry.registerModEntity(
                new ResourceLocation(DimHopperTweaks.MODID, name),
                entity,
                name,
                globalUniqueEntityId++,
                DimHopperTweaks.INSTANCE,
                trackingRange,
                1,
                true,
                spawnEggFirstColor,
                spawnEggSecondColor
        );
    }
}
