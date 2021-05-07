package mods.thecomputerizer.dimensionhoppertweaks.asm;

import com.google.common.collect.ImmutableList;
import net.thesilkminer.mc.fermion.asm.api.LaunchPlugin;
import net.thesilkminer.mc.fermion.asm.api.MappingUtilities;
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

public final class ItemSwordInfinityTransformer extends SingleTargetMethodTransformer {
    private final Logger logger;

    protected ItemSwordInfinityTransformer(final LaunchPlugin owner, final Logger logger) {
        super(
                TransformerData.Builder.create()
                        .setOwningPlugin(owner)
                        .setName("item_sword_infinity")
                        .setDescription("Transforms the Infinity Sword so that it cannot automatically kill the Final Boss")
                        .build(),
                ClassDescriptor.of("morph.avaritia.item.tools.ItemSwordInfinity"),
                MethodDescriptor.of(
                        "func_77644_a",
                        ImmutableList.of(
                                ClassDescriptor.of("net.minecraft.item.ItemStack"),
                                ClassDescriptor.of("net.minecraft.entity.EntityLivingBase"),
                                ClassDescriptor.of("net.minecraft.entity.EntityLivingBase")
                        ),
                        ClassDescriptor.of(boolean.class)
                )
        );
        this.logger = logger;
    }

