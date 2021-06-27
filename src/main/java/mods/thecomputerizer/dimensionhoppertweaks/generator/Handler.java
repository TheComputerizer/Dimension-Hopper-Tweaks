package mods.thecomputerizer.dimensionhoppertweaks.generator;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

public class Handler {
    private static final TreeMap<String, DimHopperStruct> structures = new TreeMap<String, DimHopperStruct>();
    public static final DimHopperStruct EMPTY_STRUCTURE = new DimHopperStruct.EmptyStructure();

    protected static void registerStructure(DimHopperStruct structure) {
        String name = structure.getName();

        if (name != null && name.length() > 0)
            structures.put(name, structure);
    }

    public static boolean generateStructure(final String name, final World world, final Random rand, final BlockPos basePos) {
        return generateStructure(getStructure(name), world, rand, basePos);
    }

    public static boolean generateStructure(final DimHopperStruct structure, final World world, @Nullable Random rand, final BlockPos basePos) {
        if (structure == EMPTY_STRUCTURE)
            return false;

        structure.generate(world, rand, basePos);
        return true;
    }

    public static DimHopperStruct getStructure(String name) {
        DimHopperStruct structure = structures.get(name);
        return structure;
    }

    public static int getStructureListPageCount() {
        return (int)Math.ceil(structures.size() / 50d);
    }

    public static String getStructuresList(int pageIndex) {
        StringBuilder builder = new StringBuilder(", ");
        int i = 0;

        for (String name : structures.keySet()) {
            if (i >= (pageIndex - 1) * 50) {
                if (i >= pageIndex * 50)
                    return builder.toString().substring(2);

                builder.append(name);
                builder.append(", ");
            }

            i++;
        }

        return builder.toString().substring(2);
    }

    public static List<String> autoCompleteStructureName(String st) {
        List<String> names = new ArrayList<String>();

        for (String name : structures.keySet()) {
            if (name.startsWith(st))
                names.add(name);
        }
        return names;
    }

    public static void registerStructures() {
        new Stargate();
    }
}
