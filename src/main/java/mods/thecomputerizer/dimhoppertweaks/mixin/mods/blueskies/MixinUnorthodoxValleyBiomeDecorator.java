package mods.thecomputerizer.dimhoppertweaks.mixin.mods.blueskies;

import com.legacy.blue_skies.blocks.BlocksSkies;
import com.legacy.blue_skies.world.everdawn.biome.decoration.biome_decorators.UnorthodoxValleyBiomeDecorator;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(UnorthodoxValleyBiomeDecorator.class)
public abstract class MixinUnorthodoxValleyBiomeDecorator {

    @Shadow(remap = false) public abstract void spawnOre(IBlockState state, int size, int chance, int y);

    /**
     * @author The_Computerizer
     * @reason Remove emerald ore generation
     */
    @Overwrite(remap = false)
    public void spawnOres() {
        this.spawnOre(BlocksSkies.everdawn_diopside_ore.getDefaultState(),4,5,30);
        this.spawnOre(BlocksSkies.everdawn_moonstone_ore.getDefaultState(),14,17,128);
        this.spawnOre(BlocksSkies.everdawn_pyrope_ore.getDefaultState(),7,10,84);
        this.spawnOre(BlocksSkies.everdawn_turquoise_ore.getDefaultState(),5,9,55);
        this.spawnOre(BlocksSkies.everdawn_charoite_ore.getDefaultState(),4,3,20);
        this.spawnOre(BlocksSkies.coarse_lunar_dirt.getDefaultState(),32,30,256);
    }
}