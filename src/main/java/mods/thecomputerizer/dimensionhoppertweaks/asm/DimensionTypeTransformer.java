package mods.thecomputerizer.dimensionhoppertweaks.asm;

import com.google.common.collect.ImmutableList;
import net.thesilkminer.mc.fermion.asm.api.LaunchPlugin;
import net.thesilkminer.mc.fermion.asm.api.MappingUtilities;
import net.thesilkminer.mc.fermion.asm.api.descriptor.ClassDescriptor;
import net.thesilkminer.mc.fermion.asm.api.descriptor.MethodDescriptor;
import net.thesilkminer.mc.fermion.asm.api.transformer.TransformerData;
import net.thesilkminer.mc.fermion.asm.prefab.AbstractTransformer;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import javax.annotation.Nonnull;
import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@SuppressWarnings("SpellCheckingInspection")
public final class DimensionTypeTransformer extends AbstractTransformer {

    private static final class StaticInitializationMethodVisitor extends MethodVisitor {
        //  // access flags 0x8
        //  static <clinit>()V
        //   L0
        //    LINENUMBER 8 L0
        //    NEW net/minecraft/world/DimensionType
        //    DUP
        //    LDC "OVERWORLD"
        //    ICONST_0
        //    ICONST_0
        //    LDC "overworld"
        //    LDC ""
        //    LDC Lnet/minecraft/world/WorldProviderSurface;.class
        //    INVOKESPECIAL net/minecraft/world/DimensionType.<init> (Ljava/lang/String;IILjava/lang/String;Ljava/lang/String;Ljava/lang/Class;)V
        //    PUTSTATIC net/minecraft/world/DimensionType.OVERWORLD : Lnet/minecraft/world/DimensionType;
        //   L1
        //    LINENUMBER 9 L1
        //    NEW net/minecraft/world/DimensionType
        //    DUP
        //    LDC "NETHER"
        //    ICONST_1
        //    ICONST_M1
        //    LDC "the_nether"
        //    LDC "_nether"
        //    LDC Lnet/minecraft/world/WorldProviderHell;.class
        //    INVOKESPECIAL net/minecraft/world/DimensionType.<init> (Ljava/lang/String;IILjava/lang/String;Ljava/lang/String;Ljava/lang/Class;)V
        //    PUTSTATIC net/minecraft/world/DimensionType.NETHER : Lnet/minecraft/world/DimensionType;
        //   L2
        //    LINENUMBER 10 L2
        //    NEW net/minecraft/world/DimensionType
        //    DUP
        //    LDC "THE_END"
        //    ICONST_2
        //    ICONST_1
        //    LDC "the_end"
        //    LDC "_end"
        //    LDC Lnet/minecraft/world/WorldProviderEnd;.class
        //    INVOKESPECIAL net/minecraft/world/DimensionType.<init> (Ljava/lang/String;IILjava/lang/String;Ljava/lang/String;Ljava/lang/Class;)V
        //    PUTSTATIC net/minecraft/world/DimensionType.THE_END : Lnet/minecraft/world/DimensionType;
        //   L3
        //    LINENUMBER 6 L3
        //    ICONST_3
        // <<< INJECTION BEGIN
        //    POP
        // [for every new dimension {i = index, data = dimension data}]
        //   L{800 + i}
        //    LINENUMBER {800 + i} L{800 + i}
        //    NEW net/minecraft/world/DimensionType
        //    DUP
        //    LDC {data.enumerationName}
        //    [load {3 + i}]
        //    [load {data.id}]
        //    LDC {data.dimensionName}
        //    LDC {data.suffix}
        //    INVOKEDYNAMIC get()Ljava/util/function/Supplier; [
        //      // handle kind 0x6 : INVOKESTATIC
        //      java/lang/invoke/LambdaMetafactory.metafactory(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
        //      // arguments:
        //      ()Ljava/lang/Object;,
        //      // handle kind 0x6 : INVOKESTATIC
        //      net/minecraft/world/DimensionType.fermion-injected-lambda$dimension-hopper-tweaks$supplier$targeting-{i}()Ljava/lang/Class;,
        //      ()Ljava/lang/Class;
        //    ]
        //    INVOKESTATIC mods/thecomputerizer/dimensionhoppertweaks/util/Lazy.of (Ljava/util/function/Supplier;)Lmods/thecomputerizer/dimensionhoppertweaks/util/Lazy;
        //    CHECKCAST java/util/function/Supplier
        //    [load {data.shouldKeepLoaded}]
        //    INVOKESPECIAL net/minecraft/world/DimensionType.<init> (Ljava/lang/String;IILjava/lang/String;Ljava/lang/String;Ljava/util/function/Supplier;Z)V
        //    PUTSTATIC net/minecraft/world/DimensionType.{data.enumerationName} : Lnet/minecraft/world/DimensionType;
        // [end for]
        //   L{800 + allDimData.size}
        //    LINENUMBER {800 + allDimData.size} L{800 + allDimData.size}
        //    [load {allDimData.size + 3}]
        // >>> INJECTION END
        //    ANEWARRAY net/minecraft/world/DimensionType
        //    DUP
        //    ICONST_0
        //    GETSTATIC net/minecraft/world/DimensionType.OVERWORLD : Lnet/minecraft/world/DimensionType;
        //    AASTORE
        //    DUP
        //    ICONST_1
        //    GETSTATIC net/minecraft/world/DimensionType.NETHER : Lnet/minecraft/world/DimensionType;
        //    AASTORE
        //    DUP
        //    ICONST_2
        //    GETSTATIC net/minecraft/world/DimensionType.THE_END : Lnet/minecraft/world/DimensionType;
        //    AASTORE
        // <<< INJECTION BEGIN
        // [for every new dimension {i = index, data = dimension data}]
        //    DUP
        //    [load {3 + i}]
        //    GETSTATIC net/minecraft/world/DimensionType.{data.enumerationName} : Lnet/minecraft/world/DimensionType;
        //    AASTORE
        // [end for]
        // >>> INJECTION END
        //    PUTSTATIC net/minecraft/world/DimensionType.$VALUES : [Lnet/minecraft/world/DimensionType;
        //   L4
        //    LINENUMBER 83 L4
        //    ICONST_4
        //    ANEWARRAY java/lang/Class
        //    DUP
        //    ICONST_0
        //    GETSTATIC java/lang/Integer.TYPE : Ljava/lang/Class;
        //    AASTORE
        //    DUP
        //    ICONST_1
        //    LDC Ljava/lang/String;.class
        //    AASTORE
        //    DUP
        //    ICONST_2
        //    LDC Ljava/lang/String;.class
        //    AASTORE
        //    DUP
        //    ICONST_3
        //    LDC Ljava/lang/Class;.class
        //    AASTORE
        //    PUTSTATIC net/minecraft/world/DimensionType.ENUM_ARGS : [Ljava/lang/Class;
        //   L5
        //    LINENUMBER 84 L5
        //    LDC Lnet/minecraft/world/DimensionType;.class
        //    GETSTATIC net/minecraft/world/DimensionType.ENUM_ARGS : [Ljava/lang/Class;
        //    INVOKESTATIC net/minecraftforge/common/util/EnumHelper.testEnum (Ljava/lang/Class;[Ljava/lang/Class;)V
        //    RETURN
        // <<< OVERWRITE BEGIN
        //    MAXSTACK = 8
        // === OVERWRITE WITH
        //    MAXSTACK = 9
        // >>> OVERWRITE END
        //    MAXLOCALS = 0

        private final Logger logger;

        private boolean hasInjectedCreation;
        private boolean hasInjectedArray;

        StaticInitializationMethodVisitor(final int version, final MethodVisitor parent, final Logger logger) {
            super(version, parent);
            this.logger = logger;
            this.hasInjectedCreation = false;
            this.hasInjectedArray = false;
        }

        @Override
        public void visitTypeInsn(final int opcode, final String type) {
            if (!this.hasInjectedCreation && Opcodes.ANEWARRAY == opcode && DIMENSION_TYPE.equals(type)) {
                this.logger.info("Found anewarray for {}: injecting all dimension creation code", DIMENSION_TYPE);

                super.visitInsn(Opcodes.POP);
                final int size = DIMENSIONS_TO_ADD.size();

                this.logger.info("There is a total of {} dimensions to add", size);

                for (int i = 0; i < size; ++i) {
                    final DimensionInjectionData data = DIMENSIONS_TO_ADD.get(i);

                    final Label label = new Label();
                    super.visitLabel(label);
                    super.visitLineNumber(800 + i, label);
                    super.visitTypeInsn(Opcodes.NEW, DIMENSION_TYPE);
                    super.visitInsn(Opcodes.DUP);
                    super.visitLdcInsn(data.enumerationName());
                    this.load(3 + i);
                    this.load(data.id());
                    super.visitLdcInsn(data.dimensionName());
                    super.visitLdcInsn(data.suffix());
                    super.visitInvokeDynamicInsn(
                            "get",
                            "()Ljava/util/function/Supplier;",
                            new Handle(Opcodes.H_INVOKESTATIC, LAMBDA_METAFACTORY, METAFACTORY_NAME, METAFACTORY_DESC, false),
                            Type.getType("()Ljava/lang/Object;"),
                            new Handle(Opcodes.H_INVOKESTATIC, DIMENSION_TYPE, targetingSupplierName(i), "()Ljava/lang/Class;", false),
                            Type.getType("()Ljava/lang/Class;")
                    );
                    super.visitMethodInsn(Opcodes.INVOKESTATIC, LAZY, LAZY_OF_NAME, LAZY_OF_DESC, false);
                    super.visitTypeInsn(Opcodes.CHECKCAST, SUPPLIER);
                    this.load(data.shouldKeepLoaded());
                    super.visitMethodInsn(Opcodes.INVOKESPECIAL, DIMENSION_TYPE, DT_NEW_INIT_NAME, DT_NEW_INIT_DESC, false);
                    super.visitFieldInsn(Opcodes.PUTSTATIC, DIMENSION_TYPE, data.enumerationName(), String.format("L%s;", DIMENSION_TYPE));
                }

                final Label label = new Label();
                super.visitLabel(label);
                super.visitLineNumber(800 + size, label);
                this.load(3 + size);

                this.hasInjectedCreation = true;
            }
            super.visitTypeInsn(opcode, type);
        }

        @Override
        public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
            if (!this.hasInjectedArray && Opcodes.PUTSTATIC == opcode && DIMENSION_TYPE.equals(owner) && String.format("[L%s;", DIMENSION_TYPE).equals(desc)) {
                this.logger.info("Found putstatic for values field ({} should be $VALUES), injecting gatherer", name);

                for (int i = 0, s = DIMENSIONS_TO_ADD.size(); i < s; ++i) {
                    super.visitInsn(Opcodes.DUP);
                    this.load(3 + i);
                    super.visitFieldInsn(Opcodes.GETSTATIC, DIMENSION_TYPE, DIMENSIONS_TO_ADD.get(i).enumerationName(), String.format("L%s;", DIMENSION_TYPE));
                    super.visitInsn(Opcodes.AASTORE);
                }

                this.hasInjectedArray = true;
            }
            super.visitFieldInsn(opcode, owner, name, desc);
        }

        @Override
        public void visitMaxs(final int maxStack, final int maxLocals) {
            super.visitMaxs(maxStack + 1, maxLocals);
        }

        private void load(final int number) {
            switch (number) {
                case -1:
                    super.visitInsn(Opcodes.ICONST_M1);
                    break;
                case 0:
                    super.visitInsn(Opcodes.ICONST_0);
                    break;
                case 1:
                    super.visitInsn(Opcodes.ICONST_1);
                    break;
                case 2:
                    super.visitInsn(Opcodes.ICONST_2);
                    break;
                case 3:
                    super.visitInsn(Opcodes.ICONST_3);
                    break;
                case 4:
                    super.visitInsn(Opcodes.ICONST_4);
                    break;
                case 5:
                    super.visitInsn(Opcodes.ICONST_5);
                    break;
                default:
                    if (Byte.MIN_VALUE <= number && number <= Byte.MAX_VALUE) {
                        super.visitIntInsn(Opcodes.BIPUSH, number);
                    } else if (Short.MIN_VALUE <= number && number <= Short.MAX_VALUE) {
                        super.visitIntInsn(Opcodes.SIPUSH, number);
                    } else {
                        super.visitLdcInsn(number);
                    }
            }
        }

