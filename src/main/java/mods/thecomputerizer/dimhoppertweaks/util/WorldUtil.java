package mods.thecomputerizer.dimhoppertweaks.util;

import com.google.common.base.Predicate;
import mods.thecomputerizer.dimhoppertweaks.client.particle.ParticleBlightFire;
import mods.thecomputerizer.dimhoppertweaks.common.capability.chunk.ExtraChunkData;
import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import mods.thecomputerizer.dimhoppertweaks.config.DHTConfigHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static mods.thecomputerizer.dimhoppertweaks.core.DHTRef.LOGGER;
import static net.minecraft.util.EnumFacing.EAST;
import static net.minecraft.util.EnumFacing.NORTH;
import static net.minecraft.util.EnumFacing.UP;
import static net.minecraftforge.fml.relauncher.Side.CLIENT;

public class WorldUtil {

    private static @Nullable TileEntity addCoordsAndCheck(World world, MutableBlockPos pos, EnumFacing side,
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

    public static @Nullable TileEntity getTileOrAdjacent(World world, BlockPos centerPos, boolean checkCenter,
            Collection<Class<?>> types) {
        MutableBlockPos pos = new MutableBlockPos(centerPos);
        TileEntity tile = checkValidTile(world,pos,types);
        if(Objects.nonNull(tile) && checkCenter) return tile;
        tile = addCoordsAndCheck(world,pos,EAST,types);
        if(Objects.nonNull(tile)) return tile;
        tile = addCoordsAndCheck(world,pos,NORTH,types);
        if(Objects.nonNull(tile)) return tile;
        return addCoordsAndCheck(world,pos,UP,types);
    }

    public static AxisAlignedBB getBoxFromRange(Vec3d pos, double range) {
        Vec3d min = pos.subtract(range,range,range);
        Vec3d max = pos.add(range,range,range);
        return new AxisAlignedBB(min.x,min.y,min.z,max.x,max.y,max.z);
    }

    public static List<Entity> getEntitiesInRange(World world, Vec3d pos, double range,
                                                  Predicate<Entity> filter) {
        return world.getEntitiesWithinAABB(Entity.class,getBoxFromRange(pos,range),filter);
    }

    public static boolean hasFastPlayerInOrAdjacent(World world, int chunkX, int chunkZ, @Nullable EntityPlayer setter) {
        if(world.playerEntities.size()==1 && Objects.nonNull(setter)) return false;
        AtomicBoolean ret = new AtomicBoolean();
        iterateChunks(world,chunkX,chunkZ,1,chunk -> {
            for(EntityPlayer player : world.playerEntities) {
                if(player!=setter && isPlayerInChunk(chunk,player) && SkillWrapper.makesChunksFast(player)) {
                    ret.set(true);
                    return;
                }
            }
        });
        return ret.get();
    }


    public static boolean isChunkFast(World world, int chunkX, int chunkZ) {
        if(world.isChunkGeneratedAt(chunkX,chunkZ)) return ExtraChunkData.isChunkFast(world.getChunk(chunkX,chunkZ));
        else LOGGER.error("Tried to query fast status of ungenerated chunk at ({},{})!",chunkX,chunkZ);
        return false;
    }

    public static boolean isPlayerInChunk(Chunk chunk, EntityPlayer player) {
        return chunk.x==player.chunkCoordX && chunk.z==player.chunkCoordZ;
    }

    public static void iterateChunks(World world, int chunkX, int chunkZ, int range, Consumer<Chunk> settings) {
        for(int x=chunkX-range; x<=chunkX+range; x++) {
            for(int z=chunkZ-range; z<=chunkZ+range; z++) {
                if(world.isChunkGeneratedAt(x,z)) {
                    Chunk chunk = world.getChunk(x,z);
                    if(chunk.isLoaded()) settings.accept(chunk);
                }
            }
        }
    }

    public static ITeleporter makeTeleporter(double posY) {
        return (world,entity,yaw) -> {
            float pitch = entity.rotationPitch;
            entity.setLocationAndAngles(entity.posX,posY,entity.posZ,entity.rotationYaw,pitch);
        };
    }

    public static int replaceBlocks(World world, Collection<Block> replaced, IBlockState replaceWith,
            BlockPos min, BlockPos max) {
        int replacementCount = 0;
        MutableBlockPos pos = new MutableBlockPos();
        for(int x=min.getX();x<=max.getX();x++) {
            for(int y=min.getY();y<=max.getY();y++) {
                for(int z=min.getZ();z<=max.getZ();z++) {
                    pos.setPos(x,y,z);
                    if(replaced.contains(world.getBlockState(pos).getBlock()) && (world.setBlockState(pos,replaceWith)))
                        replacementCount++;
                }
            }
        }
        return replacementCount;
    }

    public static void setFastChunk(World world, int chunkX, int chunkZ, boolean fast) {
        if(world.isChunkGeneratedAt(chunkX,chunkZ)) {
            Chunk chunk = world.getChunk(chunkX,chunkZ);
            if(chunk.isLoaded()) {
                if(fast) fast = hasFastPlayerInOrAdjacent(world,chunkX,chunkZ,null);
                if(DHTConfigHelper.isDevLogEnabled())
                    DHTConfigHelper.devDebug((fast ? "MARKING" : "REMOVING FAST STATUS FROM")+" CHUNK AT ({},{}) "+
                            (fast ? "AS FAST" : "")+" ON "+(world instanceof WorldServer ? "SERVER" : "CLIENT")+" SIDE",
                            chunkX,chunkZ);
                ExtraChunkData.setFastChunk(chunk,fast);
            }
        }
    }

    public static void setFastChunk(World world, int chunkX, int chunkZ, @Nullable EntityPlayer player) {
        setFastChunk(world,chunkX,chunkZ,Objects.nonNull(player) && SkillWrapper.makesChunksFast(player));
    }

    @SideOnly(CLIENT)
    public static void spawnBlightParticle(World world, double x, double y, double z, double width, double height) {
        double speedX = (world.rand.nextDouble()*2d)-1d;
        double speedY = (world.rand.nextDouble()*2d)-1d;
        double speedZ = (world.rand.nextDouble()*2d)-1d;
        double posX = x+(speedX*width/4d);
        double posY = y+((height/2d)+(speedY*height/4d));
        double posZ = z+(speedZ*width/4d);
        ParticleBlightFire particle = new ParticleBlightFire(world,posX,posY,posZ,speedX,speedY,speedZ,100f,
                32d,0.5f,true);
        FMLClientHandler.instance().getClient().effectRenderer.addEffect(particle);
    }

    /**
     * Vanilla/Forge implementation so the height is probably clamped from 0-256
     */
    public static void teleportDimY(Entity entity, int dim, double posY) {
        entity.changeDimension(dim,makeTeleporter(posY));
    }
}