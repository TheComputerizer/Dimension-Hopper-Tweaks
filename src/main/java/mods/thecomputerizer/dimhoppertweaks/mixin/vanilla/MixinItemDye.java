package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.common.capability.player.SkillWrapper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Random;

@Mixin(ItemDye.class)
public abstract class MixinItemDye {

    @Unique private static EntityPlayer dimhoppertweaks$savedPlayer;

    @Inject(at = @At("HEAD"), method = "applyBonemeal(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;"+
            "Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/EntityPlayer;"+
            "Lnet/minecraft/util/EnumHand;)Z", remap = false)
    private static void dimhoppertweaks$savePlayer(ItemStack stack, World world, BlockPos target, EntityPlayer player,
                                                   @Nullable EnumHand hand, CallbackInfoReturnable<Boolean> cir) {
        dimhoppertweaks$savedPlayer = player;
    }

    @Redirect(at = @At(value = "INVOKE",target = "Lnet/minecraft/item/ItemStack;shrink(I)V"),
            method = "applyBonemeal(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;"+
                    "Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/EntityPlayer;"+
                    "Lnet/minecraft/util/EnumHand;)Z")
    private static void dimhoppertweaks$shrinkChance(ItemStack stack, int count) {
        if(Objects.nonNull(dimhoppertweaks$savedPlayer) && !(dimhoppertweaks$savedPlayer instanceof FakePlayer)) {
            double prestigeFactor = (SkillWrapper.getPrestigeFactor(dimhoppertweaks$savedPlayer,"farming")-1d)/32d;
            Random rand = dimhoppertweaks$savedPlayer.getEntityWorld().rand;
            if(rand.nextDouble()>prestigeFactor) stack.shrink(count);
        }
    }

    @Inject(at = @At("RETURN"), method = "applyBonemeal(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;"+
            "Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/EntityPlayer;"+
            "Lnet/minecraft/util/EnumHand;)Z", remap = false)
    private static void dimhoppertweaks$resetPlayer(ItemStack stack, World world, BlockPos target, EntityPlayer player,
                                                    @Nullable EnumHand hand, CallbackInfoReturnable<Boolean> cir) {
        dimhoppertweaks$savedPlayer = null;
    }
}