package mods.thecomputerizer.dimhoppertweaks.mixin.mods.lightningcraft;

import mods.thecomputerizer.dimhoppertweaks.mixin.access.EnergyStorageAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import sblectric.lightningcraft.util.EnergyStorage;

@Mixin(value = EnergyStorage.class, remap = false)
public abstract class MixinEnergyStorage implements EnergyStorageAccess {

    @Shadow protected int maxReceive;

    @Shadow protected int maxExtract;

    @Override
    public void dimhoppertweaks$setReceive(int value) {
        this.maxReceive = value;
    }

    @Override
    public void dimhoppertweaks$setExtract(int value) {
        this.maxExtract = value;
    }
}
