package mods.thecomputerizer.dimhoppertweaks.core;

import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.Arrays;
import java.util.List;

import static mods.thecomputerizer.dimhoppertweaks.core.DHTRef.LOGGER;

@SuppressWarnings("unused")
public class ModMixins implements ILateMixinLoader {
    
    static {
        LOGGER.info("Initializing late mixin tweaks... I mean twinkies");
    }
    
    @Override public List<String> getMixinConfigs() {
        return Arrays.asList(DHTRef.modIDs("mixins.%s_mod_access.json","mixins.%s_mods.json"));
    }
}
