package mods.thecomputerizer.dimhoppertweaks.registry;

import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import mods.thecomputerizer.dimhoppertweaks.registry.blocks.LightningEnhancer;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class BlockRegistry {

    private static final List<Block> ALL_BLOCKS = new ArrayList<>();
    public static final Block LIGHTNING_ENHANCER = makeBlock("lightning_enhancer", LightningEnhancer::new,
            block -> block.setCreativeTab(CreativeTabs.MISC));

    @SuppressWarnings("SameParameterValue")
    private static Block makeBlock(final String name, final Supplier<Block> constructor, final Consumer<Block> config) {
        final Block block = constructor.get();
        config.accept(block);
        block.setRegistryName(DHTRef.MODID, name);
        block.setTranslationKey(DHTRef.MODID+"."+name);
        ALL_BLOCKS.add(block);
        return block;
    }

    public static Block[] getBlocks() {
        return ALL_BLOCKS.toArray(new Block[0]);
    }
}
