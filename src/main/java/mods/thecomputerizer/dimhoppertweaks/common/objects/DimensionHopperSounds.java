package mods.thecomputerizer.dimhoppertweaks.common.objects;

import mods.thecomputerizer.dimhoppertweaks.Constants;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(Constants.MODID)
@Mod.EventBusSubscriber(modid = Constants.MODID)
public class DimensionHopperSounds {
    public static final SoundEvent SHORT_STATIC = makeSoundEvent("boss.static");
    public static final SoundEvent SPAWN = makeSoundEvent("boss.spawn");

    @SubscribeEvent
    public static void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(SHORT_STATIC);
    }

    private static SoundEvent makeSoundEvent(final String name) {
        ResourceLocation id = new ResourceLocation(Constants.MODID, name);
        return new SoundEvent(id).setRegistryName(name);
    }
}
