package mods.thecomputerizer.dimhoppertweaks.util;

import mods.thecomputerizer.dimhoppertweaks.client.particle.ParticleBlightFire;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

    @SideOnly(Side.CLIENT)
    public static void spawnBlightParticle(World world, double x, double y, double z, double width, double height) {
        double speedX = (world.rand.nextDouble()*2d)-1d;
        double speedY = (world.rand.nextDouble()*2d)-1d;
        double speedZ = (world.rand.nextDouble()*2d)-1d;
        double posX = x+(speedX*width/4d);
        double posY = y+((height/2d)+(speedY*height/4d));
        double posZ = z+(speedZ*width/4d);
        ParticleBlightFire particle = new ParticleBlightFire(world,posX,posY,posZ,speedX,speedY,speedZ,100f,32d,0.5f);
        FMLClientHandler.instance().getClient().effectRenderer.addEffect(particle);
    }
}
