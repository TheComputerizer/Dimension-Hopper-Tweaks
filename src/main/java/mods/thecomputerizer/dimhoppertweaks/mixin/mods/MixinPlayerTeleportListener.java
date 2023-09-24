package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import com.gildedgames.aether.api.registrar.CapabilitiesAether;
import com.gildedgames.aether.common.capabilities.entity.player.PlayerAether;
import com.gildedgames.aether.common.capabilities.entity.player.modules.PlayerTeleportingModule;
import com.gildedgames.aether.common.containers.ContainerLoadingScreen;
import com.gildedgames.aether.common.events.listeners.player.PlayerTeleportListener;
import com.gildedgames.aether.common.init.DimensionsAether;
import com.gildedgames.aether.common.network.NetworkingAether;
import com.gildedgames.aether.common.network.packets.PacketCloseLoadingScreen;
import com.gildedgames.aether.common.network.packets.PacketLoadingScreenPercent;
import com.gildedgames.aether.common.util.helpers.MathUtil;
import com.gildedgames.aether.common.world.preparation.PrepHelper;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("DataFlowIssue")
@Mixin(value = PlayerTeleportListener.class, remap = false)
public class MixinPlayerTeleportListener {

    /**
     * @author The_Computerizer
     * @reason Fix null capability issue
     */
    @SubscribeEvent
    @Overwrite
    public static void onEvent(LivingEvent.LivingUpdateEvent event) {
        if(!event.getEntity().getEntityWorld().isRemote && event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)event.getEntity();
            if(player.hasCapability(CapabilitiesAether.PLAYER_DATA,null)) {
                PlayerAether playerAether = PlayerAether.getPlayer(player);
                if(player.getEntityWorld().provider.getDimensionType() != DimensionsAether.AETHER) return;
                PlayerTeleportingModule teleportingModule = playerAether.getModule(PlayerTeleportingModule.class);
                if(player.openContainer instanceof ContainerLoadingScreen &&
                        PrepHelper.isSectorLoaded(player.getEntityWorld(), player.chunkCoordX, player.chunkCoordZ)) {
                    boolean isLoaded = true;
                    int radius = Math.min(player.getServer().getPlayerList().getViewDistance(), 10);
                    int count = 0;
                    for(int x = player.chunkCoordX - radius; x < player.chunkCoordX + radius; ++x) {
                        for(int z = player.chunkCoordZ - radius; z < player.chunkCoordZ + radius; ++z) {
                            Chunk chunk = player.world.getChunkProvider().getLoadedChunk(x, z);
                            if(Objects.isNull(chunk)) isLoaded = false;
                            else ++count;
                        }
                    }
                    if(isLoaded) {
                        player.closeScreen();
                        NetworkingAether.sendPacketToPlayer(new PacketCloseLoadingScreen(), (EntityPlayerMP) player);
                        NetworkingAether.sendPacketToPlayer(new PacketLoadingScreenPercent(0.0F), (EntityPlayerMP) player);
                        List<IRecipe> toUnlock = Lists.newArrayList();
                        for(IRecipe r : ForgeRegistries.RECIPES) {
                            ResourceLocation loc = Item.REGISTRY.getNameForObject(r.getRecipeOutput().getItem());
                            if(Objects.nonNull(loc) && loc.getNamespace().equals("aether")) toUnlock.add(r);
                        }
                        player.unlockRecipes(toUnlock);
                    } else {
                        float diam = (float) (radius + radius);
                        float percent = (float) count / (diam * diam) * 100.0F;
                        if(!MathUtil.epsilonEquals(teleportingModule.getLastPercent(), percent)) {
                            teleportingModule.setLastPercent(percent);
                            NetworkingAether.sendPacketToPlayer(new PacketLoadingScreenPercent(percent),(EntityPlayerMP) player);
                        }
                    }
                }
            }
        }
    }
}
