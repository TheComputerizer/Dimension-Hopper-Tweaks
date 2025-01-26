package mods.thecomputerizer.dimhoppertweaks.registry;

import mods.thecomputerizer.dimhoppertweaks.registry.blocks.AutoInfusionTable;
import mods.thecomputerizer.dimhoppertweaks.registry.blocks.LightningEnhancer;
import mods.thecomputerizer.dimhoppertweaks.registry.tiles.AutoInfusionTableEntity;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static mods.thecomputerizer.dimhoppertweaks.core.DHTRef.MODID;

public final class BlockRegistry {

    private static final List<Block> ALL_BLOCKS = new ArrayList<>();
    public static final Block AUTO_INFUSION_TABLE = makeBlock("auto_infusion_table",
            () -> new AutoInfusionTable("auto_infusion_table",AutoInfusionTableEntity.class),
            block -> block.setCreativeTab(CreativeTabs.MISC));
    public static final Block LIGHTNING_ENHANCER = makeBlock("lightning_enhancer", LightningEnhancer::new,
            block -> block.setCreativeTab(CreativeTabs.MISC));

    @SuppressWarnings("SameParameterValue")
    private static Block makeBlock(final String name, final Supplier<Block> constructor, final Consumer<Block> config) {
        final Block block = constructor.get();
        config.accept(block);
        block.setRegistryName(MODID,name);
        block.setTranslationKey(MODID+"."+name);
        ALL_BLOCKS.add(block);
        return block;
    }

    public static Block[] getBlocks() {
        return ALL_BLOCKS.toArray(new Block[0]);
    }
}