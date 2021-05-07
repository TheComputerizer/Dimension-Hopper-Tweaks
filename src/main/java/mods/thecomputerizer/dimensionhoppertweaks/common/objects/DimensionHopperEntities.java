package mods.thecomputerizer.dimensionhoppertweaks.common.objects;

import mods.thecomputerizer.dimensionhoppertweaks.DimensionHopperTweaks;
import mods.thecomputerizer.dimensionhoppertweaks.common.objects.entity.EntityFinalBoss;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public final class DimensionHopperEntities {
    private static int globalUniqueEntityId = 0;

    private DimensionHopperEntities() {}

    public static void registerEntities() {
        registerEntity("dimension_hopper_boss", EntityFinalBoss.class, 100, 0x0, 0x0);
    }

    private static void registerEntity(String name, Class <? extends Entity> entity, int trackingRange, int spawnEggFirstColor, int spawnEggSecondColor) {
        EntityRegistry.registerModEntity(
                new ResourceLocation(DimensionHopperTweaks.MODID, name),
                entity,
                name,
                globalUniqueEntityId++,
                DimensionHopperTweaks.INSTANCE,
                trackingRange,
                1,
                true,
                spawnEggFirstColor,
                spawnEggSecondColor
        );
    }
}
