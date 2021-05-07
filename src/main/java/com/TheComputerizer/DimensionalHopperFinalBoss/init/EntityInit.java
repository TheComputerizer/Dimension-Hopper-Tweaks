package com.TheComputerizer.DimensionalHopperFinalBoss.init;

import com.TheComputerizer.DimensionalHopperFinalBoss.DimensionalHopperFinalBoss;
import com.TheComputerizer.DimensionalHopperFinalBoss.Reference;
import com.TheComputerizer.DimensionalHopperFinalBoss.entity.EntityFinalBoss;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityInit {
    public static void registerEntities(){
        registerEntity("finalboss", EntityFinalBoss.class, Reference.ENTITY_FINAL_BOSS, 100, 000000, 000000);
    }

    private static void registerEntity(String name, Class <? extends Entity> entity, int id, int range, int color1, int color2) {
        EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID + ":" + name), entity, name, id, DimensionalHopperFinalBoss.instance, range, 1,true, color1, color2 );
    }
}