        private void load(final boolean bool) {
            load(bool? 1 : 0);
        }
    }

    private static final class RegisterMethodVisitor extends MethodVisitor {
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
        // <<< OVERWRITE BEGIN
        //    INVOKESTATIC net/minecraftforge/common/util/EnumHelper.addEnum (Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;[Ljava/lang/Object;)Ljava/lang/Enum;
        // === OVERWRITE WITH
        //    POP
        //    POP
        //    POP
        //    POP
        //    ALOAD 0
        //    INVOKESTATIC net/minecraft/world/DimensionType.fermion-injected-method$dimension-hopper-tweaks$find (Ljava/lang/String;)Lnet/minecraft/world/DimensionType;
        // >>> OVERWRITE END
        //    CHECKCAST net/minecraft/world/DimensionType
        //    ASTORE 6
        //   L4
        //    LINENUMBER 90 L4
        //    ALOAD 6
        //    ILOAD 4
        // <<< OVERWRITE BEGIN
        //    INVOKEVIRTUAL net/minecraft/world/DimensionType.setLoadSpawn (Z)Lnet/minecraft/world/DimensionType;
        // === OVERWRITE WITH
        //    POP
        // >>> OVERWRITE END
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

        private final Logger logger;

        private boolean hasInjectedFind;
        private boolean hasInjectedLoadSpawn;

        RegisterMethodVisitor(final int version, final MethodVisitor parent, final Logger logger) {
            super(version, parent);
            this.logger = logger;
            this.hasInjectedFind = false;
            this.hasInjectedLoadSpawn = false;
        }

        @Override
        public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
            if (!this.hasInjectedFind && Opcodes.INVOKESTATIC == opcode && "net/minecraftforge/common/util/EnumHelper".equals(owner) && "addEnum".equals(name)) {
                this.logger.info("Found EnumHelper.addEnum call in register: wiping");

                for (int i = 0; i < 4; ++i) super.visitInsn(Opcodes.POP);
                super.visitVarInsn(Opcodes.ALOAD, 0);
                super.visitMethodInsn(Opcodes.INVOKESTATIC, DIMENSION_TYPE, FINDER_NAME, FINDER_DESC, false);

                this.hasInjectedFind = true;
                return;
            }

            if (!this.hasInjectedLoadSpawn && this.hasInjectedFind && Opcodes.INVOKEVIRTUAL == opcode && DIMENSION_TYPE.equals(owner) && "setLoadSpawn".equals(name)) {
                this.logger.info("Found override for setLoadSpawn: wiping");

                super.visitInsn(Opcodes.POP);

                this.hasInjectedLoadSpawn = true;
                return;
            }

            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }
    }

    private static final class ConstructorVisitor extends MethodVisitor {
        //  // access flags 0x2
        //  // signature (ILjava/lang/String;Ljava/lang/String;Ljava/lang/Class<+Lnet/minecraft/world/WorldProvider;>;)V
        //  // declaration: void <init>(int, java.lang.String, java.lang.String, java.lang.Class<? extends net.minecraft.world.WorldProvider>)
        //  private <init>(Ljava/lang/String;IILjava/lang/String;Ljava/lang/String;Ljava/lang/Class;)V
        //   L0
        //    LINENUMBER 19 L0
        //    ALOAD 0
        //    ALOAD 1
        //    ILOAD 2
        //    INVOKESPECIAL java/lang/Enum.<init> (Ljava/lang/String;I)V
        //   L1
        //    LINENUMBER 16 L1
        //    ALOAD 0
        //    ICONST_0
        //    PUTFIELD net/minecraft/world/DimensionType.shouldLoadSpawn : Z
        //   L2
        //    LINENUMBER 20 L2
        //    ALOAD 0
        //    ILOAD 3
        //    PUTFIELD net/minecraft/world/DimensionType.id : I
        //   L3
        //    LINENUMBER 21 L3
        //    ALOAD 0
        //    ALOAD 4
        //    PUTFIELD net/minecraft/world/DimensionType.name : Ljava/lang/String;
        //   L4
        //    LINENUMBER 22 L4
        //    ALOAD 0
        //    ALOAD 5
        //    PUTFIELD net/minecraft/world/DimensionType.suffix : Ljava/lang/String;
        //   L5
        //    LINENUMBER 23 L5
        //    ALOAD 0
        //    ALOAD 6
        //    PUTFIELD net/minecraft/world/DimensionType.clazz : Ljava/lang/Class;
        // <<< INJECTION BEGIN
        //   L800
        //    LINENUMBER 1000 L800
        //    ALOAD 0
        //    ALOAD 6
        //    INVOKEDYNAMIC get(Ljava/lang/Class;)Ljava/util/function/Supplier; [
        //      // handle kind 0x6 : INVOKESTATIC
        //      java/lang/invoke/LambdaMetafactory.metafactory(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
        //      // arguments:
        //      ()Ljava/lang/Object;,
        //      // handle kind 0x6 : INVOKESTATIC
        //      net/minecraft/world/DimensionType.fermion-injected-lambda$dimension-hopper-tweaks$supplier$identity-for-class(Ljava/lang/Class;)Ljava/lang/Class;
        //      ()Ljava/lang/Class;
        //    ]
        //    INVOKESTATIC mods/thecomputerizer/dimensionhoppertweaks/util/Lazy.of (Ljava/util/function/Supplier;)Lmods/thecomputerizer/dimensionhoppertweaks/util/Lazy;
        //    CHECKCAST java/util/function/Supplier
        //    PUTFIELD net/minecraft/world/DimensionType.fermion-injected-field$dimension-hopper-tweaks$classSupplier : Ljava/util/function/Supplier;
        // >>> INJECTION END
        //   L6
        //    LINENUMBER 24 L6
        //    ALOAD 0
        //    ILOAD 3
        //    IFNE L7
        //    ICONST_1
        //    GOTO L8
        //   L7
        //   FRAME FULL [net/minecraft/world/DimensionType java/lang/String I I java/lang/String java/lang/String java/lang/Class] [net/minecraft/world/DimensionType]
        //    ICONST_0
        //   L8
        //   FRAME FULL [net/minecraft/world/DimensionType java/lang/String I I java/lang/String java/lang/String java/lang/Class] [net/minecraft/world/DimensionType I]
        //    PUTFIELD net/minecraft/world/DimensionType.shouldLoadSpawn : Z
        //   L9
        //    LINENUMBER 25 L9
        //    RETURN
        //   L10
        //    LOCALVARIABLE this Lnet/minecraft/world/DimensionType; L0 L10 0
        //    LOCALVARIABLE idIn I L0 L10 3
        //    LOCALVARIABLE nameIn Ljava/lang/String; L0 L10 4
        //    LOCALVARIABLE suffixIn Ljava/lang/String; L0 L10 5
        //    LOCALVARIABLE clazzIn Ljava/lang/Class; L0 L10 6
        //    // signature Ljava/lang/Class<+Lnet/minecraft/world/WorldProvider;>;
        //    // declaration: clazzIn extends java.lang.Class<? extends net.minecraft.world.WorldProvider>
        //    MAXSTACK = 3
        //    MAXLOCALS = 7

        private final Logger logger;

        private boolean injected;

        ConstructorVisitor(final int version, final MethodVisitor parent, final Logger logger) {
            super(version, parent);
            this.logger = logger;
            this.injected = false;
        }

        @Override
        public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
            super.visitFieldInsn(opcode, owner, name, desc);

            if (!this.injected && Opcodes.PUTFIELD == opcode && MappingUtilities.INSTANCE.mapField("field_186077_g").equals(name) && String.format("L%s;", CLASS).equals(desc)) {
                this.logger.info("Found putfield for clazz in normal <init>, injecting supplier init");

                final Label label = new Label();
                super.visitLabel(label);
                super.visitLineNumber(1000, label);
                super.visitVarInsn(Opcodes.ALOAD, 0);
                super.visitVarInsn(Opcodes.ALOAD, 6);
                super.visitInvokeDynamicInsn(
                        "get",
                        "(Ljava/lang/Class;)Ljava/util/function/Supplier;",
                        new Handle(Opcodes.H_INVOKESTATIC, LAMBDA_METAFACTORY, METAFACTORY_NAME, METAFACTORY_DESC, false),
                        Type.getType("()Ljava/lang/Object;"),
                        new Handle(Opcodes.H_INVOKESTATIC, DIMENSION_TYPE, CLASS_IDENTITY_NAME, CLASS_IDENTITY_DESC, false),
                        Type.getType("()Ljava/lang/Class;")
                );
                super.visitMethodInsn(Opcodes.INVOKESTATIC, LAZY, LAZY_OF_NAME, LAZY_OF_DESC, false);
                super.visitTypeInsn(Opcodes.CHECKCAST, SUPPLIER);
                super.visitFieldInsn(Opcodes.PUTFIELD, DIMENSION_TYPE, CLASS_SUPPLIER_FIELD_NAME, String.format("L%s;", SUPPLIER));

                this.injected = true;
            }
        }
    }

    private static final class CreateDimensionMethodVisitor extends MethodVisitor {
        //  // access flags 0x1
        //  public createDimension()Lnet/minecraft/world/WorldProvider;
        //    TRYCATCHBLOCK L0 L1 L2 java/lang/NoSuchMethodException
        //    TRYCATCHBLOCK L0 L1 L3 java/lang/reflect/InvocationTargetException
        //    TRYCATCHBLOCK L0 L1 L4 java/lang/InstantiationException
        //    TRYCATCHBLOCK L0 L1 L5 java/lang/IllegalAccessException
        //   L0
        //    LINENUMBER 46 L0
        //    ALOAD 0
        // <<< OVERWRITE BEGIN
        //    GETFIELD net/minecraft/world/DimensionType.clazz : Ljava/lang/Class;
        // === OVERWRITE WITH
        //    GETFIELD net/minecraft/world/DimensionType.fermion-injected-field$dimension-hopper-tweaks$classSupplier : Ljava/util/function/Supplier;
        //    INVOKEINTERFACE java/util/function/Supplier.get ()Ljava/lang/Object; (itf)
        //    CHECKCAST java/lang/Class
        // >>> OVERWRITE END
        //    ICONST_0
        //    ANEWARRAY java/lang/Class
        //    INVOKEVIRTUAL java/lang/Class.getConstructor ([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;
        //    ASTORE 1
        //   L6
        //    LINENUMBER 47 L6
        //    ALOAD 1
        //    ICONST_0
        //    ANEWARRAY java/lang/Object
        //    INVOKEVIRTUAL java/lang/reflect/Constructor.newInstance ([Ljava/lang/Object;)Ljava/lang/Object;
        //    CHECKCAST net/minecraft/world/WorldProvider
        //   L1
        //    ARETURN
        //   L2
        //    LINENUMBER 49 L2
        //   FRAME SAME1 java/lang/NoSuchMethodException
        //    ASTORE 1
        //   L7
        //    LINENUMBER 51 L7
        //    NEW java/lang/Error
        //    DUP
        //    LDC "Could not create new dimension"
        //    ALOAD 1
        //    INVOKESPECIAL java/lang/Error.<init> (Ljava/lang/String;Ljava/lang/Throwable;)V
        //    ATHROW
        //   L3
        //    LINENUMBER 53 L3
        //   FRAME SAME1 java/lang/reflect/InvocationTargetException
        //    ASTORE 1
        //   L8
        //    LINENUMBER 55 L8
        //    NEW java/lang/Error
        //    DUP
        //    LDC "Could not create new dimension"
        //    ALOAD 1
        //    INVOKESPECIAL java/lang/Error.<init> (Ljava/lang/String;Ljava/lang/Throwable;)V
        //    ATHROW
        //   L4
        //    LINENUMBER 57 L4
        //   FRAME SAME1 java/lang/InstantiationException
        //    ASTORE 1
        //   L9
        //    LINENUMBER 59 L9
        //    NEW java/lang/Error
        //    DUP
        //    LDC "Could not create new dimension"
        //    ALOAD 1
        //    INVOKESPECIAL java/lang/Error.<init> (Ljava/lang/String;Ljava/lang/Throwable;)V
        //    ATHROW
        //   L5
        //    LINENUMBER 61 L5
        //   FRAME SAME1 java/lang/IllegalAccessException
        //    ASTORE 1
        //   L10
        //    LINENUMBER 63 L10
        //    NEW java/lang/Error
        //    DUP
        //    LDC "Could not create new dimension"
        //    ALOAD 1
        //    INVOKESPECIAL java/lang/Error.<init> (Ljava/lang/String;Ljava/lang/Throwable;)V
        //    ATHROW
        //   L11
        //    LOCALVARIABLE constructor Ljava/lang/reflect/Constructor; L6 L2 1
        //    // signature Ljava/lang/reflect/Constructor<+Lnet/minecraft/world/WorldProvider;>;
        //    // declaration: constructor extends java.lang.reflect.Constructor<? extends net.minecraft.world.WorldProvider>
        //    LOCALVARIABLE nosuchmethodexception Ljava/lang/NoSuchMethodException; L7 L3 1
        //    LOCALVARIABLE invocationtargetexception Ljava/lang/reflect/InvocationTargetException; L8 L4 1
        //    LOCALVARIABLE instantiationexception Ljava/lang/InstantiationException; L9 L5 1
        //    LOCALVARIABLE illegalaccessexception Ljava/lang/IllegalAccessException; L10 L11 1
        //    LOCALVARIABLE this Lnet/minecraft/world/DimensionType; L0 L11 0
        //    MAXSTACK = 4
        //    MAXLOCALS = 2

        private final Logger logger;

        private boolean injected;

        CreateDimensionMethodVisitor(final int version, final MethodVisitor parent, final Logger logger) {
            super(version, parent);
            this.logger = logger;
            this.injected = false;
        }

        @Override
        public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
            if (!this.injected && Opcodes.GETFIELD == opcode && DIMENSION_TYPE.equals(owner) && MappingUtilities.INSTANCE.mapField("field_186077_g").equals(name)) {
                this.logger.info("Identified attempt to read clazz: redirecting to supplier");

                super.visitFieldInsn(Opcodes.GETFIELD, DIMENSION_TYPE, CLASS_SUPPLIER_FIELD_NAME, String.format("L%s;", SUPPLIER));
                super.visitMethodInsn(Opcodes.INVOKEINTERFACE, SUPPLIER, "get", "()Ljava/lang/Object;", true);
                super.visitTypeInsn(Opcodes.CHECKCAST, CLASS);

                this.injected = true;
                return;
            }

            super.visitFieldInsn(opcode, owner, name, desc);
        }
    }

    private static final class FinderMethodInjector extends MethodVisitor {
        //  // access flags 0x101A
        //  private static final synthetic fermion-injected-method$dimension-hopper-tweaks$find(Ljava/lang/String;)Lnet/minecraft/world/DimensionType;
        //   L0
        //    LINENUMBER 400 L0
        //    ALOAD 0
        //    INVOKESTATIC net/minecraft/world/DimensionType.func_193417_a (Ljava/lang/String;)Lnet/minecraft/world/DimensionType;
        //    ARETURN
        //   L1
        //    LOCALVARIABLE $$0 Ljava/lang/String; L0 L1 0
        //    MAXSTACK = 1
        //    MAXLOCALS = 1

        private final Logger logger;

        private FinderMethodInjector(final int version, final MethodVisitor parent, final Logger logger) {
            super(version, parent);
            this.logger = logger;
        }

        public static void inject(final int version, final ClassVisitor visitor, final Logger logger) {
            final MethodVisitor parent = visitor.visitMethod(
                    Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL | Opcodes.ACC_SYNTHETIC,
                    FINDER_NAME,
                    FINDER_DESC,
                    null,
                    null
            );
            final MethodVisitor creator = new FinderMethodInjector(version, parent, logger);
            creator.visitCode();
        }

        @Override
        public void visitCode() {
            this.logger.info("Creating code for finder method");

            super.visitCode();

            final Label l0 = new Label();
            super.visitLabel(l0);
            super.visitLineNumber(400, l0);
            super.visitVarInsn(Opcodes.ALOAD, 0);
            super.visitMethodInsn(Opcodes.INVOKESTATIC, DIMENSION_TYPE, MappingUtilities.INSTANCE.mapMethod("func_193417_a"), FINDER_DESC, false);
            super.visitInsn(Opcodes.ARETURN);

            final Label l1 = new Label();
            super.visitLabel(l1);

            super.visitLocalVariable("$$0", "Ljava/lang/String;", null, l0, l1, 0);
            super.visitMaxs(1, 1);

            super.visitEnd();
        }
    }

    private static final class ConstructorInjector extends MethodVisitor {
        //  // access flags 0x1002
        //  // signature (ILjava/lang/String;Ljava/lang/String;Ljava/util/function/Supplier<Ljava/lang/Class<+Lnet/minecraft/world/WorldProvider;>;>;Z)V
        //  // declaration: void <init>(int, java.lang.String, java.lang.String, java.util.function.Supplier<java.lang.Class<? extends net.minecraft.world.WorldProvider>>, boolean)
        //  private synthetic <init>(Ljava/lang/String;IILjava/lang/String;Ljava/lang/String;Ljava/util/function/Supplier;Z)V
        //   L0
        //    LINENUMBER 500 L0
        //    ALOAD 0
        //    ALOAD 1
        //    ILOAD 2
        //    INVOKESPECIAL java/lang/Enum.<init> (Ljava/lang/String;I)V
        //   L1
        //    LINENUMBER 501 L1
        //    ALOAD 0
        //    ILOAD 3
        //    PUTFIELD net/minecraft/world/DimensionType.field_186074_d : I
        //   L2
        //    LINENUMBER 502 L2
        //    ALOAD 0
        //    ALOAD 4
        //    PUTFIELD net/minecraft/world/DimensionType.field_186075_e : Ljava/lang/String;
        //   L3
        //    LINENUMBER 503 L3
        //    ALOAD 0
        //    ALOAD 5
        //    PUTFIELD net/minecraft/world/DimensionType.field_186076_f : Ljava/lang/String;
        //   L4
        //    LINENUMBER 504 L4
        //    ALOAD 0
        //    ALOAD 6
        //    PUTFIELD net/minecraft/world/DimensionType.fermion-injected-field$dimension-hopper-tweaks$classSupplier : Ljava/util/function/Supplier;
        //   L5
        //    LINENUMBER 505 L5
        //    ALOAD 0
        //    ILOAD 7
        //    PUTFIELD net/minecraft/world/DimensionType.shouldLoadSpawn : Z
        //   L6
        //    LINENUMBER 506 L6
        //    ACONST_NULL
        //    ASTORE 8
        //   L7
        //    LINENUMBER 507 L7
        //    ALOAD 0
        //    ALOAD 8
        //    PUTFIELD net/minecraft/world/DimensionType.field_186077_g : Ljava/lang/Class;
        //   L8
        //    LINENUMBER 508 L8
        //    RETURN
        //   L9
        //    LOCALVARIABLE this Lnet/minecraft/world/DimensionType; L0 L9 0
        //    LOCALVARIABLE $$1 Ljava/lang/String; L0 L9 1
        //    LOCALVARIABLE $$2 I L0 L9 2
        //    LOCALVARIABLE $$3 I L0 L9 3
        //    LOCALVARIABLE $$4 Ljava/lang/String; L0 L9 4
        //    LOCALVARIABLE $$5 Ljava/lang/String; L0 L9 5
        //    LOCALVARIABLE $$6 Ljava/util/function/Supplier; L0 L9 6
        //    // signature Ljava/util/function/Supplier<Ljava/lang/Class<+Lnet/minecraft/world/WorldProvider;>;>;
        //    // declaration: $$6 extends java.util.function.Supplier<java.lang.Class<? extends net.minecraft.world.WorldProvider>>
        //    LOCALVARIABLE $$7 Z L0 L9 7
        //    LOCALVARIABLE $$8 Ljava/lang/Class; L7 L9 8
        //    // signature Ljava/lang/Class<*>;
        //    // declaration: $$8 extends java.lang.Class<?>
        //    MAXSTACK = 3
        //    MAXLOCALS = 9

        private final Logger logger;

        private ConstructorInjector(final int version, final MethodVisitor parent, final Logger logger) {
            super(version, parent);
            this.logger = logger;
        }

        public static void inject(final int version, final ClassVisitor visitor, final Logger logger) {
            final MethodVisitor parent = visitor.visitMethod(Opcodes.ACC_PRIVATE | Opcodes.ACC_SYNTHETIC, DT_NEW_INIT_NAME, DT_NEW_INIT_DESC, null, null);
            final MethodVisitor creator = new ConstructorInjector(version, parent, logger);
            creator.visitCode();
        }

        @Override
        public void visitCode() {
            this.logger.info("Generating new synthetic constructor for supplier-based access");

            super.visitCode();

            final Label l0 = new Label();
            super.visitLabel(l0);
            super.visitLineNumber(500, l0);
            super.visitVarInsn(Opcodes.ALOAD, 0);
            super.visitVarInsn(Opcodes.ALOAD, 1);
            super.visitVarInsn(Opcodes.ILOAD, 2);
            super.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Enum", "<init>", "(Ljava/lang/String;I)V", false);

            this.field(INTEGER, 3, MappingUtilities.INSTANCE.mapField("field_186074_d"), "I");
            this.field(OBJECT, 4, MappingUtilities.INSTANCE.mapField("field_186075_e"), "Ljava/lang/String;");
            this.field(OBJECT, 5, MappingUtilities.INSTANCE.mapField("field_186076_f"), "Ljava/lang/String;");
            this.field(OBJECT, 6, CLASS_SUPPLIER_FIELD_NAME, String.format("L%s;", SUPPLIER));
            this.field(INTEGER, 7, "shouldLoadSpawn", "Z");

            final Label l6 = new Label();
            super.visitLabel(l6);
            super.visitLineNumber(506, l6);
            super.visitInsn(Opcodes.ACONST_NULL);
            super.visitVarInsn(Opcodes.ASTORE, 8);

            final Label l7 = this.field(OBJECT, 8, MappingUtilities.INSTANCE.mapField("field_186077_g"), String.format("L%s;", CLASS));

            final Label l8 = new Label();
            super.visitLabel(l8);
            super.visitLineNumber(508, l8);
            super.visitInsn(Opcodes.RETURN);

            final Label l9 = new Label();
            super.visitLabel(l9);

            super.visitLocalVariable("this", String.format("L%s;", DIMENSION_TYPE), null, l0, l9, 0);
            super.visitLocalVariable("$$1", "Ljava/lang/String;", null, l0, l9, 1);
            super.visitLocalVariable("$$2", "I", null, l0, l9, 2);
            super.visitLocalVariable("$$3", "I", null, l0, l9, 3);
            super.visitLocalVariable("$$4", "Ljava/lang/String;", null, l0, l9, 4);
            super.visitLocalVariable("$$5", "Ljava/lang/String;", null, l0, l9, 5);
            super.visitLocalVariable("$$6", String.format("L%s;", SUPPLIER), String.format("L%s<L%s<+Lnet/minecraft/world/WorldProvider;>;>;", SUPPLIER, CLASS), l0, l9, 7);
            super.visitLocalVariable("$$7", "Z", null, l0, l9, 7);
            super.visitLocalVariable("$$8", String.format("L%s;", CLASS), String.format("L%s<*>;", CLASS), l7, l9, 8);
            super.visitMaxs(3, 9);

            super.visitEnd();
        }

        private Label field(final int type, final int index, final String name, final String desc) {
            final Label label = new Label();
            super.visitLabel(label);
            super.visitLineNumber(498 + index + (index == 8? 1 : 0), label);
            super.visitVarInsn(Opcodes.ALOAD, 0);
            this.load(type, index);
            super.visitFieldInsn(Opcodes.PUTFIELD, DIMENSION_TYPE, name, desc);
            return label;
        }

        private void load(final int type, final int index) {
            switch (type) {
                case OBJECT:
                    super.visitVarInsn(Opcodes.ALOAD, index);
                    break;
                case INTEGER:
                    super.visitVarInsn(Opcodes.ILOAD, index);
                    break;
                default:
                    throw new IllegalArgumentException(Integer.toString(type, 16));
            }
        }
    }

    private static final class FieldInjector {
        // [for every dimension {data = dimension data}]
        //  // access flags 0x4019
        //  public final static enum Lnet/minecraft/world/DimensionType; {data.enumerationName}
        // [end for]
        //
        //  // access flags 0x12
        //  // signature Ljava/util/function/Supplier<Ljava/lang/Class<+Lnet/minecraft/world/WorldProvider;>;>;
        //  // declaration: fermion-injected-field$dimension-hopper-tweaks$classSupplier extends java.util.function.Supplier<java.lang.Class<? extends net.minecraft.world.WorldProvider>>
        //  private final Ljava/util/function/Supplier; fermion-injected-field$dimension-hopper-tweaks$classSupplier

        public static void inject(final ClassVisitor visitor, final Logger logger) {
            logger.info("Injecting all dimension fields");
            DIMENSIONS_TO_ADD.forEach(data -> visitor.visitField(
                    Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL | Opcodes.ACC_ENUM,
                    data.enumerationName(),
                    String.format("L%s;", DIMENSION_TYPE),
                    null,
                    null
            ));

            logger.info("Injecting supplier field");
            visitor.visitField(
                    Opcodes.ACC_PRIVATE | Opcodes.ACC_FINAL,
                    CLASS_SUPPLIER_FIELD_NAME,
                    String.format("L%s;", SUPPLIER),
                    String.format("L%s<L%s<+Lnet/minecraft/world/WorldProvider;>;>;", SUPPLIER, CLASS),
                    null
            );
        }
    }

    private static final class IdentityLambdaInjector extends MethodVisitor {
        //  // access flags 0x101A
        //  // signature (Ljava/lang/Class<*>;)Ljava/lang/Class<*>;
        //  // declaration: private static final java.lang.Class<?> fermion-injected-lambda$dimension-hopper-tweaks$supplier$identity-for-class(java.lang.Class<?>)
        //  private static final synthetic fermion-injected-lambda$dimension-hopper-tweaks$supplier$identity-for-class (Ljava/lang/Class;)Ljava/lang/Class;
        //   L0
        //    LINENUMBER 1990 L0
        //    ALOAD 0
        //    ARETURN
        //   L1
        //    LOCALVARIABLE $$0 Ljava/lang/Class; L0 L1
        //    // signature Ljava/lang/Class<*>;
        //    // declaration: $$0 extends java.lang.Class<?>
        //    MAXSTACK = 1
        //    MAXLOCALS = 1

        private final Logger logger;

        private IdentityLambdaInjector(final int version, final MethodVisitor parent, final Logger logger) {
            super(version, parent);
            this.logger = logger;
        }

        public static void inject(final int version, final ClassVisitor visitor, final Logger logger) {
            final MethodVisitor parent = visitor.visitMethod(
                    Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL | Opcodes.ACC_SYNTHETIC,
                    CLASS_IDENTITY_NAME,
                    CLASS_IDENTITY_DESC,
                    String.format("(L%s<*>;)L%s<*>;", CLASS, CLASS),
                    null
            );
            final MethodVisitor creator = new IdentityLambdaInjector(version, parent, logger);
            creator.visitCode();
        }

        @Override
        public void visitCode() {
            logger.info("Injecting identity function for classes, leveraged by the supplier");

            super.visitCode();

            final Label l0 = new Label();
            super.visitLabel(l0);
            super.visitLineNumber(1990, l0);
            super.visitVarInsn(Opcodes.ALOAD, 0);
            super.visitInsn(Opcodes.ARETURN);

            final Label l1 = new Label();
            super.visitLabel(l1);

            super.visitLocalVariable("$$0", String.format("L%s;", CLASS), String.format("L%s<*>;", CLASS), l0, l1, 0);
            super.visitMaxs(1, 1);

            super.visitEnd();
        }
    }

    private static final class SupplierLambdaInjector extends MethodVisitor {
        // [for every dimension {i = index, data = dimension data}]
        //  // access flags 0x101A
        //  // signature ()Ljava/lang/Class<+Lnet/minecraft/world/WorldProvider;>;
        //  // declaration: private static final java.lang.Class<? extends net.minecraft.world.WorldProvider> fermion-injected-lambda$dimension-hopper-tweaks$supplier$targeting-{i}()
        //  private static final synthetic fermion-injected-lambda$dimension-hopper-tweaks$supplier$targeting-{i} ()Ljava/lang/Class;
        //   L0
        //    LINENUMBER {1200 + i} L0
        //    LDC {data.worldProviderClassName}
        //    INVOKESTATIC java/lang/Class.forName (Ljava/lang/String;)Ljava/lang/Class;
        //    ARETURN
        //   L1
        //    MAXSTACK = 1
        //    MAXLOCALS = 0
        // [end for]

        private final int index;

        private SupplierLambdaInjector(final int version, final MethodVisitor parent, final int index) {
            super(version, parent);
            this.index = index;
        }

        public static void inject(final int version, final ClassVisitor visitor, final Logger logger) {
            logger.info("Performing massively and extremely invasive lambda injection");

            for (int i = 0, s = DIMENSIONS_TO_ADD.size(); i < s; ++i) {
                final MethodVisitor parent = visitor.visitMethod(
                        Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL | Opcodes.ACC_SYNTHETIC,
                        targetingSupplierName(i),
                        String.format("()L%s;", CLASS),
                        String.format("()L%s<+Lnet/minecraft/world/WorldProvider;>;", CLASS),
                        null
                );
                final MethodVisitor creator = new SupplierLambdaInjector(version, parent, i);
                creator.visitCode();
            }
        }

        @Override
        public void visitCode() {
            super.visitCode();

            final Label l0 = new Label();
            super.visitLabel(l0);
            super.visitLineNumber(1200 + this.index, l0);
            super.visitLdcInsn(DIMENSIONS_TO_ADD.get(this.index).worldProviderClassName());
            super.visitMethodInsn(Opcodes.INVOKESTATIC, CLASS, "forName", String.format("(Ljava/lang/String;)L%s;", CLASS), false);
            super.visitInsn(Opcodes.ARETURN);

            final Label l1 = new Label();
            super.visitLabel(l1);

            super.visitMaxs(1, 0);

            super.visitEnd();
        }
    }

    // This should be a record, but alas Java 8
    private static final class DimensionInjectionData {
        private final String enumerationName;
        private final int id;
        private final String dimensionName;
        private final String suffix;
        private final String worldProviderClassName;
        private final boolean shouldKeepLoaded;

        DimensionInjectionData(
                final String enumerationName,
                final int id,
                final String dimensionName,
                final String suffix,
                final String worldProviderClassName,
                final boolean shouldKeepLoaded
        ) {
            this.enumerationName = Objects.requireNonNull(enumerationName);
            this.id = id;
            this.dimensionName = Objects.requireNonNull(dimensionName);
            this.suffix = Objects.requireNonNull(suffix);
            this.worldProviderClassName = Objects.requireNonNull(worldProviderClassName);
            this.shouldKeepLoaded = shouldKeepLoaded;
        }

        String enumerationName() {
            return this.enumerationName;
        }

        int id() {
            return this.id;
        }

        String dimensionName() {
            return this.dimensionName;
        }

        String suffix() {
            return this.suffix;
        }

        String worldProviderClassName() {
            return this.worldProviderClassName;
        }

        boolean shouldKeepLoaded() {
            return this.shouldKeepLoaded;
        }
    }

    private static final int OBJECT = 0x1368;
    private static final int INTEGER = 0x1369;

    private static final String CLASS = ClassDescriptor.of(Class.class).toAsmName();
    private static final String DIMENSION_TYPE = "net/minecraft/world/DimensionType";
    private static final String LAMBDA_METAFACTORY = ClassDescriptor.of(LambdaMetafactory.class).toAsmName();
    private static final String LAZY = "mods/thecomputerizer/dimensionhoppertweaks/util/Lazy";
    private static final String SUPPLIER = ClassDescriptor.of(Supplier.class).toAsmName();

    private static final String CLASS_IDENTITY_NAME = "fermion-injected-lambda$dimension-hopper-tweaks$supplier$identity-for-class";
    private static final String CLASS_IDENTITY_DESC = MethodDescriptor.of(
            CLASS_IDENTITY_NAME,
            ImmutableList.of(ClassDescriptor.of(Class.class)),
            ClassDescriptor.of(Class.class)
    ).toAsmDescriptor();

    private static final String CLASS_SUPPLIER_FIELD_NAME = "fermion-injected-field$dimension-hopper-tweaks$classSupplier";

    private static final String DT_NEW_INIT_NAME = "<init>";
    private static final String DT_NEW_INIT_DESC = MethodDescriptor.of(
            DT_NEW_INIT_NAME,
            ImmutableList.of(
                    ClassDescriptor.of(String.class),
                    ClassDescriptor.of(int.class),
                    ClassDescriptor.of(int.class),
                    ClassDescriptor.of(String.class),
                    ClassDescriptor.of(String.class),
                    ClassDescriptor.of(Supplier.class),
                    ClassDescriptor.of(boolean.class)
            ),
            ClassDescriptor.of(void.class)
    ).toAsmDescriptor();

    private static final String FINDER_NAME = "fermion-injected-method$dimension-hopper-tweaks$find";
    private static final String FINDER_DESC = MethodDescriptor.of(
            FINDER_NAME,
            ImmutableList.of(ClassDescriptor.of(String.class)),
            ClassDescriptor.of(DIMENSION_TYPE)
    ).toAsmDescriptor();

    private static final String LAZY_OF_NAME = "of";
    private static final String LAZY_OF_DESC = MethodDescriptor.of(
            LAZY_OF_NAME,
            ImmutableList.of(ClassDescriptor.of(SUPPLIER)),
            ClassDescriptor.of(LAZY)
    ).toAsmDescriptor();

    private static final String METAFACTORY_NAME = "metafactory";
    private static final String METAFACTORY_DESC = MethodDescriptor.of(
            METAFACTORY_NAME,
            ImmutableList.of(
                    ClassDescriptor.of(MethodHandles.Lookup.class),
                    ClassDescriptor.of(String.class),
                    ClassDescriptor.of(MethodType.class),
                    ClassDescriptor.of(MethodType.class),
                    ClassDescriptor.of(MethodHandle.class),
                    ClassDescriptor.of(MethodType.class)
            ),
            ClassDescriptor.of(CallSite.class)
    ).toAsmDescriptor();

    private static final List<DimensionInjectionData> DIMENSIONS_TO_ADD = ImmutableList.of(
            new DimensionInjectionData("overworld",0,"overworld","","net.minecraft.world.WorldProviderSurface",true),
            new DimensionInjectionData("the_nether",-1,"the_nether","_nether","net.minecraft.world.WorldProviderHell",false),
            new DimensionInjectionData("the_end",1,"the_end","_end","net.minecraft.world.WorldProviderEnd",false),
            new DimensionInjectionData("jed_surface",7891,"JED Surface", "_dim7891","fi.dy.masa.justenoughdimensions.world.WorldProviderSurfaceJED",false),
            new DimensionInjectionData("jed_surface_0",0,"JED Surface 0", "","fi.dy.masa.justenoughdimensions.world.WorldProviderSurfaceJED",false),
            new DimensionInjectionData("jed_surface_loaded_0",0,"JED Surface Loaded 0", "_dim0","fi.dy.masa.justenoughdimensions.world.WorldProviderSurfaceJED",true),
            new DimensionInjectionData("jed_hell",-1,"JED Hell", "_dim-1","fi.dy.masa.justenoughdimensions.world.WorldProviderHellJED",false),
            new DimensionInjectionData("jed_end",1,"JED End", "_dim1","fi.dy.masa.justenoughdimensions.world.WorldProviderEndJED",false),
            new DimensionInjectionData("dimensiondiamond",9993,"dimensionDiamond","_dimensionDiamond","mod.mcreator.mcreator_dimensionDiamond$WorldProviderMod",false),
            new DimensionInjectionData("dimensionemerald",9994,"dimensionEmerald","_dimensionEmerald","mod.mcreator.mcreator_dimensionEmerald$WorldProviderMod",false),
            new DimensionInjectionData("dimensionglowstone",9995,"dimensionGlowstone","_dimensionGlowstone","mod.mcreator.mcreator_dimensionGlowstone$WorldProviderMod",false),
            new DimensionInjectionData("dimensiongold",9996,"dimensionGold","_dimensionGold","mod.mcreator.mcreator_dimensionGold$WorldProviderMod",false),
            new DimensionInjectionData("dimensioniron",9997,"dimensionIron","_dimensionIron","mod.mcreator.mcreator_dimensionIron$WorldProviderMod",false),
            new DimensionInjectionData("dimensionlapis",9998,"dimensionLapis","_dimensionLapis","mod.mcreator.mcreator_dimensionLapis$WorldProviderMod",false),
            new DimensionInjectionData("dimensionobsidian",9999,"dimensionObsidian","_dimensionObsidian","mod.mcreator.mcreator_dimensionObsidian$WorldProviderMod",false),
            new DimensionInjectionData("dimensionredstone",99911,"dimensionRedstone","_dimensionRedstone","mod.mcreator.mcreator_dimensionRedstone$WorldProviderMod",false),
            new DimensionInjectionData("dimensionsponge",9909,"dimensionSponge","_dimensionSponge","mod.mcreator.mcreator_dimensionSponge$WorldProviderMod",false),
            new DimensionInjectionData("dimensionwool",9903,"dimensionWool","_dimensionWool","mod.mcreator.mcreator_dimensionWool$WorldProviderMod",false),
            new DimensionInjectionData("dimensionice",99914,"dimensionIce","_dimensionIce","mod.mcreator.mcreator_dimensionIce$WorldProviderMod",false),
            new DimensionInjectionData("dimensioncloud",9917,"dimensionCloud","_dimensionCloud","mod.mcreator.mcreator_dimensionCloud$WorldProviderMod",false),
            new DimensionInjectionData("dimensionwood",9904,"dimensionWood","_dimensionWood","mod.mcreator.mcreator_dimensionWood$WorldProviderMod",false),
            new DimensionInjectionData("dimensionsnow",9911,"dimensionSnow","_dimensionSnow","mod.mcreator.mcreator_dimensionSnow$WorldProviderMod",false),
            new DimensionInjectionData("dimensionmelon",9918,"dimensionMelon","_dimensionMelon","mod.mcreator.mcreator_dimensionMelon$WorldProviderMod",false),
            new DimensionInjectionData("dimensionstone",9907,"dimensionStone","_dimensionStone","mod.mcreator.mcreator_dimensionStone$WorldProviderMod",false),
            new DimensionInjectionData("dimensioninterdimensional",9920,"dimensionInterDimensional","_dimensionInterDimensional","mod.mcreator.mcreator_dimensionInterDimensional$WorldProviderMod",false),
            new DimensionInjectionData("dimensioncoal",9921,"dimensionCoal","_dimensionCoal","mod.mcreator.mcreator_dimensionCoal$WorldProviderMod",false),
            new DimensionInjectionData("dimensionconcrete",9922,"dimensionConcrete","_dimensionConcrete","mod.mcreator.mcreator_dimensionConcrete$WorldProviderMod",false),
            new DimensionInjectionData("dimensionclay",9923,"dimensionClay","_dimensionClay","mod.mcreator.mcreator_dimensionClay$WorldProviderMod",false),
            new DimensionInjectionData("dimensiongreenery",9924,"dimensionGreenery","_dimensionGreenery","mod.mcreator.mcreator_dimensionGreenery$WorldProviderMod",false),
            new DimensionInjectionData("dimensionalternatereality",9912,"dimensionAlternateReality","_dimensionAlternateReality","mod.mcreator.mcreator_dimensionAlternateReality$WorldProviderMod",false),
            new DimensionInjectionData("dimensionbrick",99917,"dimensionBrick","_dimensionBrick","mod.mcreator.mcreator_dimensionBrick$WorldProviderMod",false),
            new DimensionInjectionData("dimensionstonebrick",9906,"dimensionStoneBrick","_dimensionStoneBrick","mod.mcreator.mcreator_dimensionStoneBrick$WorldProviderMod",false),
            new DimensionInjectionData("dimensionendstone",9928,"dimensionEndStone","_dimensionEndStone","mod.mcreator.mcreator_dimensionEndStone$WorldProviderMod",false),
            new DimensionInjectionData("dimensionprismarine",9929,"dimensionPrismarine","_dimensionPrismarine","mod.mcreator.mcreator_dimensionPrismarine$WorldProviderMod",false),
            new DimensionInjectionData("dimensionnetherwart",9930,"dimensionNetherWart","_dimensionNetherWart","mod.mcreator.mcreator_dimensionNetherWart$WorldProviderMod",false),
            new DimensionInjectionData("dimensionhaybale",9931,"dimensionHayBale","_dimensionHayBale","mod.mcreator.mcreator_dimensionHayBale$WorldProviderMod",false),
            new DimensionInjectionData("dimensionsealantern",9932,"dimensionSeaLantern","_dimensionSeaLantern","mod.mcreator.mcreator_dimensionSeaLantern$WorldProviderMod",false),
            new DimensionInjectionData("dimensionflint",9933,"dimensionflint","_dimensionflint","mod.mcreator.mcreator_dimensionflint$WorldProviderMod",false),
            new DimensionInjectionData("dimensionnetherbrick",9934,"dimensionNetherBrick","_dimensionNetherBrick","mod.mcreator.mcreator_dimensionNetherBrick$WorldProviderMod",false),
            new DimensionInjectionData("sandstonedimension",9902,"sandstoneDimension","_sandstoneDimension","mod.mcreator.mcreator_sandstoneDimension$WorldProviderMod",false),
            new DimensionInjectionData("redsandstonedimension",9901,"redSandstoneDimension","_redSandstoneDimension","mod.mcreator.mcreator_redSandstoneDimension$WorldProviderMod",false),
            new DimensionInjectionData("dimensionpurpur",9937,"dimensionPurpur","_dimensionPurpur","mod.mcreator.mcreator_dimensionPurpur$WorldProviderMod",false),
            new DimensionInjectionData("dimensionrednetherbrick",9938,"dimensionRedNetherBrick","_dimensionRedNetherBrick","mod.mcreator.mcreator_dimensionRedNetherBrick$WorldProviderMod",false),
            new DimensionInjectionData("dimensionbone",9915,"dimensionBone","_dimensionBone","mod.mcreator.mcreator_dimensionBone$WorldProviderMod",false),
            new DimensionInjectionData("dimensionslime",9940,"dimensionSlime","_dimensionSlime","mod.mcreator.mcreator_dimensionSlime$WorldProviderMod",false),
            new DimensionInjectionData("dimensionredstonelamp",9941,"dimensionRedstoneLamp","_dimensionRedstoneLamp","mod.mcreator.mcreator_dimensionRedstoneLamp$WorldProviderMod",false),
            new DimensionInjectionData("dimensionclaynormal",9942,"dimensionClayNormal","_dimensionClayNormal","mod.mcreator.mcreator_dimensionClayNormal$WorldProviderMod",false),
            new DimensionInjectionData("dimensionbookshelf",9916,"dimensionBookshelf","_dimensionBookshelf","mod.mcreator.mcreator_dimensionBookshelf$WorldProviderMod",false),
            new DimensionInjectionData("dimensionglass",9944,"dimensionGlass","_dimensionGlass","mod.mcreator.mcreator_dimensionGlass$WorldProviderMod",false),
            new DimensionInjectionData("dimensionsoulsand",9910,"dimensionSoulSand","_dimensionSoulSand","mod.mcreator.mcreator_dimensionSoulSand$WorldProviderMod",false),
            new DimensionInjectionData("dimensionjackolantern",9946,"dimensionJackoLantern","_dimensionJackoLantern","mod.mcreator.mcreator_dimensionJackoLantern$WorldProviderMod",false),
            new DimensionInjectionData("dimensionpumpkin",9947,"dimensionPumpkin","_dimensionPumpkin","mod.mcreator.mcreator_dimensionPumpkin$WorldProviderMod",false),
            new DimensionInjectionData("dimensionendstonebricks",9948,"dimensionEndStoneBricks","_dimensionEndStoneBricks","mod.mcreator.mcreator_dimensionEndStoneBricks$WorldProviderMod",false),
            new DimensionInjectionData("dimensioncobblestone",9949,"dimensionCobblestone","_dimensionCobblestone","mod.mcreator.mcreator_dimensionCobblestone$WorldProviderMod",false),
            new DimensionInjectionData("dimensionphantom",9953,"dimensionPhantom","_dimensionPhantom","mod.mcreator.mcreator_dimensionPhantom$WorldProviderMod",false),
            new DimensionInjectionData("dimensioncraftingtable",9954,"dimensionCraftingTable","_dimensionCraftingTable","mod.mcreator.mcreator_dimensionCraftingTable$WorldProviderMod",false),
            new DimensionInjectionData("dimensiontnt",9905,"dimensionTNT","_dimensionTNT","mod.mcreator.mcreator_dimensionTNT$WorldProviderMod",false),
            new DimensionInjectionData("dimensionfurnace",9956,"dimensionFurnace","_dimensionFurnace","mod.mcreator.mcreator_dimensionFurnace$WorldProviderMod",false),
            new DimensionInjectionData("dimensionquartz",9957,"dimensionQuartz","_dimensionQuartz","mod.mcreator.mcreator_dimensionQuartz$WorldProviderMod",false),
            new DimensionInjectionData("dimensionbedrock",9914,"dimensionBedrock","_dimensionBedrock","mod.mcreator.mcreator_dimensionBedrock$WorldProviderMod",false),
            new DimensionInjectionData("dimensionmagma",9959,"dimensionMagma","_dimensionMagma","mod.mcreator.mcreator_dimensionMagma$WorldProviderMod",false),
            new DimensionInjectionData("dimensionnoteblock",9961,"dimensionNoteblock","_dimensionNoteblock","mod.mcreator.mcreator_dimensionNoteblock$WorldProviderMod",false),
            new DimensionInjectionData("dimensioncouldron",9962,"dimensionCouldron","_dimensionCouldron","mod.mcreator.mcreator_dimensionCouldron$WorldProviderMod",false),
            new DimensionInjectionData("dimensionenchantmenttable",9963,"dimensionEnchantmentTable","_dimensionEnchantmentTable","mod.mcreator.mcreator_dimensionEnchantmentTable$WorldProviderMod",false),
            new DimensionInjectionData("dimensionendportal",9964,"dimensionEndPortal","_dimensionEndPortal","mod.mcreator.mcreator_dimensionEndPortal$WorldProviderMod",false),
            new DimensionInjectionData("dimensionshulkerbox",9965,"dimensionShulkerBox","_dimensionShulkerBox","mod.mcreator.mcreator_dimensionShulkerBox$WorldProviderMod",false),
            new DimensionInjectionData("dimensionglazedterracotta",9966,"dimensionGlazedTerracotta","_dimensionGlazedTerracotta","mod.mcreator.mcreator_dimensionGlazedTerracotta$WorldProviderMod",false),
            new DimensionInjectionData("dimensiondispenser",9967,"dimensionDispenser","_dimensionDispenser","mod.mcreator.mcreator_dimensionDispenser$WorldProviderMod",false),
            new DimensionInjectionData("dimensionjukebox",9968,"dimensionJukebox","_dimensionJukebox","mod.mcreator.mcreator_dimensionJukebox$WorldProviderMod",false),
            new DimensionInjectionData("dimensionstickypiston",9908,"dimensionStickyPiston","_dimensionStickyPiston","mod.mcreator.mcreator_dimensionStickyPiston$WorldProviderMod",false),
            new DimensionInjectionData("dimensionpiston",9970,"dimensionPiston","_dimensionPiston","mod.mcreator.mcreator_dimensionPiston$WorldProviderMod",false),
            new DimensionInjectionData("dimensionbrewingstand",999199,"dimensionBrewingStand","_dimensionBrewingStand","mod.mcreator.mcreator_dimensionBrewingStand$WorldProviderMod",false),
            new DimensionInjectionData("dimensionhopper",9972,"dimensionHopper","_dimensionHopper","mod.mcreator.mcreator_dimensionHopper$WorldProviderMod",false),
            new DimensionInjectionData("dimensiondropper",9973,"dimensionDropper","_dimensionDropper","mod.mcreator.mcreator_dimensionDropper$WorldProviderMod",false),
            new DimensionInjectionData("dimensionobserver",9974,"dimensionObserver","_dimensionObserver","mod.mcreator.mcreator_dimensionObserver$WorldProviderMod",false),
            new DimensionInjectionData("dimensionbeacon",9913,"dimensionBeacon","_dimensionBeacon","mod.mcreator.mcreator_dimensionBeacon$WorldProviderMod",false),
            new DimensionInjectionData("dimensionenderchest",9976,"dimensionEnderChest","_dimensionEnderChest","mod.mcreator.mcreator_dimensionEnderChest$WorldProviderMod",false),
            new DimensionInjectionData("dimensionfence",9977,"dimensionFence","_dimensionFence","mod.mcreator.mcreator_dimensionFence$WorldProviderMod",false),
            new DimensionInjectionData("dimensionironbars",9978,"dimensionIronBars","_dimensionIronBars","mod.mcreator.mcreator_dimensionIronBars$WorldProviderMod",false),
            new DimensionInjectionData("dimensionglasspane",9979,"dimensionGlassPane","_dimensionGlassPane","mod.mcreator.mcreator_dimensionGlassPane$WorldProviderMod",false),
            new DimensionInjectionData("dimensioncobblestonewall",9980,"dimensionCobblestoneWall","_dimensionCobblestoneWall","mod.mcreator.mcreator_dimensionCobblestoneWall$WorldProviderMod",false),
            new DimensionInjectionData("nether",-1,"Nether","_nether","biomesoplenty.common.world.WorldProviderBOPHell",false),
            new DimensionInjectionData("twilight_forest",7,"twilight_forest","_twilightforest","twilightforest.world.WorldProviderTwilightForest",false),
            new DimensionInjectionData("aether",49,"Aether","_aether","com.gildedgames.aether.common.world.WorldProviderAether",false),
            new DimensionInjectionData("necromancertower",4,"NecromancerTower","_necromancer_tower","com.gildedgames.aether.common.world.WorldProviderNecromancerTower",false),
            new DimensionInjectionData("abyss",800,"abyss","_abyss","net.tslat.aoa3.dimension.abyss.WorldProviderAbyss",false),
            new DimensionInjectionData("ancient_cavern",801,"ancient_cavern","_ancientcavern","net.tslat.aoa3.dimension.ancientcavern.WorldProviderAncientCavern",false),
            new DimensionInjectionData("barathos",802,"barathos","_barathos","net.tslat.aoa3.dimension.barathos.WorldProviderBarathos",false),
            new DimensionInjectionData("candyland",803,"candyland","_candyland","net.tslat.aoa3.dimension.candyland.WorldProviderCandyland",false),
            new DimensionInjectionData("celeve",804,"celeve","_celeve","net.tslat.aoa3.dimension.celeve.WorldProviderCeleve",false),
            new DimensionInjectionData("creeponia",805,"creeponia","_creeponia","net.tslat.aoa3.dimension.creeponia.WorldProviderCreeponia",false),
            new DimensionInjectionData("crystevia",806,"crystevia","_crystevia","net.tslat.aoa3.dimension.crystevia.WorldProviderCrystevia",false),
            new DimensionInjectionData("deeplands",807,"deeplands","_deeplands","net.tslat.aoa3.dimension.deeplands.WorldProviderDeeplands",false),
            new DimensionInjectionData("dustopia",808,"dustopia","_dustopia","net.tslat.aoa3.dimension.dustopia.WorldProviderDustopia",false),
            new DimensionInjectionData("gardencia",809,"gardencia","_gardencia","net.tslat.aoa3.dimension.gardencia.WorldProviderGardencia",false),
            new DimensionInjectionData("greckon",810,"greckon","_greckon","net.tslat.aoa3.dimension.greckon.WorldProviderGreckon",false),
            new DimensionInjectionData("haven",811,"haven","_haven","net.tslat.aoa3.dimension.haven.WorldProviderHaven",false),
            new DimensionInjectionData("immortallis",812,"immortallis","_immortallis","net.tslat.aoa3.dimension.immortallis.WorldProviderImmortallis",false),
            new DimensionInjectionData("iromine",813,"iromine","_iromine","net.tslat.aoa3.dimension.iromine.WorldProviderIromine",false),
            new DimensionInjectionData("lborean",814,"lborean","_lborean","net.tslat.aoa3.dimension.lborean.WorldProviderLBorean",false),
            new DimensionInjectionData("lelyetia",815,"lelyetia","_lelyetia","net.tslat.aoa3.dimension.lelyetia.WorldProviderLelyetia",false),
            new DimensionInjectionData("lunalus",816,"lunalus","_lunalus","net.tslat.aoa3.dimension.lunalus.WorldProviderLunalus",false),
            new DimensionInjectionData("mysterium",817,"mysterium","_mysterium","net.tslat.aoa3.dimension.mysterium.WorldProviderMysterium",false),
            new DimensionInjectionData("precasia",818,"precasia","_precasia","net.tslat.aoa3.dimension.precasia.WorldProviderPrecasia",false),
            new DimensionInjectionData("runandor",819,"runandor","_runandor","net.tslat.aoa3.dimension.runandor.WorldProviderRunandor",false),
            new DimensionInjectionData("shyrelands",820,"shyrelands","_shyrelands","net.tslat.aoa3.dimension.shyrelands.WorldProviderShyrelands",false),
            new DimensionInjectionData("vox_ponds",821,"vox_ponds","_voxponds","net.tslat.aoa3.dimension.voxponds.WorldProviderVoxPonds",false),
            new DimensionInjectionData("mining_world",-6,"MINING_WORLD","_mining_world","aroma1997.world.dimension.WorldProviderMining",false),
            new DimensionInjectionData("bedrockcraftvoid",2,"bedrockcraftvoid","_bvoidworld","bedrockcraft.voidworld.WorldProviderVoid",false),
            new DimensionInjectionData("compactmachines",144,"CompactMachines","_suffix","org.dave.compactmachines3.world.WorldProviderMachines",false),
            new DimensionInjectionData("limbo",684,"limbo","_limbo","org.dimdev.dimdoors.shared.world.limbo.WorldProviderLimbo",false),
            new DimensionInjectionData("private_pockets",685,"private_pockets","_private","org.dimdev.dimdoors.shared.world.pocketdimension.WorldProviderPersonalPocket",false),
            new DimensionInjectionData("public_pockets",686,"public_pockets","_public","org.dimdev.dimdoors.shared.world.pocketdimension.WorldProviderPublicPocket",false),
            new DimensionInjectionData("dungeon_pockets",687,"dungeon_pockets","_dungeon","org.dimdev.dimdoors.shared.world.pocketdimension.WorldProviderDungeonPocket",false),
            new DimensionInjectionData("erebus",66,"EREBUS","","erebus.world.WorldProviderErebus",false),
            new DimensionInjectionData("zollus_space_station",-6000,"Zollus Space Station", "_zollus_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitZollus",false),
            new DimensionInjectionData("zollus_space_station_alt",-6001,"Zollus Space Station", "_zollus_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitZollus",true),
            new DimensionInjectionData("kriffon_space_station",-6002,"Kriffon Space Station", "_kriffon_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitKriffon",false),
            new DimensionInjectionData("kriffon_space_station_alt",-6003,"Kriffon Space Station", "_kriffon_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitKriffon",true),
            new DimensionInjectionData("purgot_space_station",-6004,"Purgot Space Station", "_purgot_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitPurgot",false),
            new DimensionInjectionData("purgot_space_station_alt",-6005,"Purgot Space Station", "_purgot_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitPurgot",true),
            new DimensionInjectionData("eden_space_station",-6006,"Eden Space Station", "_eden_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitEden",false),
            new DimensionInjectionData("eden_space_station_alt",-6007,"Eden Space Station", "_eden_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitEden",true),
            new DimensionInjectionData("xathius_space_station",-6008,"Xathius Space Station", "_xathius_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitXathius",false),
            new DimensionInjectionData("xathius_space_station_alt",-6009,"Xathius Space Station", "_xathius_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitXathius",true),
            new DimensionInjectionData("oasis_space_station",-6010,"Oasis Space Station", "_oasis_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitOasis",false),
            new DimensionInjectionData("oasis_space_station_alt",-6011,"Oasis Space Station", "_oasis_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitOasis",true),
            new DimensionInjectionData("xantheon_space_station",-6012,"Xantheon Space Station", "_xantheon_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitXantheon",false),
            new DimensionInjectionData("xantheon_space_station_alt",-6013,"Xantheon Space Station", "_xantheon_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitXantheon",true),
            new DimensionInjectionData("candora_space_station",-6014,"Candora Space Station", "_candora_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitCandora",false),
            new DimensionInjectionData("candora_space_station_alt",-6015,"Candora Space Station", "_candora_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitCandora",true),
            new DimensionInjectionData("atheon_space_station",-6016,"Atheon Space Station", "_atheon_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitAtheon",false),
            new DimensionInjectionData("atheon_space_station_alt",-6017,"Atheon Space Station", "_atheon_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitAtheon",true),
            new DimensionInjectionData("perdita_space_station",-6018,"Perdita Space Station", "_perdita_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitPerdita",false),
            new DimensionInjectionData("perdita_space_station_alt",-6019,"Perdita Space Station", "_perdita_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitPerdita",true),
            new DimensionInjectionData("altum_space_station",-6020,"Altum Space Station", "_altum_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitAltum",false),
            new DimensionInjectionData("altum_space_station_alt",-6021,"Altum Space Station", "_altum_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitAltum",true),
            new DimensionInjectionData("caligro_space_station",-6022,"Caligro Space Station", "_caligro_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitCaligro",false),
            new DimensionInjectionData("caligro_space_station_alt",-6023,"Caligro Space Station", "_caligro_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitCaligro",true),
            new DimensionInjectionData("exodus_space_station",-6024,"Exodus Space Station", "_exodus_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitExodus",false),
            new DimensionInjectionData("exodus_space_station_alt",-6025,"Exodus Space Station", "_exodus_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitExodus",true),
            new DimensionInjectionData("vortex_space_station",-6026,"Vortex Space Station", "_vortex_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitVortex",false),
            new DimensionInjectionData("vortex_space_station_alt",-6027,"Vortex Space Station", "_vortex_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitVortex",true),
            new DimensionInjectionData("metztli_space_station",-6028,"Metztli Space Station", "_metztli_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitMetztli",false),
            new DimensionInjectionData("metztli_space_station_alt",-6029,"Metztli Space Station", "_metztli_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitMetztli",true),
            new DimensionInjectionData("centotl_space_station",-6030,"Centotl Space Station", "_centotl_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitCentotl",false),
            new DimensionInjectionData("centotl_space_station_alt",-6031,"Centotl Space Station", "_centotl_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitCentotl",true),
            new DimensionInjectionData("toci_space_station",-6032,"Toci Space Station", "_toci_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitToci",false),
            new DimensionInjectionData("toci_space_station_alt",-6033,"Toci Space Station", "_toci_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitToci",true),
            new DimensionInjectionData("tlaloc_space_station",-6034,"Tlaloc Space Station", "_tlaloc_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitTlaloc",false),
            new DimensionInjectionData("tlaloc_space_station_alt",-6035,"Tlaloc Space Station", "_tlaloc_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitTlaloc",true),
            new DimensionInjectionData("kronos_space_station",-6036,"Kronos Space Station", "_kronos_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitKronos",false),
            new DimensionInjectionData("kronos_space_station_alt",-6037,"Kronos Space Station", "_kronos_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitKronos",true),
            new DimensionInjectionData("maveth_space_station",-6038,"Maveth Space Station", "_maveth_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitMaveth",false),
            new DimensionInjectionData("maveth_space_station_alt",-6039,"Maveth Space Station", "_maveth_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitMaveth",true),
            new DimensionInjectionData("mercury_space_station",-61,"Mercury Space Station", "_mercury_orbit","com.mjr.extraplanets.planets.Mercury.spacestation.WorldProviderMercuryOrbit",false),
            new DimensionInjectionData("mercury_space_station_alt",-82,"Mercury Space Station", "_mercury_orbit","com.mjr.extraplanets.planets.Mercury.spacestation.WorldProviderMercuryOrbit",true),
            new DimensionInjectionData("venus_space_station",-63,"Venus Space Station", "_venus_orbit","com.mjr.extraplanets.planets.venus.spacestation.WorldProviderVenusOrbit",false),
            new DimensionInjectionData("venus_space_station_alt",-62,"Venus Space Station", "_venus_orbit","com.mjr.extraplanets.planets.venus.spacestation.WorldProviderVenusOrbit",true),
            new DimensionInjectionData("ceres_space_station",-65,"Ceres Space Station", "_ceres_orbit","com.mjr.extraplanets.planets.Ceres.spacestation.WorldProviderCeresOrbit",false),
            new DimensionInjectionData("ceres_space_station_alt",-64,"Ceres Space Station", "_ceres_orbit","com.mjr.extraplanets.planets.Ceres.spacestation.WorldProviderCeresOrbit",true),
            new DimensionInjectionData("mars_space_station",-67,"Mars Space Station", "_mars_orbit","com.mjr.extraplanets.planets.mars.spacestation.WorldProviderMarsOrbit",false),
            new DimensionInjectionData("mars_space_station_alt",-66,"Mars Space Station", "_mars_orbit","com.mjr.extraplanets.planets.mars.spacestation.WorldProviderMarsOrbit",true),
            new DimensionInjectionData("jupiter_space_station",-69,"Jupiter Space Station", "_jupiter_orbit","com.mjr.extraplanets.planets.Jupiter.spacestation.WorldProviderJupiterOrbit",false),
            new DimensionInjectionData("jupiter_space_station_alt",-68,"Jupiter Space Station", "_jupiter_orbit","com.mjr.extraplanets.planets.Jupiter.spacestation.WorldProviderJupiterOrbit",true),
            new DimensionInjectionData("saturn_space_station",-71,"Saturn Space Station", "_saturn_orbit","com.mjr.extraplanets.planets.Saturn.spacestation.WorldProviderSaturnOrbit",false),
            new DimensionInjectionData("saturn_space_station_alt",-70,"Saturn Space Station", "_saturn_orbit","com.mjr.extraplanets.planets.Saturn.spacestation.WorldProviderSaturnOrbit",true),
            new DimensionInjectionData("uranus_space_station",-73,"Uranus Space Station", "_uranus_orbit","com.mjr.extraplanets.planets.Uranus.spacestation.WorldProviderUranusOrbit",false),
            new DimensionInjectionData("uranus_space_station_alt",-72,"Uranus Space Station", "_uranus_orbit","com.mjr.extraplanets.planets.Uranus.spacestation.WorldProviderUranusOrbit",true),
            new DimensionInjectionData("neptune_space_station",-75,"Neptune Space Station", "_neptune_orbit","com.mjr.extraplanets.planets.Neptune.spacestation.WorldProviderNeptuneOrbit",false),
            new DimensionInjectionData("neptune_space_station_alt",-74,"Neptune Space Station", "_neptune_orbit","com.mjr.extraplanets.planets.Neptune.spacestation.WorldProviderNeptuneOrbit",true),
            new DimensionInjectionData("pluto_space_station",-77,"Pluto Space Station", "_pluto_orbit","com.mjr.extraplanets.planets.Pluto.spacestation.WorldProviderPlutoOrbit",false),
            new DimensionInjectionData("pluto_space_station_alt",-76,"Pluto Space Station", "_pluto_orbit","com.mjr.extraplanets.planets.Pluto.spacestation.WorldProviderPlutoOrbit",true),
            new DimensionInjectionData("eris_space_station",-79,"Eris Space Station", "_eris_orbit","com.mjr.extraplanets.planets.Eris.spacestation.WorldProviderErisOrbit",false),
            new DimensionInjectionData("eris_space_station_alt",-78,"Eris Space Station", "_eris_orbit","com.mjr.extraplanets.planets.Eris.spacestation.WorldProviderErisOrbit",true),
            new DimensionInjectionData("kepler22b_space_station",-81,"Kepler22b Space Station", "_kepler22b_orbit","com.mjr.extraplanets.planets.Kepler22b.spacestation.WorldProviderKepler22bOrbit",false),
            new DimensionInjectionData("kepler22b_space_station_alt",-80,"Kepler22b Space Station", "_kepler22b_orbit","com.mjr.extraplanets.planets.Kepler22b.spacestation.WorldProviderKepler22bOrbit",true),
            new DimensionInjectionData("deep_dark",-11325,"Deep Dark", "_deep.dark","com.rwtema.extrautils2.dimensions.deep_dark.WorldProviderDeepDark",false),
            new DimensionInjectionData("extrautils2_quarry_dim",-9999,"ExtraUtils2_Quarry_Dim","_extrautilsquarrydim","com.rwtema.extrautils2.dimensions.workhousedim.WorldProviderSpecialDim",true),
            new DimensionInjectionData("gaia",-2,"Gaia","_gaia","androsa.gaiadimension.world.WorldProviderGaia",false),
            new DimensionInjectionData("hunting_dim",28885,"hunting_dim","_hunting","net.darkhax.huntingdim.dimension.WorldProviderHunting",false),
            new DimensionInjectionData("betweenlands",20,"betweenlands","_betweenlands","thebetweenlands.common.world.WorldProviderBetweenlands",false),
            new DimensionInjectionData("rftools_dimension",999200,"rftools_dimension","_rftools","mcjty.rftoolsdim.dimensions.world.GenericWorldProvider",false),
            new DimensionInjectionData("theaurorian",428,"theaurorian","_aurorian","com.elseytd.theaurorian.World.TAWorldProvider",false),
            new DimensionInjectionData("midnight",-23,"midnight","_midnight","com.mushroom.midnight.common.world.MidnightWorldProvider",false),
            new DimensionInjectionData("everbright",76,"everbright","_everBright","com.legacy.blue_skies.world.everbright.EverbrightWorldProvider",false),
            new DimensionInjectionData("everdawn",77,"everdawn","_everDawn","com.legacy.blue_skies.world.everdawn.EverdawnWorldProvider",false),
            new DimensionInjectionData("cavern",-50,"cavern","_cavern","cavern.world.WorldProviderCavern",false),
            new DimensionInjectionData("huge_cavern",-51,"huge_cavern","_huge_cavern","cavern.world.WorldProviderHugeCavern",false),
            new DimensionInjectionData("aqua_cavern",-52,"aqua_cavern","_aqua_cavern","cavern.world.WorldProviderAquaCavern",false),
            new DimensionInjectionData("caveland",-53,"caveland","_caveland","cavern.world.mirage.WorldProviderCaveland",false),
            new DimensionInjectionData("cavenia",-54,"cavenia","_cavenia","cavern.world.mirage.WorldProviderCavenia",false),
            new DimensionInjectionData("frost_mountains",-55,"frost_mountains","_frost_mountains","cavern.world.mirage.WorldProviderFrostMountains",false),
            new DimensionInjectionData("wide_desert",-56,"wide_desert","_wide_desert","cavern.world.mirage.WorldProviderWideDesert",false),
            new DimensionInjectionData("the_void",-57,"the_void","_the_void","cavern.world.mirage.WorldProviderVoid",false),
            new DimensionInjectionData("dark_forest",-58,"dark_forest","_dark_forest","cavern.world.mirage.WorldProviderDarkForest",false),
            new DimensionInjectionData("crown_cliffs",-59,"crown_cliffs","_crown_cliffs","cavern.world.mirage.WorldProviderCrownCliffs",false),
            new DimensionInjectionData("skyland",-60,"skyland","_skyland","cavern.world.mirage.WorldProviderSkyland",false),
            new DimensionInjectionData("space_station",-27,"Space","Station _orbit","micdoodle8.mods.galacticraft.core.dimension.WorldProviderOverworldOrbit",false),
            new DimensionInjectionData("space_station_alt",-26,"Space","Station _orbit","micdoodle8.mods.galacticraft.core.dimension.WorldProviderOverworldOrbit",true),
            new DimensionInjectionData("glacidus",84,"Glacidus","_glacidus","com.legacy.glacidus.world.WorldProviderGlacidus",false),
            new DimensionInjectionData("gooddream",44,"GoodDream","_goodDream","com.legacy.goodnightsleep.world.dream.GoodDreamWorldProvider",false),
            new DimensionInjectionData("nightmare",44,"Nightmare","_nightmare","com.legacy.goodnightsleep.world.nightmare.NightmareWorldProvider",false),
            new DimensionInjectionData("spectre",-343800852,"Spectre","Spectre_","lumien.randomthings.handler.spectre.SpectreWorldProvider",true),
            new DimensionInjectionData("labyrinth",-28982081,"labyrinth","_labyrinth","com.abecderic.labyrinth.worldgen.LabyrinthWorldProvider",false),
            new DimensionInjectionData("underworld",-9,"Underworld","_under","sblectric.lightningcraft.dimensions.LCWorldProviderUnderworld",false),
            new DimensionInjectionData("misty_world",69,"misty_world","_mist","ru.liahim.mist.world.WorldProviderMist",false),
            new DimensionInjectionData("diona_space_station",-3007,"Diona Space Station", "_diona_orbit","com.mjr.moreplanetsextras.spaceStations.WorldProviderOrbitDiona",false),
            new DimensionInjectionData("diona_space_station_alt",-3008,"Diona Space Station", "_diona_orbit","com.mjr.moreplanetsextras.spaceStations.WorldProviderOrbitDiona",true),
            new DimensionInjectionData("chalos_space_station",-3009,"Chalos Space Station", "_chalos_orbit","com.mjr.moreplanetsextras.spaceStations.WorldProviderOrbitChalos",false),
            new DimensionInjectionData("chalos_space_station_alt",-3010,"Chalos Space Station", "_chalos_orbit","com.mjr.moreplanetsextras.spaceStations.WorldProviderOrbitChalos",true),
            new DimensionInjectionData("nibiru_space_station",-3011,"Nibiru Space Station", "_nibiru_orbit","com.mjr.moreplanetsextras.spaceStations.WorldProviderOrbitNibiru",false),
            new DimensionInjectionData("nibiru_space_station_alt",-3012,"Nibiru Space Station", "_nibiru_orbit","com.mjr.moreplanetsextras.spaceStations.WorldProviderOrbitNibiru",true),
            new DimensionInjectionData("storage_cell",-11,"Storage Cell", "_cell","appeng.spatial.StorageWorldProvider",true),
            new DimensionInjectionData("atum",17,"atum","_atum","com.teammetallurgy.atum.world.WorldProviderAtum",false),
            new DimensionInjectionData("planet_altum",-7878,"planet.altum","","zollerngalaxy.core.dimensions.worldproviders.WorldProviderAltum",true),
            new DimensionInjectionData("planet_asteroids",-30,"planet.asteroids", "","micdoodle8.mods.galacticraft.planets.asteroids.dimension.WorldProviderAsteroids",true),
            new DimensionInjectionData("planet_atheon",-7998,"planet.atheon", "","zollerngalaxy.core.dimensions.worldproviders.WorldProviderAtheon",true),
            new DimensionInjectionData("planet_caligro",-7877,"planet.caligro", "","zollerngalaxy.core.dimensions.worldproviders.WorldProviderCaligro",true),
            new DimensionInjectionData("planet_candora",-7777,"planet.candora", "","zollerngalaxy.core.dimensions.worldproviders.WorldProviderCandora",true),
            new DimensionInjectionData("planet_centotl",-7997,"planet.centotl", "","zollerngalaxy.core.dimensions.worldproviders.WorldProviderCentotl",true),
            new DimensionInjectionData("planet_ceres",-20,"planet.Ceres", "","com.mjr.extraplanets.planets.Ceres.WorldProviderCeres",true),
            new DimensionInjectionData("planet_chalos",-2543,"planet.chalos", "","stevekung.mods.moreplanets.planets.chalos.dimension.WorldProviderChalos",true),
            new DimensionInjectionData("planet_diona",-2542,"planet.diona", "","stevekung.mods.moreplanets.planets.diona.dimension.WorldProviderDiona",true),
            new DimensionInjectionData("planet_eden",-7996,"planet.eden", "","zollerngalaxy.core.dimensions.worldproviders.WorldProviderEden",true),
            new DimensionInjectionData("planet_eris",-21,"planet.Eris", "","com.mjr.extraplanets.planets.Eris.WorldProviderEris",true),
            new DimensionInjectionData("planet_exodus",-7980,"planet.exodus", "","zollerngalaxy.core.dimensions.worldproviders.WorldProviderExodus",true),
            new DimensionInjectionData("planet_fronos",-2545,"planet.fronos", "","stevekung.mods.moreplanets.planets.fronos.dimension.WorldProviderFronos",true),
            new DimensionInjectionData("planet_jupiter",-15,"planet.Jupiter", "","com.mjr.extraplanets.planets.Jupiter.WorldProviderJupiter",true),
            new DimensionInjectionData("planet_kepler22b",-22,"planet.kepler22b", "","com.mjr.extraplanets.planets.Kepler22b.WorldProviderKepler22b",true),
            new DimensionInjectionData("planet_kriffus",-7994,"planet.kriffus", "","zollerngalaxy.core.dimensions.worldproviders.WorldProviderKriffon",true),
            new DimensionInjectionData("planet_kronos",-7993,"planet.kronos", "","zollerngalaxy.core.dimensions.worldproviders.WorldProviderKronos",true),
            new DimensionInjectionData("planet_mars",-29,"planet.mars", "","micdoodle8.mods.galacticraft.planets.mars.dimension.WorldProviderMars",true),
            new DimensionInjectionData("planet_maveth",-4552,"planet.maveth", "","zollerngalaxy.core.dimensions.worldproviders.WorldProviderMaveth",true),
            new DimensionInjectionData("planet_mercury",-13,"planet.Mercury", "","com.mjr.extraplanets.planets.Mercury.WorldProviderMercury",true),
            new DimensionInjectionData("planet_metztli",-7991,"planet.metztli", "","zollerngalaxy.core.dimensions.worldproviders.WorldProviderMetztli",true),
            new DimensionInjectionData("planet_neptune",-18,"planet.Neptune", "","com.mjr.extraplanets.planets.Neptune.WorldProviderNeptune",true),
            new DimensionInjectionData("planet_nibiru",-2544,"planet.nibiru", "","stevekung.mods.moreplanets.planets.nibiru.dimension.WorldProviderNibiru",true),
            new DimensionInjectionData("planet_oasis",-7990,"planet.oasis", "","zollerngalaxy.core.dimensions.worldproviders.WorldProviderOasis",true),
            new DimensionInjectionData("planet_perdita",-7979,"planet.perdita", "","zollerngalaxy.core.dimensions.worldproviders.WorldProviderPerdita",true),
            new DimensionInjectionData("planet_pluto",-19,"planet.Pluto", "","com.mjr.extraplanets.planets.Pluto.WorldProviderPluto",true),
            new DimensionInjectionData("planet_purgot",-7988,"planet.purgot", "","zollerngalaxy.core.dimensions.worldproviders.WorldProviderPurgot",true),
            new DimensionInjectionData("planet_saturn",-16,"planet.Saturn", "","com.mjr.extraplanets.planets.Saturn.WorldProviderSaturn",true),
            new DimensionInjectionData("planet_space_nether",-2541,"planet.space_nether", "","stevekung.mods.moreplanets.core.dimension.WorldProviderSpaceNether",true),
            new DimensionInjectionData("planet_tlaloc",-7986,"planet.tlaloc", "","zollerngalaxy.core.dimensions.worldproviders.WorldProviderTlaloc",true),
            new DimensionInjectionData("planet_toci",-7985,"planet.toci", "","zollerngalaxy.core.dimensions.worldproviders.WorldProviderToci",true),
            new DimensionInjectionData("planet_uranus",-17,"planet.Uranus", "","com.mjr.extraplanets.planets.Uranus.WorldProviderUranus",true),
            new DimensionInjectionData("planet_venus",-31,"planet.venus", "","micdoodle8.mods.galacticraft.planets.venus.dimension.WorldProviderVenus",true),
            new DimensionInjectionData("planet_vortex",-7675,"planet.vortex", "","zollerngalaxy.core.dimensions.worldproviders.WorldProviderVortex",true),
            new DimensionInjectionData("planet_xantheon",-7984,"planet.xantheon", "","zollerngalaxy.core.dimensions.worldproviders.WorldProviderXantheon",true),
            new DimensionInjectionData("planet_xathius",-7983,"planet.xathius", "","zollerngalaxy.core.dimensions.worldproviders.WorldProviderXathius",true),
            new DimensionInjectionData("planet_zollus",-7982,"planet.zollus", "","zollerngalaxy.core.dimensions.worldproviders.WorldProviderZollus",true),
            new DimensionInjectionData("moon_astros",-7981,"moon.astros", "","zollerngalaxy.core.dimensions.worldproviders.WorldProviderAstros",true),
            new DimensionInjectionData("moon_callisto",-1505,"moon.callisto", "","com.mjr.extraplanets.moons.Callisto.WorldProviderCallisto",true),
            new DimensionInjectionData("moon_deimos",-1503,"moon.deimos", "","com.mjr.extraplanets.moons.Deimos.WorldProviderDeimos",true),
            new DimensionInjectionData("moon_europa",-1501,"moon.europa", "","com.mjr.extraplanets.moons.Europa.WorldProviderEuropa",true),
            new DimensionInjectionData("moon_ganymede",-1506,"moon.ganymede", "","com.mjr.extraplanets.moons.Ganymede.WorldProviderGanymede",true),
            new DimensionInjectionData("moon_iapetus",-1511,"moon.iapetus", "","com.mjr.extraplanets.moons.Iapetus.WorldProviderIapetus",true),
            new DimensionInjectionData("moon_io",-1500,"moon.io", "","com.mjr.extraplanets.moons.Io.WorldProviderIo",true),
            new DimensionInjectionData("moon_koentus",-2642,"moon.koentus", "","stevekung.mods.moreplanets.moons.koentus.dimension.WorldProviderKoentus",true),
            new DimensionInjectionData("moon_moon",-28,"moon.moon", "","micdoodle8.mods.galacticraft.core.dimension.WorldProviderMoon",true),
            new DimensionInjectionData("moon_oberon",-1509,"moon.oberon", "","com.mjr.extraplanets.moons.Oberon.WorldProviderOberon",true),
            new DimensionInjectionData("moon_phobos",-1502,"moon.phobos", "","com.mjr.extraplanets.moons.Phobos.WorldProviderPhobos",true),
            new DimensionInjectionData("moon_rhea",-1507,"moon.rhea", "","com.mjr.extraplanets.moons.Rhea.WorldProviderRhea",true),
            new DimensionInjectionData("moon_titan",-1508,"moon.titan", "","com.mjr.extraplanets.moons.Titan.WorldProviderTitan",true),
            new DimensionInjectionData("moon_titania",-1510,"moon.titania", "","com.mjr.extraplanets.moons.Titania.WorldProviderTitania",true),
            new DimensionInjectionData("moon_triton",-1504,"moon.triton", "","com.mjr.extraplanets.moons.Triton.WorldProviderTriton",true),
            new DimensionInjectionData("zollus_space_station_wp",-6000,"Zollus Space Station", "_zollus_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitZollus",false),
            new DimensionInjectionData("zollus_space_station_alt_wp",-6001,"Zollus Space Station", "_zollus_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitZollus",true),
            new DimensionInjectionData("kriffon_space_station_wp",-6002,"Kriffon Space Station", "_kriffon_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitKriffon",false),
            new DimensionInjectionData("kriffon_space_station_alt_wp",-6003,"Kriffon Space Station", "_kriffon_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitKriffon",true),
            new DimensionInjectionData("purgot_space_station_wp",-6004,"Purgot Space Station", "_purgot_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitPurgot",false),
            new DimensionInjectionData("purgot_space_station_alt_wp",-6005,"Purgot Space Station", "_purgot_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitPurgot",true),
            new DimensionInjectionData("eden_space_station_wp",-6006,"Eden Space Station", "_eden_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitEden",false),
            new DimensionInjectionData("eden_space_station_alt_wp",-6007,"Eden Space Station", "_eden_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitEden",true),
            new DimensionInjectionData("xathius_space_station_wp",-6008,"Xathius Space Station", "_xathius_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitXathius",false),
            new DimensionInjectionData("xathius_space_station_alt_wp",-6009,"Xathius Space Station", "_xathius_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitXathius",true),
            new DimensionInjectionData("oasis_space_station_wp",-6010,"Oasis Space Station", "_oasis_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitOasis",false),
            new DimensionInjectionData("oasis_space_station_alt_wp",-6011,"Oasis Space Station", "_oasis_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitOasis",true),
            new DimensionInjectionData("xantheon_space_station_wp",-6012,"Xantheon Space Station", "_xantheon_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitXantheon",false),
            new DimensionInjectionData("xantheon_space_station_alt_wp",-6013,"Xantheon Space Station", "_xantheon_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitXantheon",true),
            new DimensionInjectionData("candora_space_station_wp",-6014,"Candora Space Station", "_candora_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitCandora",false),
            new DimensionInjectionData("candora_space_station_alt_wp",-6015,"Candora Space Station", "_candora_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitCandora",true),
            new DimensionInjectionData("atheon_space_station_wp",-6016,"Atheon Space Station", "_atheon_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitAtheon",false),
            new DimensionInjectionData("atheon_space_station_alt_wp",-6017,"Atheon Space Station", "_atheon_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitAtheon",true),
            new DimensionInjectionData("perdita_space_station_wp",-6018,"Perdita Space Station", "_perdita_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitPerdita",false),
            new DimensionInjectionData("perdita_space_station_alt_wp",-6019,"Perdita Space Station", "_perdita_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitPerdita",true),
            new DimensionInjectionData("altum_space_station_wp",-6020,"Altum Space Station", "_altum_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitAltum",false),
            new DimensionInjectionData("altum_space_station_alt_wp",-6021,"Altum Space Station", "_altum_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitAltum",true),
            new DimensionInjectionData("caligro_space_station_wp",-6022,"Caligro Space Station", "_caligro_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitCaligro",false),
            new DimensionInjectionData("caligro_space_station_alt_wp",-6023,"Caligro Space Station", "_caligro_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitCaligro",true),
            new DimensionInjectionData("exodus_space_station_wp",-6024,"Exodus Space Station", "_exodus_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitExodus",false),
            new DimensionInjectionData("exodus_space_station_alt_wp",-6025,"Exodus Space Station", "_exodus_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitExodus",true),
            new DimensionInjectionData("vortex_space_station_wp",-6026,"Vortex Space Station", "_vortex_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitVortex",false),
            new DimensionInjectionData("vortex_space_station_alt_wp",-6027,"Vortex Space Station", "_vortex_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitVortex",true),
            new DimensionInjectionData("metztli_space_station_wp",-6028,"Metztli Space Station", "_metztli_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitMetztli",false),
            new DimensionInjectionData("metztli_space_station_alt_wp",-6029,"Metztli Space Station", "_metztli_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitMetztli",true),
            new DimensionInjectionData("centotl_space_station_wp",-6030,"Centotl Space Station", "_centotl_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitCentotl",false),
            new DimensionInjectionData("centotl_space_station_alt_wp",-6031,"Centotl Space Station", "_centotl_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitCentotl",true),
            new DimensionInjectionData("toci_space_station_wp",-6032,"Toci Space Station", "_toci_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitToci",false),
            new DimensionInjectionData("toci_space_station_alt_wp",-6035,"Toci Space Station", "_toci_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitToci",true),
            new DimensionInjectionData("tlaloc_space_station_wp",-6034,"Tlaloc Space Station", "_tlaloc_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitTlaloc",false),
            new DimensionInjectionData("tlaloc_space_station_alt_wp",-6035,"Tlaloc Space Station", "_tlaloc_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitTlaloc",true),
            new DimensionInjectionData("kronos_space_station_wp",-6036,"Kronos Space Station", "_kronos_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitKronos",false),
            new DimensionInjectionData("kronos_space_station_alt_wp",-6037,"Kronos Space Station", "_kronos_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitKronos",true),
            new DimensionInjectionData("maveth_space_station_wp",-6038,"Maveth Space Station", "_maveth_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitMaveth",false),
            new DimensionInjectionData("maveth_space_station_alt_wp",-6039,"Maveth Space Station", "_maveth_orbit","zollerngalaxy.core.dimensions.worldproviders.orbit.WorldProviderOrbitMaveth",true),
            new DimensionInjectionData("ceres_space_station_wp",-65,"Ceres Space Station", "_ceres_orbit","com.mjr.extraplanets.planets.Ceres.spacestation.WorldProviderCeresOrbit",false),
            new DimensionInjectionData("eris_space_station_wp",-79,"Eris Space Station", "_eris_orbit","com.mjr.extraplanets.planets.Eris.spacestation.WorldProviderErisOrbit",false),
            new DimensionInjectionData("jupiter_space_station_wp",-69,"Jupiter Space Station", "_jupiter_orbit","com.mjr.extraplanets.planets.Jupiter.spacestation.WorldProviderJupiterOrbit",false),
            new DimensionInjectionData("mercury_space_station_wp",-61,"Mercury Space Station", "_mercury_orbit","com.mjr.extraplanets.planets.Mercury.spacestation.WorldProviderMercuryOrbit",false),
            new DimensionInjectionData("neptune_space_station_wp",-75,"Neptune Space Station", "_neptune_orbit","com.mjr.extraplanets.planets.Neptune.spacestation.WorldProviderNeptuneOrbit",false),
            new DimensionInjectionData("pluto_space_station_wp",-77,"Pluto Space Station", "_pluto_orbit","com.mjr.extraplanets.planets.Pluto.spacestation.WorldProviderPlutoOrbit",false),
            new DimensionInjectionData("saturn_space_station_wp",-71,"Saturn Space Station", "_saturn_orbit","com.mjr.extraplanets.planets.Saturn.spacestation.WorldProviderSaturnOrbit",false),
            new DimensionInjectionData("uranus_space_station_wp",-73,"Uranus Space Station", "_uranus_orbit","com.mjr.extraplanets.planets.Uranus.spacestation.WorldProviderUranusOrbit",false),
            new DimensionInjectionData("kepler22b_space_station_wp",-81,"Kepler22b","Space Station orbit","com.mjr.extraplanets.planets.Kepler22b.spacestation.WorldProviderKepler22bOrbit",false),
            new DimensionInjectionData("mars_space_station_wp",-67,"Mars Space Station", "_mars_orbit","com.mjr.extraplanets.planets.mars.spacestation.WorldProviderMarsOrbit",false),
            new DimensionInjectionData("venus_space_station_wp",-63,"Venus Space Station", "_venus_orbit","com.mjr.extraplanets.planets.venus.spacestation.WorldProviderVenusOrbit",false),
            new DimensionInjectionData("ceres_space_station_alt_wp",-64,"Ceres Space Station", "_ceres_orbit","com.mjr.extraplanets.planets.Ceres.spacestation.WorldProviderCeresOrbit",true),
            new DimensionInjectionData("eris_space_station_alt_wp",-78,"Eris Space Station", "_eris_orbit","com.mjr.extraplanets.planets.Eris.spacestation.WorldProviderErisOrbit",true),
            new DimensionInjectionData("jupiter_space_station_alt_wp",-68,"Jupiter Space Station", "_jupiter_orbit","com.mjr.extraplanets.planets.Jupiter.spacestation.WorldProviderJupiterOrbit",true),
            new DimensionInjectionData("mercury_space_station_alt_wp",-82,"Mercury Space Station", "_mercury_orbit","com.mjr.extraplanets.planets.Mercury.spacestation.WorldProviderMercuryOrbit",true),
            new DimensionInjectionData("neptune_space_station_alt_wp",-74,"Neptune Space Station", "_neptune_orbit","com.mjr.extraplanets.planets.Neptune.spacestation.WorldProviderNeptuneOrbit",true),
            new DimensionInjectionData("pluto_space_station_alt_wp",-76,"Pluto Space Station", "_pluto_orbit","com.mjr.extraplanets.planets.Pluto.spacestation.WorldProviderPlutoOrbit",true),
            new DimensionInjectionData("saturn_space_station_alt_wp",-70,"Saturn Space Station", "_saturn_orbit","com.mjr.extraplanets.planets.Saturn.spacestation.WorldProviderSaturnOrbit",true),
            new DimensionInjectionData("uranus_space_station_alt_wp",-72,"Uranus Space Station", "_uranus_orbit","com.mjr.extraplanets.planets.Uranus.spacestation.WorldProviderUranusOrbit",true),
            new DimensionInjectionData("kepler22b_space_station_alt_wp",-80,"Kepler22b","Space Station orbit","com.mjr.extraplanets.planets.Kepler22b.spacestation.WorldProviderKepler22bOrbit",true),
            new DimensionInjectionData("mars_space_station_alt_wp",-66,"Mars Space Station", "_mars_orbit","com.mjr.extraplanets.planets.mars.spacestation.WorldProviderMarsOrbit",true),
            new DimensionInjectionData("venus_space_station_alt_wp",-62,"Venus Space Station", "_venus_orbit","com.mjr.extraplanets.planets.venus.spacestation.WorldProviderVenusOrbit",true)
    );

    private final Logger logger;

    DimensionTypeTransformer(final LaunchPlugin owner, final Logger logger) {
        super(
                TransformerData.Builder.create()
                        .setOwningPlugin(owner)
                        .setName("dimension_type")
                        .setDescription("Transforms DimensionType to automatically account for all necessary dimensions")
                        .setDisabledByDefault()
                        .build(),
                ClassDescriptor.of(DIMENSION_TYPE)
        );
        this.logger = logger;
        forceInnerInit();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void forceInnerInit() { // Force initialization to work around dumb 1.12.2 classloading
        StaticInitializationMethodVisitor.class.toString();
        RegisterMethodVisitor.class.toString();
        ConstructorVisitor.class.toString();
        CreateDimensionMethodVisitor.class.toString();
        FinderMethodInjector.class.toString();
        ConstructorInjector.class.toString();
        FieldInjector.class.toString();
        IdentityLambdaInjector.class.toString();
        SupplierLambdaInjector.class.toString();
    }

    private static String targetingSupplierName(final int index) {
        return "fermion-injected-lambda$dimension-hopper-tweaks$supplier$targeting" + index;
    }

    @Nonnull
    @Override
    public BiFunction<Integer, ClassVisitor, ClassVisitor> getClassVisitorCreator() {
        return (v, parent) -> new ClassVisitor(v, parent) {
            @Override
            public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
                super.visit(version, access, name, signature, superName, interfaces);
                logger.info("Initiated DimensionType extremely invasive transformation: here be dragons");
            }

            @Override
            public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
                final MethodVisitor parent = super.visitMethod(access, name, desc, signature, exceptions);
                if ("<clinit>".equals(name)) {
                    logger.info("Static initializer {} identified: attempting transformation", name);
                    return new StaticInitializationMethodVisitor(v, parent, logger);
                }
                if ("<init>".equals(name) && !DT_NEW_INIT_DESC.equals(desc)) {
                    logger.info("Constructor identified ({} {}): attempting transformation", name, desc);
                    return new ConstructorVisitor(v, parent, logger);
                }
                if ("register".equals(name)) {
                    logger.info("Register method identified ({} {}): attempting transformation", name, desc);
                    return new RegisterMethodVisitor(v, parent, logger);
                }
                if (MappingUtilities.INSTANCE.mapMethod("func_186070_d").equals(name)) {
                    logger.info("Found create dimension or equivalent SRG in {} {}: attempting transformation", name, desc);
                    return new CreateDimensionMethodVisitor(v, parent, logger);
                }
                return parent;
            }

            @Override
            public void visitEnd() {
                logger.info("Reached end of class: injecting additional methods and fields");
                FinderMethodInjector.inject(v, this, logger);
                ConstructorInjector.inject(v, this, logger);
                FieldInjector.inject(this, logger);
                IdentityLambdaInjector.inject(v, this, logger);
                SupplierLambdaInjector.inject(v, this, logger);
                super.visitEnd();
                logger.info("Transformation complete: here be dragons... AGAIN");
            }
        };
    }
}
