package mods.thecomputerizer.dimhoppertweaks.mixin.mods.goodnightsleep;

import com.legacy.goodnightsleep.world.dream.biome.GoodDreamBiomeDecorator;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import static com.legacy.goodnightsleep.blocks.BlocksGNS.*;
import static net.minecraft.init.Blocks.GLOWSTONE;

@Mixin(value = GoodDreamBiomeDecorator.class, remap = false)
public abstract class MixinGoodDreamBiomeDecorator {

    @Shadow public abstract void spawnOre(IBlockState state, int size, int chance, int y);

    /**
     * @author The_Computerizer
     * @reason Remove rainbow ore generation
     */
    @Overwrite
    public void spawnOres() {
        this.spawnOre(dream_dirt.getDefaultState(),32,20,128);
        this.spawnOre(coal_ore.getDefaultState(),16,20,128);
        this.spawnOre(lapis_ore.getDefaultState(),6,1,16);
        this.spawnOre(GLOWSTONE.getDefaultState(),8,15,3);
        this.spawnOre(candy_ore.getDefaultState(),8,20,128);
        this.spawnOre(positite_ore.getDefaultState(),4,1,16);
    }
}