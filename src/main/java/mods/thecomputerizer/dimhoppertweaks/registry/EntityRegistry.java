package mods.thecomputerizer.dimhoppertweaks.registry;

import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import mods.thecomputerizer.dimhoppertweaks.DimHopperTweaks;
import mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.EntityFinalBoss;
import mods.thecomputerizer.dimhoppertweaks.registry.entities.HomingProjectile;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;

public final class EntityRegistry {
    private static int globalUniqueEntityId = 0;

    public static void register() {
        registerEntity("dimension_hopper_final_boss", EntityFinalBoss.class, 100, 0x0, 0x0);
        registerEntity("homing_projectile", HomingProjectile.class, 100, 0x0, 0x0);
    }

    @SuppressWarnings("SameParameterValue")
    private static void registerEntity(String name, Class <? extends Entity> entity, int trackingRange, int spawnEggFirstColor, int spawnEggSecondColor) {
        net.minecraftforge.fml.common.registry.EntityRegistry.registerModEntity(
                Constants.res(name), entity, name,globalUniqueEntityId++,
                DimHopperTweaks.INSTANCE, trackingRange, 1, true,
                spawnEggFirstColor,spawnEggSecondColor
        );
    }
}
