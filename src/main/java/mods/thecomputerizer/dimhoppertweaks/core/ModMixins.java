package mods.thecomputerizer.dimhoppertweaks.core;

import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.Collections;
import java.util.List;

import static mods.thecomputerizer.dimhoppertweaks.core.DHTRef.LOGGER;

@SuppressWarnings("unused")
public class ModMixins implements ILateMixinLoader {
    
    static {
        LOGGER.info("Initializing late mixin tweaks... I mean twinkies");
    }
    
    @Override public List<String> getMixinConfigs() {
        return Collections.singletonList(DHTRef.modID("mixins.%s_mods.json"));
    }
}
