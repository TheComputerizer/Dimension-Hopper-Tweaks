package mods.thecomputerizer.dimhoppertweaks.registry;

import mods.thecomputerizer.dimhoppertweaks.registry.structures.AbstractStructure;
import mods.thecomputerizer.dimhoppertweaks.registry.structures.StargateStructure;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public final class StructureRegistry {

    public static final IForgeRegistry<AbstractStructure> REGISTRY = GameRegistry.findRegistry(AbstractStructure.class);
    private static final List<AbstractStructure> ALL_STRUCTURES = new ArrayList<>();
    public static final AbstractStructure STARGATE = makeStructure(new StargateStructure());

    private static <A extends AbstractStructure> A makeStructure(A struct) {
        ALL_STRUCTURES.add(struct);
        return struct;
    }

    public static AbstractStructure[] getStructures() {
        return ALL_STRUCTURES.toArray(new AbstractStructure[0]);
    }
}