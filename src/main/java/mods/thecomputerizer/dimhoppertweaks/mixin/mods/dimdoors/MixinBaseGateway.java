package mods.thecomputerizer.dimhoppertweaks.mixin.mods.dimdoors;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.dimdev.dimdoors.shared.world.gateways.BaseGateway;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Arrays;
import java.util.List;

@Mixin(value = BaseGateway.class, remap = false)
public abstract class MixinBaseGateway {

    @Unique
    private static final List<Integer> BLACKLISTED_DIMENSIONS = Arrays.asList(7,-1,1,-2,-9,-13,-15,-16,-17,-18,-19,-20,
            -21,-23,-27,-28,-29,-30,-31,-42,-61,-62,-63,-64,-65,-66,-67,-68,-69,-70,-71,-72,-73,-74,-75,-76,-77,-78,-79,
            -80,-81,-82,-1500,-1501,-1502,-1503,-1504,-1505,-1506,-1507,-1508,-1509,-1510,-1511,-2542,-2543,-2544,17,20,
            44,45,49,66,76,77,800,801,802,803,804,805,806,807,808,809,810,811,812,813,814,815,816,817,818,819,820,821,1024);

    @Shadow protected abstract boolean isBiomeValid(Biome biome);

    /**
     * @author The_Computerizer
     * @reason Gates should only generate in the overworld and the config doesn't seem to actually be implemented yet
     */
    @Overwrite
    public boolean isLocationValid(World world, int x, int y, int z) {
        return !BLACKLISTED_DIMENSIONS.contains(world.provider.getDimension()) &&
                this.isBiomeValid(world.getBiome(new BlockPos(x,y,z)));
    }
}
