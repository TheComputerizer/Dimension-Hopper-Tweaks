package mods.thecomputerizer.dimensionhoppertweaks.asm;

import com.google.common.collect.ImmutableSet;
import net.thesilkminer.mc.fermion.asm.api.PluginMetadata;
import net.thesilkminer.mc.fermion.asm.prefab.AbstractLaunchPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;
import zone.rong.mixinbooter.ILateMixinLoader;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class DimensionHopperTweaksLaunchPlugin extends AbstractLaunchPlugin {
    public static final String PLUGIN_ID = "dimensionhoppertwinkies.asm";

    public DimensionHopperTweaksLaunchPlugin() {
        super(PLUGIN_ID);
        this.registerTransformer(new DimensionTypeLoggingTransformer(this, this.logger));
        this.registerTransformer(new DimensionTypeTransformer(this, this.logger));
        this.registerTransformer(new ItemSwordInfinityTransformer(this, this.logger));
        //MixinBootstrap.init();
        //Mixins.addConfiguration("dimensionhoppertweaks.mixin.json");
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
        return ImmutableSet.of("mods.thecomputerizer.dimensionhoppertweaks.asm");
    }
}