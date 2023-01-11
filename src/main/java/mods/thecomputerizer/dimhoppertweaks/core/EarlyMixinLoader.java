package mods.thecomputerizer.dimhoppertweaks.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zone.rong.mixinbooter.IEarlyMixinLoader;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class EarlyMixinLoader implements IEarlyMixinLoader {

    private static final Logger MIXIN_LOGGER = LogManager.getLogger();

    @Override
    public List<String> getMixinConfigs() {
        ArrayList<String> ret = new ArrayList<>();
        ret.add("dimhoppertweaks_early.mixin.json");
        return ret;
    }
}
