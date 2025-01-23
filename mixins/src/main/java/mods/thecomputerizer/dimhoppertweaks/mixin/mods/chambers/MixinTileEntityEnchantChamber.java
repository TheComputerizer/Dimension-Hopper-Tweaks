package mods.thecomputerizer.dimhoppertweaks.mixin.mods.chambers;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import usertravis.chambers.CustomEnergyStorage;
import usertravis.chambers.enchant.TileEntityEnchantChamber;

@Mixin(value = TileEntityEnchantChamber.class, remap = false)
public class MixinTileEntityEnchantChamber {
    
    @Shadow private CustomEnergyStorage storage = new CustomEnergyStorage(200000,200000,0);
}