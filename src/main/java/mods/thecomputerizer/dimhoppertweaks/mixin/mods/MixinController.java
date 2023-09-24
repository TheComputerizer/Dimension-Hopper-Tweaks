package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import me.towdium.jecalculation.JecaCapability;
import me.towdium.jecalculation.JecaConfig;
import me.towdium.jecalculation.JustEnoughCalculation;
import me.towdium.jecalculation.data.Controller;
import me.towdium.jecalculation.data.structure.RecordPlayer;
import me.towdium.jecalculation.network.packets.PRecord;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Objects;

@SuppressWarnings("ConstantValue")
@Mixin(value = Controller.class, remap = false)
public class MixinController {

    /**
     * @author The_Computerizer
     * @reason Fix null capability issue
     */
    @SubscribeEvent
    @Overwrite
    public static void onJoin(PlayerEvent.PlayerLoggedInEvent e) {
        if(!JecaConfig.clientMode) {
            RecordPlayer record = JecaCapability.getRecord(e.player);
            if(Objects.nonNull(record))
                JustEnoughCalculation.network.sendTo(new PRecord(record),(EntityPlayerMP)e.player);
        }
    }
}