    @Nonnull
    @Override
    @SuppressWarnings("SpellCheckingInspection")
    protected BiFunction<Integer, MethodVisitor, MethodVisitor> getMethodVisitorCreator() {
        return (v, mv) -> new MethodVisitor(v, mv) {
            //  // access flags 0x1
            //  public hitEntity(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/entity/EntityLivingBase;)Z
            //   L0
            //    LINENUMBER 46 L0
            //    ALOAD 3
            //    GETFIELD net/minecraft/entity/EntityLivingBase.world : Lnet/minecraft/world/World;
            //    GETFIELD net/minecraft/world/World.isRemote : Z
            //    IFEQ L1
            //   L2
            //    LINENUMBER 47 L2
            //    ICONST_1
            //    IRETURN
            //   L1
            //    LINENUMBER 49 L1
            //   FRAME SAME
            // <<< INJECTION BEGIN
            //   L800
            //    LINENUMBER 800 L800
            //    ALOAD 2
            //    INSTANCEOF mods/thecomputerizer/dimensionhoppertweaks/common/objects/entity/EntityFinalBoss
            //    IFEQ L801
            //   L802
            //    LINENUMBER 801 L802
            //    ICONST_1
            //    IRETURN
            //   L801
            //    LINENUMBER 49 L801
            //   FRAME SAME
            // >>> INJECTION END
            //    ALOAD 2
            //    INSTANCEOF net/minecraft/entity/player/EntityPlayer
            //    IFEQ L3
            //   L4
            //    LINENUMBER 50 L4
            //    ALOAD 2
            //    CHECKCAST net/minecraft/entity/player/EntityPlayer
            //    ASTORE 4
            //   L5
            //    LINENUMBER 51 L5
            //    ALOAD 4
            //    INVOKESTATIC morph/avaritia/handler/AvaritiaEventHandler.isInfinite (Lnet/minecraft/entity/player/EntityPlayer;)Z
            //    IFEQ L6
            //   L7
            //    LINENUMBER 52 L7
            //    ALOAD 2
            //    NEW morph/avaritia/util/DamageSourceInfinitySword
            //    DUP
            //    ALOAD 3
            //    INVOKESPECIAL morph/avaritia/util/DamageSourceInfinitySword.<init> (Lnet/minecraft/entity/Entity;)V
            //    INVOKEVIRTUAL morph/avaritia/util/DamageSourceInfinitySword.setDamageBypassesArmor ()Lnet/minecraft/util/DamageSource;
            //    LDC 4.0
            //    INVOKEVIRTUAL net/minecraft/entity/EntityLivingBase.attackEntityFrom (Lnet/minecraft/util/DamageSource;F)Z
            //    POP
            //   L8
            //    LINENUMBER 53 L8
            //    ICONST_1
            //    IRETURN
            //   L6
            //    LINENUMBER 55 L6
            //   FRAME APPEND [net/minecraft/entity/player/EntityPlayer]
            //    ALOAD 4
            //    GETSTATIC net/minecraft/util/EnumHand.MAIN_HAND : Lnet/minecraft/util/EnumHand;
            //    INVOKEVIRTUAL net/minecraft/entity/player/EntityPlayer.getHeldItem (Lnet/minecraft/util/EnumHand;)Lnet/minecraft/item/ItemStack;
            //    IFNULL L3
            //    ALOAD 4
            //    GETSTATIC net/minecraft/util/EnumHand.MAIN_HAND : Lnet/minecraft/util/EnumHand;
            //    INVOKEVIRTUAL net/minecraft/entity/player/EntityPlayer.getHeldItem (Lnet/minecraft/util/EnumHand;)Lnet/minecraft/item/ItemStack;
            //    INVOKEVIRTUAL net/minecraft/item/ItemStack.getItem ()Lnet/minecraft/item/Item;
            //    GETSTATIC morph/avaritia/init/ModItems.infinity_sword : Lmorph/avaritia/item/tools/ItemSwordInfinity;
            //    IF_ACMPNE L3
            //    ALOAD 4
            //    INVOKEVIRTUAL net/minecraft/entity/player/EntityPlayer.isHandActive ()Z
            //    IFEQ L3
            //   L9
            //    LINENUMBER 56 L9
            //    ICONST_1
            //    IRETURN
            //   L3
            //    LINENUMBER 60 L3
            //   FRAME CHOP 1
            //    ALOAD 2
            //    BIPUSH 60
            //    PUTFIELD net/minecraft/entity/EntityLivingBase.recentlyHit : I
            //   L10
            //    LINENUMBER 61 L10
            //    ALOAD 2
            //    INVOKEVIRTUAL net/minecraft/entity/EntityLivingBase.getCombatTracker ()Lnet/minecraft/util/CombatTracker;
            //    NEW morph/avaritia/util/DamageSourceInfinitySword
            //    DUP
            //    ALOAD 3
            //    INVOKESPECIAL morph/avaritia/util/DamageSourceInfinitySword.<init> (Lnet/minecraft/entity/Entity;)V
            //    ALOAD 2
            //    INVOKEVIRTUAL net/minecraft/entity/EntityLivingBase.getHealth ()F
            //    ALOAD 2
            //    INVOKEVIRTUAL net/minecraft/entity/EntityLivingBase.getHealth ()F
            //    INVOKEVIRTUAL net/minecraft/util/CombatTracker.trackDamage (Lnet/minecraft/util/DamageSource;FF)V
            //   L11
            //    LINENUMBER 62 L11
            //    ALOAD 2
            //    FCONST_0
            //    INVOKEVIRTUAL net/minecraft/entity/EntityLivingBase.setHealth (F)V
            //   L12
            //    LINENUMBER 63 L12
            //    ALOAD 2
            //    NEW net/minecraft/util/EntityDamageSource
            //    DUP
            //    LDC "infinity"
            //    ALOAD 3
            //    INVOKESPECIAL net/minecraft/util/EntityDamageSource.<init> (Ljava/lang/String;Lnet/minecraft/entity/Entity;)V
            //    INVOKEVIRTUAL net/minecraft/entity/EntityLivingBase.onDeath (Lnet/minecraft/util/DamageSource;)V
            //   L13
            //    LINENUMBER 64 L13
            //    ICONST_1
            //    IRETURN
            //   L14
            //    LOCALVARIABLE pvp Lnet/minecraft/entity/player/EntityPlayer; L5 L3 4
            //    LOCALVARIABLE this Lmorph/avaritia/item/tools/ItemSwordInfinity; L0 L14 0
            //    LOCALVARIABLE stack Lnet/minecraft/item/ItemStack; L0 L14 1
            //    LOCALVARIABLE victim Lnet/minecraft/entity/EntityLivingBase; L0 L14 2
            //    LOCALVARIABLE player Lnet/minecraft/entity/EntityLivingBase; L0 L14 3
            //    MAXSTACK = 5
            //    MAXLOCALS = 5

            private boolean seenWorldCheck = false;
            private boolean seenIConst1 = false;
            private boolean seenIReturn = false;
            private boolean injected = false;

            @Override
            public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
                super.visitFieldInsn(opcode, owner, name, desc);

                if (this.injected) return;

                if (opcode == Opcodes.GETFIELD && "net/minecraft/world/World".equals(owner) &&
                        MappingUtilities.INSTANCE.mapField("field_72995_K").equals(name) && "Z".equals(desc)) {
                    logger.info("Identified target 'GETFIELD World.isRemote'");
                    this.seenWorldCheck = true;
                }
            }

            @Override
            public void visitInsn(final int opcode) {
                super.visitInsn(opcode);

                if (this.injected) return;
                if (!this.seenWorldCheck) return;

                if (opcode == Opcodes.ICONST_1) {
                    logger.info("Identified 'ICONST_1");
                    this.seenIConst1 = true;
                    return;
                }

                if (!this.seenIConst1) return;

                if (opcode == Opcodes.IRETURN) {
                    logger.info("Identified 'IRETURN'");
                    this.seenIReturn = true;
                }
            }

            @Override
            public void visitFrame(final int type, final int nLocal, final Object[] local, final int nStack, final Object[] stack) {
                super.visitFrame(type, nLocal, local, nStack, stack);

                if (this.injected) return;
                if (!this.seenWorldCheck) return;
                if (!this.seenIConst1) return;
                if (!this.seenIReturn) return;

                if (type != Opcodes.F_SAME) return;

                logger.info("Identified same frame: injecting");

                final Label l800 = new Label();
                final Label l801 = new Label();
                final Label l802 = new Label();

                super.visitLabel(l800);
                super.visitLineNumber(800, l800);
                super.visitVarInsn(Opcodes.ALOAD, 2);
                super.visitTypeInsn(Opcodes.INSTANCEOF, "mods/thecomputerizer/dimensionhoppertweaks/common/objects/entity/EntityFinalBoss");
                super.visitJumpInsn(Opcodes.IFEQ, l801);

                super.visitLabel(l802);
                super.visitLineNumber(801, l802);
                super.visitInsn(Opcodes.ICONST_1);
                super.visitInsn(Opcodes.IRETURN);

                super.visitLabel(l801);
                super.visitLineNumber(49, l801);
                super.visitFrame(Opcodes.F_SAME, 0, null, 0, null);

                logger.info("Injection completed");
                this.injected = true;
            }
        };
    }
}
