package mods.thecomputerizer.dimhoppertweaks.mixin.mods.lockyzextradimensionsmod;

import mods.thecomputerizer.dimhoppertweaks.mixin.api.IBlockFlintOre;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "mod.mcreator.mcreator_flintOre$BlockFlintOre", remap = false)
public abstract class MixinBlockFlintOre extends Block implements IBlockFlintOre {
    
    public MixinBlockFlintOre(Material material, MapColor color) {
        super(material, color);
    }

    @Override
    public IBlockState dimhoppertweaks$getDefaultState() {
        return getDefaultState();
    }
}