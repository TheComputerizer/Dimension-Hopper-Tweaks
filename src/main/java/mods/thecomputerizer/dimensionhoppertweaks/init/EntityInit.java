package mods.thecomputerizer.dimensionhoppertweaks.init;

import mods.thecomputerizer.dimensionhoppertweaks.DimensionHopperTweaks;
import mods.thecomputerizer.dimensionhoppertweaks.Reference;
import mods.thecomputerizer.dimensionhoppertweaks.entity.EntityFinalBoss;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityInit {
    public static void registerEntities(){
        registerEntity("finalboss", EntityFinalBoss.class, Reference.ENTITY_FINAL_BOSS, 100, 000000, 000000);
    }

    private static void registerEntity(String name, Class <? extends Entity> entity, int id, int range, int color1, int color2) {
        EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID + ":" + name), entity, name, id, DimensionHopperTweaks.instance, range, 1,true, color1, color2 );
    }
}
