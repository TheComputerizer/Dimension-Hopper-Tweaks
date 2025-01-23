package mods.thecomputerizer.dimhoppertweaks.mixin.mods.dimdoors;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import org.dimdev.dimdoors.shared.world.gateways.GatewayGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Mixin(value = GatewayGenerator.class, remap = false)
public abstract class MixinGatewayGenerator {

    @Unique private static final List<Integer> BLACKLISTED_DIMENSIONS = Arrays.asList(7,-1,1,-2,-9,-13,-15,-16,-17,-18,
            -19,-20,-21,-23,-27,-28,-29,-30,-31,-42,-61,-62,-63,-64,-65,-66,-67,-68,-69,-70,-71,-72,-73,-74,-75,-76,-77,
            -78,-79,-80,-81,-82,-1500,-1501,-1502,-1503,-1504,-1505,-1506,-1507,-1508,-1509,-1510,-1511,-2542,-2543,
            -2544,17,20,44,45,49,66,76,77,800,801,802,803,804,805,806,807,808,809,810,811,812,813,814,815,816,817,818,
            819,820,821,1024);

    @Inject(at = @At("HEAD"), method = "generate", cancellable = true)
    private void dimhoppertweaks$generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator generator,
                                          IChunkProvider provider, CallbackInfo ci) {
        if(!world.isRemote && BLACKLISTED_DIMENSIONS.contains(world.provider.getDimension())) ci.cancel();
    }
}