package mods.thecomputerizer.dimhoppertweaks.mixin.mods.lightningcraft;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import sblectric.lightningcraft.blocks.BlockAirTerminal.EnumType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(value = EnumType.class, remap = false)
public class MixinBlockAirTerminalEnumType {
    
    @Shadow @Final @Mutable private static EnumType[] $VALUES;
    
    @Unique private static final EnumType AWAKENED_DRACONIUM = dimhoppertweaks$addTerminalType("AWAKENED_DRACONIUM");
    @Unique private static final EnumType INSANIUM = dimhoppertweaks$addTerminalType("INSANIUM");
    @Unique private static final EnumType ULTIMATE = dimhoppertweaks$addTerminalType("ULTIMATE");
    @Unique private static final EnumType INFINITY = dimhoppertweaks$addTerminalType("INFINITY");
    
    @Invoker("<init>")
    private static EnumType dimhoppertweaks$invokeInit(String internalName, int ordinal, int meta) {
        throw new AssertionError();
    }
    
    @SuppressWarnings("DataFlowIssue") @Unique
    private static EnumType dimhoppertweaks$addTerminalType(String internalName) {
        List<EnumType> variants = new ArrayList<>(Arrays.asList($VALUES));
        int size = variants.size();
        int ordinal = variants.get(size-1).ordinal()+1; //probably the same but just in case
        EnumType type = dimhoppertweaks$invokeInit(internalName,ordinal,size);
        variants.add(type);
        $VALUES = variants.toArray(new EnumType[0]);
        return type;
    }
}