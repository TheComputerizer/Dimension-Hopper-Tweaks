package mods.thecomputerizer.dimensionhoppertweaks.util;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TeleporterGeneric extends Teleporter {
    public TeleporterGeneric(WorldServer world) {
        super(world);
    }

    public void func_180266_a(Entity entityIn, float rotationYaw) {
    }

    public boolean func_180620_b(Entity entityIn, float rotationYaw) {
        return false;
    }

    public boolean func_85188_a(Entity p_85188_1_) {
        return false;
    }

    public void func_85189_a(long worldTime) {
    }
}
