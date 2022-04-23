package mods.thecomputerizer.dimensionhoppertweaks.asm;

import com.google.common.collect.ImmutableList;
import net.thesilkminer.mc.fermion.asm.api.LaunchPlugin;
import net.thesilkminer.mc.fermion.asm.api.descriptor.ClassDescriptor;
import net.thesilkminer.mc.fermion.asm.api.descriptor.MethodDescriptor;
import net.thesilkminer.mc.fermion.asm.api.transformer.TransformerData;
import net.thesilkminer.mc.fermion.asm.prefab.transformer.SingleTargetMethodTransformer;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import javax.annotation.Nonnull;
import java.util.function.BiFunction;

public final class DimensionTypeLoggingTransformer extends SingleTargetMethodTransformer {
    private static final int OBJECT = 0x8000;
    private static final int BOOLEAN = 0x8001;
    private static final int INTEGER = 0x8002;

    private static final String STRING_BUILDER = ClassDescriptor.of(StringBuilder.class).toAsmName();

    private final Logger logger;

    DimensionTypeLoggingTransformer(final LaunchPlugin owner, final Logger logger) {
        super(
                TransformerData.Builder.create()
                        .setOwningPlugin(owner)
                        .setName("dimension_type_logger")
                        .setDescription("Logs every DimensionType addition attempt, useful for data collecting and debugging")
                        .setDisabledByDefault()
                        .build(),
                ClassDescriptor.of("net.minecraft.world.DimensionType"),
                MethodDescriptor.of(
                        "register",
                        ImmutableList.of(
                                ClassDescriptor.of(String.class),
                                ClassDescriptor.of(String.class),
                                ClassDescriptor.of(int.class),
                                ClassDescriptor.of(Class.class),
                                ClassDescriptor.of(boolean.class)
                        ),
                        ClassDescriptor.of("net.minecraft.world.DimensionType")
                )
        );
        this.logger = logger;
    }

