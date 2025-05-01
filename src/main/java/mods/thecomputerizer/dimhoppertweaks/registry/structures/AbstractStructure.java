package mods.thecomputerizer.dimhoppertweaks.registry.structures;

import com.google.common.reflect.TypeToken;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Objects;
import java.util.Random;

/**
 * Does not extend IForgeRegistryEntry$Impl since this already extends WorldGenerator
 */
@SuppressWarnings({"unchecked", "UnstableApiUsage", "unused"})
public abstract class AbstractStructure extends WorldGenerator implements IForgeRegistryEntry<AbstractStructure>  {

    private final TypeToken<AbstractStructure> token = new TypeToken<AbstractStructure>(getClass()){};
    private ResourceLocation registryName = null;

    public final AbstractStructure setRegistryName(String name) {
        if(Objects.nonNull(this.registryName))
            throw new IllegalStateException("Attempted to set registry name with existing registry name! New: "+name+
                    " Old: "+getRegistryName());
        this.registryName = GameData.checkPrefix(name,true);
        return this;
    }

    @Override
    public final AbstractStructure setRegistryName(ResourceLocation name){ return setRegistryName(name.toString()); }
    public final AbstractStructure setRegistryName(String modID, String name){ return setRegistryName(modID+":"+name); }

    @Override
    public final ResourceLocation getRegistryName() {
        return Objects.nonNull(this.registryName) ? this.registryName : null;
    }

    @Override
    public final Class<AbstractStructure> getRegistryType() {
        return (Class<AbstractStructure>)this.token.getRawType();
    }

    @Override
    public final boolean generate(World world, Random rand, BlockPos pos) {
        return build(world,new BlockPos.MutableBlockPos(pos));
    }

    public abstract boolean build(World world, BlockPos pos);

    public final void addBlock(World world, BlockPos pos, int xOffset, int yOffset, int zOffset, IBlockState state) {
        world.setBlockState(pos.add(xOffset,yOffset,zOffset),state,2);
    }
}
