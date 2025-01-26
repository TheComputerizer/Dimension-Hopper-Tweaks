package mods.thecomputerizer.dimhoppertweaks.registry;

import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import mods.thecomputerizer.dimhoppertweaks.registry.entities.HomingProjectile;
import mods.thecomputerizer.dimhoppertweaks.registry.entities.boss.EntityFinalBoss;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public final class EntityRegistry {

    private static final List<EntityEntry> ALL_ENTRIES = new ArrayList<>();
    public static final EntityEntry FINAL_BOSS = makeEntry("dimension_hopper_final_boss",
            EntityFinalBoss.class,0,0);
    public static final EntityEntry HOMING_PROJECTILE = makeEntry("homing_projectile",
            HomingProjectile.class,1,1);
    private static int entityIdCounter = 0;

    @SuppressWarnings("unchecked")
    private static <E extends Entity> EntityEntry makeEntry(
            final String name, final Class<E> entityClass, final int eggColor1, final int eggColor2) {
        final EntityEntryBuilder<E> builder = EntityEntryBuilder.create();
        EntityEntryBuilder<E>.BuiltEntityEntry entry = (EntityEntryBuilder<E>.BuiltEntityEntry)builder.entity(entityClass)
                .tracker(100,1,true).egg(eggColor1,eggColor2)
                .name(name).id(DHTRef.res(name),entityIdCounter++).build();
        entry.addedToRegistry();
        ALL_ENTRIES.add(entry);
        return entry;
    }

    public static EntityEntry[] getEntityEntries() {
        return ALL_ENTRIES.toArray(new EntityEntry[0]);
    }
}