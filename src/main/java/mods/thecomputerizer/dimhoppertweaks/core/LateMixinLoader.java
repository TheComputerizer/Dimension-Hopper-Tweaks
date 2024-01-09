package mods.thecomputerizer.dimhoppertweaks.core;

import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public class LateMixinLoader implements ILateMixinLoader {

    @Override
    public List<String> getMixinConfigs() {
        return Collections.singletonList(DHTRef.modID("mixins.%s_mods.json"));
    }
}
