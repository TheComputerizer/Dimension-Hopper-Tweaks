package mods.thecomputerizer.dimhoppertweaks.mixin.mods.aquaculture;

import com.teammetallurgy.aquaculture.items.ItemTreasureChest;
import com.teammetallurgy.aquaculture.loot.WeightedLootSet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@Mixin(value = ItemTreasureChest.class, remap = false)
public abstract class MixinItemTreasureChest {

    @Shadow private WeightedLootSet loot;

    @Inject(at = @At(value = "HEAD"), method = "onItemRightClick")
    private void dimhoppertweaks$onItemRightClick(
            ItemStack stack, World world, EntityPlayer player, CallbackInfoReturnable<ItemStack> cir) {
        if(!world.isRemote) {
            this.loot = new WeightedLootSet();
            this.loot.addLoot(Items.IRON_INGOT,40);
            this.loot.addLoot(Items.GOLD_INGOT,30);
        }
    }
}