package mods.thecomputerizer.dimhoppertweaks.mixin.mods;

import mods.thecomputerizer.dimhoppertweaks.mixin.access.DelayedModAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.tslat.aoa3.client.event.KeyBinder;
import net.tslat.aoa3.client.gui.mainwindow.AdventMainGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;

@Mixin(value = KeyBinder.class, remap = false)
public class MixinKeyBinder {

    @Shadow public static KeyBinding keyCreatureStats;

    @Shadow public static boolean statusCreatureStats;

    @Shadow public static KeyBinding keyResourceGui;

    @Shadow public static boolean statusResourceGui;

    @Shadow public static boolean statusResourceGuiMessage;

    @Shadow public static KeyBinding keySkillGui;

    @Shadow public static boolean statusSkillGui;

    @Shadow public static boolean statusSkillGuiMessage;

    @Shadow public static KeyBinding keyAdventGui;

    /**
     * @author The_Computerizer
     * @reason Mark skill key as pressed
     */
    @Overwrite
    @SubscribeEvent
    public void onKeyDown(InputEvent.KeyInputEvent ev) {
        if(keyCreatureStats.isPressed()) statusCreatureStats = !statusCreatureStats;
        if(keyResourceGui.isPressed()) {
            statusResourceGui = !statusResourceGui;
            statusResourceGuiMessage = false;
        }
        if(keySkillGui.isPressed()) {
            statusSkillGui = !statusSkillGui;
            statusSkillGuiMessage = false;
            DelayedModAccess.sendKeyPress(1);
        }
        Minecraft mc = Minecraft.getMinecraft();
        if(keyAdventGui.isPressed() && Objects.nonNull(mc.player)) {
            if(mc.currentScreen instanceof AdventMainGui) mc.displayGuiScreen(null);
            else if(Objects.isNull(mc.currentScreen)) mc.displayGuiScreen(new AdventMainGui(mc.player));
        }
    }
}
