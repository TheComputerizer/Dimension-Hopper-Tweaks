package mods.thecomputerizer.dimhoppertweaks.registry;

import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import mods.thecomputerizer.dimhoppertweaks.registry.blocks.LightningEnhancer;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@GameRegistry.ObjectHolder(Constants.MODID)
public final class BlockRegistry {

    private static final List<Block> ALL_BLOCKS = new ArrayList<>();
    public static final Block LIGHTNING_ENHANCER = makeBlock("lightning_enhancer", LightningEnhancer::new,
            block -> block.setCreativeTab(CreativeTabs.MISC));

    @SuppressWarnings("SameParameterValue")
    private static Block makeBlock(final String name, final Supplier<Block> constructor, final Consumer<Block> config) {
        final Block block = constructor.get();
        config.accept(block);
        block.setRegistryName(Constants.MODID, name);
        block.setTranslationKey(Constants.MODID+"."+name);
        ALL_BLOCKS.add(block);
        return block;
    }

    public static Block[] getBlocks() {
        return ALL_BLOCKS.toArray(new Block[0]);
    }
}
