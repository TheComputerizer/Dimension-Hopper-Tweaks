package mods.thecomputerizer.dimhoppertweaks.core;

import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name;
import zone.rong.mixinbooter.IEarlyMixinLoader;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static mods.thecomputerizer.dimhoppertweaks.core.DHTRef.LOGGER;

@Name("DimHopperTweaks")
@MCVersion(ForgeVersion.mcVersion)
public class MixinPlugin implements IEarlyMixinLoader, IFMLLoadingPlugin {

    static {
        LOGGER.info("Initializing early mixin twinkies... I mean tweaks");
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public @Nullable String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String,Object> data) {}

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
    
    @Override public List<String> getMixinConfigs() {
        return Arrays.asList(DHTRef.modIDs("mixins.%s_vanilla_access.json","mixins.%s_vanilla.json","mixins.%s_forge.json"));
    }
}
