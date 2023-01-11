package mods.thecomputerizer.dimhoppertweaks.core;

import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class LateMixinLoader implements ILateMixinLoader {

    @Override
    public List<String> getMixinConfigs() {
        ArrayList<String> ret = new ArrayList<>();
        ret.add("dimhoppertweaks_late.mixin.json");
        return ret;
    }
}
