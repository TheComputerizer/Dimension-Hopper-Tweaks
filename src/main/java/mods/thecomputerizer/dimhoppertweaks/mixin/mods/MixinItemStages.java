package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import mods.thecomputerizer.dimhoppertweaks.mixin.access.DelayedModAccess;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.TileEntityAccess;
import mods.thecomputerizer.dimhoppertweaks.util.WorldUtil;
import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.itemstages.ConfigurationHandler;
import net.darkhax.itemstages.ItemStages;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;
import java.util.Objects;

@Mixin(value = ItemStages.class, remap = false)
public abstract class MixinItemStages {

    @Unique private boolean dimhoppertweaks$verifyHooks(World world, @Nullable EntityPlayer player, BlockPos pos, String stage) {
        TileEntity tile = WorldUtil.getTileOrAdjacent(world,pos,true, DelayedModAccess.getPlacerTileClasses());
        if(Objects.nonNull(tile)) return !((TileEntityAccess)tile).dimhoppertweaks$hasStage(stage);
        else if(Objects.nonNull(player) && !player.getPosition().equals(pos)) {
            tile = WorldUtil.getTileOrAdjacent(world,player.getPosition(),true, DelayedModAccess.getPlacerTileClasses());
            if(Objects.nonNull(tile)) return !((TileEntityAccess)tile).dimhoppertweaks$hasStage(stage);
        }
        return Objects.nonNull(player) && !GameStageHelper.hasStage(player,stage);
    }

    @Unique private boolean dimhoppertweaks$verifyStage(World world, EntityPlayer player, BlockPos pos, String stage) {
        return Objects.isNull(player) || player instanceof FakePlayer ?
                dimhoppertweaks$verifyHooks(world,player,pos,stage) : !GameStageHelper.hasStage(player,stage);
    }

    @Shadow public static String getStage(ItemStack stack) {
        return null;
    }

    @Shadow public static String getEnchantStage(ItemStack stack) {
        return null;
    }

    @Shadow private static void sendAttackFailMessage(EntityPlayer player, ItemStack stack) {}

    /**
     * @author The_Computerizer
     * @reason Hook for fixing fake players using staged items
     */
    @SubscribeEvent
    @Overwrite
    public void onPlayerDig(PlayerEvent.BreakSpeed event) {
        if (!ConfigurationHandler.allowInteractRestricted && !event.getEntityPlayer().isCreative()) {
            EntityPlayer player = event.getEntityPlayer();
            ItemStack heldItem = player.getHeldItemMainhand();
            String stage = getStage(heldItem);
            String enchantStage = getEnchantStage(heldItem);
            if((Objects.nonNull(stage) && dimhoppertweaks$verifyStage(player.getEntityWorld(),player,player.getPosition(),stage)) ||
                    (Objects.nonNull(enchantStage) && dimhoppertweaks$verifyStage(player.getEntityWorld(),player,player.getPosition(),enchantStage))) {
                event.setNewSpeed(-1f);
                event.setCanceled(true);
            }
        }

    }

    /**
     * @author The_Computerizer
     * @reason Hook for fixing fake players using staged items
     */
    @SubscribeEvent
    @Overwrite
    public void onPlayerAttack(AttackEntityEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        if(!ConfigurationHandler.allowInteractRestricted && !player.isCreative()) {
            String stage = getEnchantStage(player.getHeldItemMainhand());
            if(Objects.nonNull(stage) && dimhoppertweaks$verifyStage(player.getEntityWorld(),player,player.getPosition(),stage)) {
                if (event.getEntityPlayer().world.getTotalWorldTime() % 2L == 0L)
                    sendAttackFailMessage(player,player.getHeldItemMainhand());
                event.setCanceled(true);
            }
        }

    }

    /**
     * @author The_Computerizer
     * @reason Hook for fixing fake players using staged items
     */
    @SubscribeEvent
    @Overwrite
    public void onPlayerInteract(PlayerInteractEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        if(event.isCancelable() && !ConfigurationHandler.allowInteractRestricted && !player.isCreative()) {
            String stage = getStage(event.getItemStack());
            if(Objects.nonNull(stage) && dimhoppertweaks$verifyStage(player.getEntityWorld(),player,event.getPos(),stage))
                event.setCanceled(true);
        }

    }
}
