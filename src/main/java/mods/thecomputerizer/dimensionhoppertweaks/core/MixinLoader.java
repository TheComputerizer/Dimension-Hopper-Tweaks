package mods.thecomputerizer.dimensionhoppertweaks.core;

import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class MixinLoader implements ILateMixinLoader {

    @Override
    public List<String> getMixinConfigs() {
        ArrayList<String> ret = new ArrayList<>();
        ret.add("dimensionhoppertweaks.mixin.json");
        return ret;
    }
}
