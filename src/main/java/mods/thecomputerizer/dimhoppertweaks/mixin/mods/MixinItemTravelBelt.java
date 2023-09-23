package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.equipment.bauble.ItemTravelBelt;

import java.util.List;
import java.util.Objects;

@Mixin(value = ItemTravelBelt.class, remap = false)
public abstract class MixinItemTravelBelt {

    @Shadow public static String playerStr(EntityPlayer player) {
        return null;
    }

    @Shadow @Final public static List<String> playersWithStepup;

    @Shadow protected abstract boolean shouldPlayerHaveStepup(EntityPlayer player);

    /**
     * @author The_Computerizer
     * @reason Account for null bauble capability
     */
    @SubscribeEvent
    @Overwrite
    public void updatePlayerStepStatus(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)event.getEntityLiving();
            String s = playerStr(player);
            IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
            if(Objects.nonNull(handler)) {
                ItemStack belt = handler.getStackInSlot(3);
                if (playersWithStepup.contains(s)) {
                    if (this.shouldPlayerHaveStepup(player)) {
                        ItemTravelBelt beltItem = (ItemTravelBelt) belt.getItem();
                        if (player.world.isRemote) {
                            if ((player.onGround || player.capabilities.isFlying) && player.moveForward > 0f &&
                                    !player.isInsideOfMaterial(Material.WATER)) {
                                float speed = beltItem.getSpeed(belt);
                                player.moveRelative(0f, 0f, 1f, speed);
                                beltItem.onMovedTick(belt, player);
                                if (player.ticksExisted % 10 == 0) {
                                    ManaItemHandler.requestManaExact(belt, player, 1, true);
                                }
                            } else beltItem.onNotMovingTick(belt, player);
                        }
                        if (player.isSneaking()) player.stepHeight = 0.60001f;
                        else player.stepHeight = 1.25f;
                    } else {
                        player.stepHeight = 0.6f;
                        playersWithStepup.remove(s);
                    }
                } else if (this.shouldPlayerHaveStepup(player)) {
                    playersWithStepup.add(s);
                    player.stepHeight = 1.25f;
                }
            }
        }
    }



    @Redirect(at = @At(value = "INVOKE", target = "Lbaubles/api/cap/IBaublesItemHandler;" +
            "getStackInSlot(I)Lnet/minecraft/item/ItemStack;"), method = "onPlayerJump")
    private ItemStack dimhoppertweaks$redirectGetStackInSlot(IBaublesItemHandler handler, int i) {
        return Objects.nonNull(handler) ? handler.getStackInSlot(i) : ItemStack.EMPTY;
    }
}
