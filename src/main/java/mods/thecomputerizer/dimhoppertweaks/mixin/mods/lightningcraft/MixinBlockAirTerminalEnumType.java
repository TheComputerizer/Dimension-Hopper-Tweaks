package mods.thecomputerizer.dimhoppertweaks.mixin.mods.lightningcraft;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import sblectric.lightningcraft.blocks.BlockAirTerminal.EnumType;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(value = EnumType.class, remap = false)
public class MixinBlockAirTerminalEnumType {
    
    @Shadow @Final @Mutable private static EnumType[] $VALUES;
    
    @Unique private static final EnumType AWAKENED_DRACONIUM = dimhoppertweaks$addTerminalType();
    @Unique private static final EnumType INSANIUM = dimhoppertweaks$addTerminalType();
    @Unique private static final EnumType ULTIMATE = dimhoppertweaks$addTerminalType();
    @Unique private static final EnumType INFINITY = dimhoppertweaks$addTerminalType();
    
    @SuppressWarnings("MixinAnnotationTarget") @Invoker("<init>")
    public static EnumType dimhoppertweaks$invokeInit(int meta) {
        throw new AssertionError();
    }
    
    @SuppressWarnings({"unchecked","DataFlowIssue"}) @Unique
    private static EnumType dimhoppertweaks$addTerminalType() {
        List<EnumType> variants = new ArrayList<EnumType>(Arrays.asList($VALUES));
        EnumType type = dimhoppertweaks$invokeInit(variants.size());
        variants.add(type);
        $VALUES = variants.toArray(new EnumType[0]);
        return type;
    }
}
