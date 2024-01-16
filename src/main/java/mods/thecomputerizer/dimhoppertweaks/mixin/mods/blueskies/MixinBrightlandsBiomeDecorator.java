package mods.thecomputerizer.dimhoppertweaks.mixin.mods.blueskies;

import com.legacy.blue_skies.blocks.BlocksSkies;
import com.legacy.blue_skies.world.everbright.biome.decoration.biome_decorators.BrightlandsBiomeDecorator;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BrightlandsBiomeDecorator.class)
public abstract class MixinBrightlandsBiomeDecorator {

    @Shadow(remap = false) public abstract void spawnOre(IBlockState state, int size, int chance, int y);

    /**
     * @author The_Computerizer
     * @reason Remove emerald ore generation
     */
    @Overwrite(remap = false)
    public void spawnOres() {
        this.spawnOre(BlocksSkies.everbright_diopside_ore.getDefaultState(),4,5,30);
        this.spawnOre(BlocksSkies.everbright_moonstone_ore.getDefaultState(),14,17,128);
        this.spawnOre(BlocksSkies.everbright_pyrope_ore.getDefaultState(),7,10,84);
        this.spawnOre(BlocksSkies.everbright_turquoise_ore.getDefaultState(),5,9,55);
        this.spawnOre(BlocksSkies.everbright_charoite_ore.getDefaultState(),4,3,20);
        this.spawnOre(BlocksSkies.coarse_turquoise_dirt.getDefaultState(),32,30,256);
    }
}