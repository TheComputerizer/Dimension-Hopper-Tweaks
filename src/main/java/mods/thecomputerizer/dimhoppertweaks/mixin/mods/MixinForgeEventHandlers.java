package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import mcjty.theoneprobe.ForgeEventHandlers;
import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.items.ModItems;
import mcjty.theoneprobe.playerdata.PlayerGotNote;
import mcjty.theoneprobe.playerdata.PlayerProperties;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Objects;

@Mixin(value = ForgeEventHandlers.class, remap = false)
public class MixinForgeEventHandlers {

    /**
     * @author The_Computerizer
     * @reason Fix null capability issues
     */
    @SubscribeEvent
    @Overwrite
    public void onPlayerLoggedIn(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event) {
        if(Config.spawnNote) {
            PlayerGotNote note = PlayerProperties.getPlayerGotNote(event.player);
            if(Objects.nonNull(note) && !note.isPlayerGotNote()) {
                boolean success = event.player.inventory.addItemStackToInventory(new ItemStack(ModItems.probeNote));
                if(success) note.setPlayerGotNote(true);
            }
        }
    }
}
