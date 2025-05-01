package mods.thecomputerizer.dimhoppertweaks.registry.entities;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;


public class InvincibleEntityItem extends EntityItem {

    public InvincibleEntityItem(World world, double x, double y, double z, ItemStack stack) {
        super(world,x,y,z,stack);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(source==DamageSource.OUT_OF_WORLD) {
            this.setDead();
            return true;
        }
        return false;
    }
}
