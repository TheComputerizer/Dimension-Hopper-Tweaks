package mods.thecomputerizer.dimhoppertweaks.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Objects;

public class WorldUtil {

    public static @Nullable TileEntity getTileOrAdjacent(World world, BlockPos centerPos, boolean checkCenter,
                                                         Collection<Class<?>> types) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(centerPos);
        TileEntity tile = checkValidTile(world,pos,types);
        if(Objects.nonNull(tile) && checkCenter) return tile;
        tile = addCoordsAndCheck(world,pos,EnumFacing.EAST,types);
        if(Objects.nonNull(tile)) return tile;
        tile = addCoordsAndCheck(world,pos,EnumFacing.NORTH,types);
        if(Objects.nonNull(tile)) return tile;
        return addCoordsAndCheck(world,pos,EnumFacing.UP,types);
    }

    private static @Nullable TileEntity addCoordsAndCheck(World world, BlockPos.MutableBlockPos pos, EnumFacing side,
                                                          Collection<Class<?>> types) {
        pos.move(side);
        TileEntity tile = checkValidTile(world,pos,types);
        if(Objects.nonNull(tile)) return tile;
        pos.move(side,-2);
        tile = checkValidTile(world,pos,types);
        if(Objects.nonNull(tile)) return tile;
        pos.move(side);
        return null;
    }

    public static @Nullable TileEntity checkValidTile(World world, BlockPos pos, Collection<Class<?>> types) {
        TileEntity tile = world.getTileEntity(pos);
        if(Objects.isNull(tile) || types.isEmpty()) return tile;
        for(Class<?> type : types)
            if(Objects.nonNull(type) && type.isAssignableFrom(tile.getClass()))
                return tile;
        return null;
    }
}
