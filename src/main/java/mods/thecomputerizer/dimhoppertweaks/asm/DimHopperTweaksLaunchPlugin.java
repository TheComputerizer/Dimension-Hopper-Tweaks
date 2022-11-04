package mods.thecomputerizer.dimhoppertweaks.asm;

import com.google.common.collect.ImmutableSet;
import net.thesilkminer.mc.fermion.asm.api.PluginMetadata;
import net.thesilkminer.mc.fermion.asm.prefab.AbstractLaunchPlugin;

import javax.annotation.Nonnull;
import java.util.Set;

public final class DimHopperTweaksLaunchPlugin extends AbstractLaunchPlugin {
    public static final String PLUGIN_ID = "dimhoppertwinkies.asm";

    public DimHopperTweaksLaunchPlugin() {
        super(PLUGIN_ID);
        this.registerTransformer(new ItemSwordInfinityTransformer(this, this.logger));
    }

    @Override
    protected void populateMetadata(@Nonnull final PluginMetadata.Builder builder) {
        builder.setName("Dimension Hopper Tweaks ASM")
                .setDescription("Collection of ASM patches for mods in the modpack Dimension Hopper")
                .setVersion("1.0.0")
                .addAuthor("TheSilkMiner")
                .addAuthor("TheComputerizer");
    }

    @Nonnull
    @Override
    public Set<String> getRootPackages() {
        return ImmutableSet.of("mods.thecomputerizer.dimhoppertweaks.asm");
    }
}