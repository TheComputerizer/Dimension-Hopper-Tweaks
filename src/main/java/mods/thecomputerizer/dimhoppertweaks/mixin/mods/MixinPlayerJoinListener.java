package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import com.gildedgames.aether.api.registrar.CapabilitiesAether;
import com.gildedgames.aether.common.capabilities.entity.player.PlayerAether;
import com.gildedgames.aether.common.events.listeners.player.PlayerJoinListener;
import com.gildedgames.aether.common.network.NetworkingAether;
import com.gildedgames.aether.common.network.packets.PacketRequestClientInfo;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;

@Mixin(value = PlayerJoinListener.class, remap = false)
public class MixinPlayerJoinListener {

    @Shadow @Final private static ResourceLocation TELEPORTER_RECIPE;

    /**
     * @author The_Computerizer
     * @reason Fix null capability issues
     */
    @SubscribeEvent
    @Overwrite
    public static void onPlayerJoined(PlayerEvent.PlayerLoggedInEvent event) {
        NetworkingAether.sendPacketToPlayer(new PacketRequestClientInfo(), (EntityPlayerMP)event.player);
        if(Objects.nonNull(CapabilitiesAether.PLAYER_DATA) && event.player.hasCapability(CapabilitiesAether.PLAYER_DATA,null)) {
            PlayerAether aePlayer = PlayerAether.getPlayer(event.player);
            aePlayer.sendFullUpdate();
        }
        IRecipe recipe = ForgeRegistries.RECIPES.getValue(TELEPORTER_RECIPE);
        if(Objects.nonNull(recipe)) event.player.unlockRecipes(Lists.newArrayList(recipe));
    }
}
