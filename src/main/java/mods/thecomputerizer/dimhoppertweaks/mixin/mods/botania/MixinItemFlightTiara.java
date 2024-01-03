package mods.thecomputerizer.dimhoppertweaks.mixin.mods.botania;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;

import java.util.List;
import java.util.Objects;

@Mixin(value = ItemFlightTiara.class, remap = false)
public abstract class MixinItemFlightTiara {

    @Shadow @Final public static List<String> playersWithFlight;

    @Shadow public static String playerStr(EntityPlayer player) {
        return null;
    }

    @Shadow protected abstract boolean shouldPlayerHaveFlight(EntityPlayer player);

    @Shadow public abstract int getCost(ItemStack stack, int timeLeft);

    /**
     * @author The_Computerizer
     * @reason Account for null bauble capability
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SubscribeEvent
    @Overwrite
    public void updatePlayerFlyStatus(LivingEvent.LivingUpdateEvent event) {
        if(event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
            if (Objects.nonNull(handler)) {
                ItemStack tiara = handler.getStackInSlot(4);
                int left = ItemNBTHelper.getInt(tiara,"timeLeft",1200);
                if (playersWithFlight.contains(playerStr(player))) {
                    if (this.shouldPlayerHaveFlight(player)) {
                        player.capabilities.allowFlying = true;
                        if (player.capabilities.isFlying) {
                            if (!player.world.isRemote) {
                                ManaItemHandler.requestManaExact(tiara,player,this.getCost(tiara,left),true);
                            } else if (Math.abs(player.motionX) > 0.1 || Math.abs(player.motionZ) > 0.1) {
                                double x = event.getEntityLiving().posX - 0.5;
                                double y = event.getEntityLiving().posY - 0.5;
                                double z = event.getEntityLiving().posZ - 0.5;
                                player.getGameProfile().getName();
                                float r = 1f;
                                float g = 1f;
                                float b = 1f;
                                switch (tiara.getItemDamage()) {
                                    case 2:
                                        r = 0.1f;
                                        g = 0.1f;
                                        b = 0.1f;
                                        break;
                                    case 3:
                                        r = 0.0f;
                                        g = 0.6f;
                                        break;
                                    case 4:
                                        g = 0.3f;
                                        b = 0.3f;
                                        break;
                                    case 5:
                                        r = 0.6f;
                                        g = 0.0f;
                                        b = 0.6f;
                                        break;
                                    case 6:
                                        r = 0.4f;
                                        g = 0.0f;
                                        b = 0.0f;
                                        break;
                                    case 7:
                                        r = 0.2f;
                                        g = 0.6f;
                                        b = 0.2f;
                                        break;
                                    case 8:
                                        r = 0.85f;
                                        g = 0.85f;
                                        b = 0.0f;
                                        break;
                                    case 9:
                                        r = 0f;
                                        b = 0f;
                                }

                                for (int i = 0; i < 2; ++i)
                                    Botania.proxy.sparkleFX(x+Math.random()*(double)event.getEntityLiving().width,
                                            y+Math.random()*0.4,z+Math.random()*(double)event.getEntityLiving().width,
                                            r,g,b,2f*(float)Math.random(),20);
                            }
                        }
                    } else {
                        if (!player.isSpectator() && !player.capabilities.isCreativeMode) {
                            player.capabilities.allowFlying = false;
                            player.capabilities.isFlying = false;
                            player.capabilities.disableDamage = false;
                        }
                        playersWithFlight.remove(playerStr(player));
                    }
                } else if (this.shouldPlayerHaveFlight(player)) {
                    playersWithFlight.add(playerStr(player));
                    player.capabilities.allowFlying = true;
                }
            }
        }
    }
}
