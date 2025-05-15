package mods.thecomputerizer.dimhoppertweaks.registry;

import mekanism.api.gas.Gas;

import java.util.ArrayList;
import java.util.List;

public class GasRegistry {
    
    private static final List<Gas> ALL_GASES = new ArrayList<>();
    public static final Gas RAW_ANTI_RAD_SOLUTION = makeGas("raw_anti_rad_solution",12018695);
    public static final Gas ANTI_RAD_SOLUTION = makeGas("anti_rad_solution",16772096);
    
    @SuppressWarnings("SameParameterValue")
    private static Gas makeGas(String name, int color) {
        Gas gas = new Gas(name,color);
        ALL_GASES.add(gas);
        return gas;
    }
    
    public static void register() {
        for(Gas gas : ALL_GASES) mekanism.api.gas.GasRegistry.register(gas);
    }
}