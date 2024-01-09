package mods.thecomputerizer.dimhoppertweaks.core;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import zone.rong.mixinbooter.IEarlyMixinLoader;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class EarlyMixinPlugin implements IFMLLoadingPlugin, IEarlyMixinLoader {

    static {
        DHTRef.LOGGER.info("Initializing early mixin twinkies... I mean tweaks");
    }

    @Override
    public List<String> getMixinConfigs() {
        return Arrays.asList(DHTRef.modIDs("mixins.%s_vanilla.json","mixins.%s_forge.json"));
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
    public void injectData(Map<String, Object> data) {}

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
