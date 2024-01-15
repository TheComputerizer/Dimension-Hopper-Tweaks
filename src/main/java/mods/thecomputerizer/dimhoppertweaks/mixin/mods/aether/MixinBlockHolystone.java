package mods.thecomputerizer.dimhoppertweaks.mixin.mods.aether;

import com.gildedgames.aether.common.blocks.natural.BlockHolystone;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = BlockHolystone.class, remap = false)
public abstract class MixinBlockHolystone {

    /**
     * @author The_Computerizer
     * @reason Remove radiation ticking
     */
    @Overwrite
    private void tickRadiation(World world, BlockPos pos, int radiationAmount, int radiationDistance, int radiationRate) {}
}