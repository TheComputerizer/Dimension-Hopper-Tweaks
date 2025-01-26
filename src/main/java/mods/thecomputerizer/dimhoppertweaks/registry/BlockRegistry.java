package mods.thecomputerizer.dimhoppertweaks.registry;

import mods.thecomputerizer.dimhoppertweaks.registry.blocks.AutoInfusionTable;
import mods.thecomputerizer.dimhoppertweaks.registry.blocks.LightningEnhancer;
import mods.thecomputerizer.dimhoppertweaks.registry.tiles.AutoInfusionTableEntity;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static mods.thecomputerizer.dimhoppertweaks.core.DHTRef.MODID;
import static net.minecraft.creativetab.CreativeTabs.MISC;

public final class BlockRegistry {

    private static final List<Block> ALL_BLOCKS = new ArrayList<>();
    public static final Block AUTO_INFUSION_TABLE = new AutoInfusionTable("auto_infusion_table",AutoInfusionTableEntity.class);
    public static final Block LIGHTNING_ENHANCER = makeBlock("lightning_enhancer",LightningEnhancer::new,
            block -> block.setCreativeTab(MISC));
    
    @SuppressWarnings("SameParameterValue")
    private static Block makeBlock(final String name, final Supplier<Block> constructor, final Consumer<Block> config) {
        return makeBlock(name,constructor,config,true);
    }

    private static Block makeBlock(final String name, final Supplier<Block> constructor, final Consumer<Block> config,
            boolean setRegistryName) {
        final Block block = constructor.get();
        config.accept(block);
        if(setRegistryName) block.setRegistryName(MODID,name);
        block.setTranslationKey(MODID+"."+name);
        ALL_BLOCKS.add(block);
        return block;
    }

    public static Block[] getBlocks() {
        return ALL_BLOCKS.toArray(new Block[0]);
    }
}