    @Nonnull
    @Override
    @SuppressWarnings("SpellCheckingInspection")
    protected BiFunction<Integer, MethodVisitor, MethodVisitor> getMethodVisitorCreator() {
        return (v, mv) -> new MethodVisitor(v, mv) {
            //  // access flags 0x9
            //  // signature (Ljava/lang/String;Ljava/lang/String;ILjava/lang/Class<+Lnet/minecraft/world/WorldProvider;>;Z)Lnet/minecraft/world/DimensionType;
            //  // declaration: net.minecraft.world.DimensionType register(java.lang.String, java.lang.String, int, java.lang.Class<? extends net.minecraft.world.WorldProvider>, boolean)
            //  public static register(Ljava/lang/String;Ljava/lang/String;ILjava/lang/Class;Z)Lnet/minecraft/world/DimensionType;
            //   L0
            //    LINENUMBER 87 L0
            //    ALOAD 0
            //    LDC " "
            //    LDC "_"
            //    INVOKEVIRTUAL java/lang/String.replace (Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
            //    INVOKEVIRTUAL java/lang/String.toLowerCase ()Ljava/lang/String;
            //    ASTORE 5
            // <<< INJECTION BEGIN
            //   L800
            //    LINENUMBER 800 L800
            //    NEW java/lang/StringBuilder
            //    DUP
            //    LDC "*** ENTRY FOUND: "
            //    INVOKESPECIAL java/lang/StringBuilder.<init> (Ljava/lang/String;)V
            //    ALOAD 5
            //    INVOKEVIRTUAL java/lang/StringBuilder.append (Ljava/lang/Object;)Ljava/lang/StringBuilder;
            //    BIPUSH 32
            //    INVOKEVIRTUAL java/lang/StringBuilder.append (C)Ljava/lang/StringBuilder;
            //    ILOAD 2
            //    INVOKEVIRTUAL java/lang/StringBuilder.append (I)Ljava/lang/StringBuilder;
            //    BIPUSH 32
            //    INVOKEVIRTUAL java/lang/StringBuilder.append (C)Ljava/lang/StringBuilder;
            //    ALOAD 0
            //    INVOKEVIRTUAL java/lang/StringBuilder.append (Ljava/lang/Object;)Ljava/lang/StringBuilder;
            //    BIPUSH 32
            //    INVOKEVIRTUAL java/lang/StringBuilder.append (C)Ljava/lang/StringBuilder;
            //    ALOAD 1
            //    INVOKEVIRTUAL java/lang/StringBuilder.append (Ljava/lang/Object;)Ljava/lang/StringBuilder;
            //    BIPUSH 32
            //    INVOKEVIRTUAL java/lang/StringBuilder.append (C)Ljava/lang/StringBuilder;
            //    ALOAD 3
            //    INVOKEVIRTUAL java/lang/StringBuilder.append (Ljava/lang/Object;)Ljava/lang/StringBuilder;
            //    BIPUSH 32
            //    INVOKEVIRTUAL java/lang/StringBuilder.append (C)Ljava/lang/StringBuilder;
            //    ILOAD 4
            //    INVOKEVIRTUAL java/lang/StringBuilder.append (Z)Ljava/lang/StringBuilder;
            //    BIPUSH 32
            //    INVOKEVIRTUAL java/lang/StringBuilder.append (C)Ljava/lang/StringBuilder;
            //    LDC " ***"
            //    INVOKEVIRTUAL java/lang/StringBuilder.append (Ljava/lang/String;)V
            //    GETSTATIC java/lang/System.out : Ljava/io/PrintStream;
            //    SWAP
            //    INVOKEVIRTUAL java/io/PrintStream.println (Ljava/lang/Object;)V
            // >>> INJECTION END
            //   L1
            //    LINENUMBER 88 L1
            //    LDC Lnet/minecraft/world/DimensionType;.class
            //    ALOAD 5
            //    GETSTATIC net/minecraft/world/DimensionType.ENUM_ARGS : [Ljava/lang/Class;
            //    ICONST_4
            //    ANEWARRAY java/lang/Object
            //    DUP
            //    ICONST_0
            //    ILOAD 2
            //   L2
            //    LINENUMBER 89 L2
            //    INVOKESTATIC java/lang/Integer.valueOf (I)Ljava/lang/Integer;
            //    AASTORE
            //    DUP
            //    ICONST_1
            //    ALOAD 0
            //    AASTORE
            //    DUP
            //    ICONST_2
            //    ALOAD 1
            //    AASTORE
            //    DUP
            //    ICONST_3
            //    ALOAD 3
            //    AASTORE
            //   L3
            //    LINENUMBER 88 L3
            //    INVOKESTATIC net/minecraftforge/common/util/EnumHelper.addEnum (Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;[Ljava/lang/Object;)Ljava/lang/Enum;
            //    CHECKCAST net/minecraft/world/DimensionType
            //    ASTORE 6
            //   L4
            //    LINENUMBER 90 L4
            //    ALOAD 6
            //    ILOAD 4
            //    INVOKEVIRTUAL net/minecraft/world/DimensionType.setLoadSpawn (Z)Lnet/minecraft/world/DimensionType;
            //    ARETURN
            //   L5
            //    LOCALVARIABLE name Ljava/lang/String; L0 L5 0
            //    LOCALVARIABLE suffix Ljava/lang/String; L0 L5 1
            //    LOCALVARIABLE id I L0 L5 2
            //    LOCALVARIABLE provider Ljava/lang/Class; L0 L5 3
            //    // signature Ljava/lang/Class<+Lnet/minecraft/world/WorldProvider;>;
            //    // declaration: provider extends java.lang.Class<? extends net.minecraft.world.WorldProvider>
            //    LOCALVARIABLE keepLoaded Z L0 L5 4
            //    LOCALVARIABLE enum_name Ljava/lang/String; L1 L5 5
            //    LOCALVARIABLE ret Lnet/minecraft/world/DimensionType; L4 L5 6
            //    MAXSTACK = 7
            //    MAXLOCALS = 7

            private boolean injected = false;

            @Override
            public void visitVarInsn(final int opcode, final int var) {
                super.visitVarInsn(opcode, var);

                if (this.injected || opcode != Opcodes.ASTORE || var != 5) {
                    return;
                }

                logger.info("[DEBUG ENABLED] Found ASTORE 5: injecting debug code");

                final Label l800 = new Label();
                super.visitLabel(l800);
                super.visitLineNumber(800, l800);
                super.visitTypeInsn(Opcodes.NEW, STRING_BUILDER);
                super.visitInsn(Opcodes.DUP);
                super.visitLdcInsn("*** ENTRY FOUND: ");
                super.visitMethodInsn(Opcodes.INVOKESPECIAL, STRING_BUILDER, "<init>", "(Ljava/lang/String;)V", false);
                this.appendElement(OBJECT, 5);
                this.appendElement(INTEGER, 2);
                this.appendElement(OBJECT, 0);
                this.appendElement(OBJECT, 1);
                this.appendElement(OBJECT, 3);
                this.appendElement(BOOLEAN, 4);
                this.visitLdcInsn(" ***");
                super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, STRING_BUILDER, "append", String.format("(Ljava/lang/String;)L%s;", STRING_BUILDER), false);
                super.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                super.visitInsn(Opcodes.SWAP);
                super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/Object;)V", false);

                this.injected = true;
            }

            private void appendElement(final int type, final int slot) {
                super.visitVarInsn(this.load(type), slot);
                super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, STRING_BUILDER, "append", String.format("(%s)L%s;", this.desc(type), STRING_BUILDER), false);
                super.visitIntInsn(Opcodes.BIPUSH, 32);
                super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, STRING_BUILDER, "append", String.format("(C)L%s;", STRING_BUILDER), false);
            }

            private int load(final int type) {
                switch (type) {
                    case OBJECT:
                        return Opcodes.ALOAD;
                    case BOOLEAN:
                    case INTEGER:
                        return Opcodes.ILOAD;
                }
                throw new IllegalArgumentException(Integer.toString(type, 16));
            }

            private String desc(final int type) {
                switch (type) {
                    case OBJECT:
                        return "Ljava/lang/Object;";
                    case INTEGER:
                        return "I";
                    case BOOLEAN:
                        return "Z";
                }
                throw new IllegalArgumentException(Integer.toString(type, 16));
            }

        };
    }
}