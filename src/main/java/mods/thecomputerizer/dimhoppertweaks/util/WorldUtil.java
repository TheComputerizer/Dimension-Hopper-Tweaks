package mods.thecomputerizer.dimhoppertweaks.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Objects;

public class WorldUtil {

    public static @Nullable TileEntity getTileOrAdjacent(World world, BlockPos centerPos, Class<?> ... types) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(centerPos);
        TileEntity tile = addCoordsAndCheck(world,pos,0,0,0,types);
        if(Objects.nonNull(tile)) return tile;
        tile = addCoordsAndCheck(world,pos,-1,0,0,types);
        if(Objects.nonNull(tile)) return tile;
        tile = addCoordsAndCheck(world,pos,2,0,0,types);
        if(Objects.nonNull(tile)) return tile;
        tile = addCoordsAndCheck(world,pos,0,-1,0,types);
        if(Objects.nonNull(tile)) return tile;
        tile = addCoordsAndCheck(world,pos,0,2,0,types);
        if(Objects.nonNull(tile)) return tile;
        tile = addCoordsAndCheck(world,pos,0,0,-1,types);
        if(Objects.nonNull(tile)) return tile;
        tile = addCoordsAndCheck(world,pos,0,0,2,types);
        pos.add(0,0,-1);
        return tile;
    }

    private static @Nullable TileEntity addCoordsAndCheck(World world, BlockPos.MutableBlockPos pos, int x, int y,
                                                          int z, Class<?> ... types) {
        pos.setPos(pos.getX()+x,pos.getX()+y,pos.getX()+z);
        TileEntity tile = world.getTileEntity(pos);
        if(Objects.isNull(tile) || types.length==0) return tile;
        for(Class<?> type : types)
            if(Objects.nonNull(type) && type.isAssignableFrom(tile.getClass()))
                return tile;
        return null;
    }
}
