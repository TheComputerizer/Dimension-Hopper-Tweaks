package mods.thecomputerizer.dimhoppertweaks.core;

import fermiumbooter.FermiumRegistryAPI;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import javax.annotation.Nullable;
import java.util.Map;


@IFMLLoadingPlugin.Name("DimHopperTweaks")
@IFMLLoadingPlugin.MCVersion(ForgeVersion.mcVersion)
public class MixinPlugin implements IFMLLoadingPlugin {

    static {
        DHTRef.LOGGER.info("Initializing early mixin twinkies... I mean tweaks");
    }

    public MixinPlugin() {
        FermiumRegistryAPI.enqueueMixin(false,DHTRef.modIDs("mixins.%s_vanilla.json","mixins.%s_forge.json"));
        FermiumRegistryAPI.enqueueMixin(true,DHTRef.modID("mixins.%s_mods.json"));
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
