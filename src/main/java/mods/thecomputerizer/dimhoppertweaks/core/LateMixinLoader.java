package mods.thecomputerizer.dimhoppertweaks.core;

import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class LateMixinLoader implements ILateMixinLoader {

    @Override
    public List<String> getMixinConfigs() {
        return Stream.of("dimhoppertweaks_mods.mixin.json").collect(Collectors.toList());
    }
}
