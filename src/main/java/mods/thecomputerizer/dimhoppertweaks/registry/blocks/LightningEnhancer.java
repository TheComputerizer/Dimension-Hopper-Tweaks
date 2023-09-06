package mods.thecomputerizer.dimhoppertweaks.registry.blocks;

import mods.thecomputerizer.dimhoppertweaks.registry.tiles.LightningEnhancerEntity;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LightningEnhancer extends Block implements ITileEntityProvider {

    public LightningEnhancer() {
        super(Material.ANVIL,MapColor.ICE);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull World world, int meta) {
        return new LightningEnhancerEntity();
    }
}
