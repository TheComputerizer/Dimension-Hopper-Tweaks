package mods.thecomputerizer.dimhoppertweaks.core;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import zone.rong.mixinbooter.IEarlyMixinLoader;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EarlyMixinPlugin implements IFMLLoadingPlugin, IEarlyMixinLoader {

    static {
        DHTRef.LOGGER.info("Initializing early mixin twinkies... I mean tweaks");
    }

    @Override
    public List<String> getMixinConfigs() {
        return Stream.of("dimhoppertweaks_vanilla.mixin.json").collect(Collectors.toList());
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return CoreContainer.class.getName();
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
