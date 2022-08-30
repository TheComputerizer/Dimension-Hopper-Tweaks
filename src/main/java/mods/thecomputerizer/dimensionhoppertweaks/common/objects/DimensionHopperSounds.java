package mods.thecomputerizer.dimensionhoppertweaks.common.objects;

import mods.thecomputerizer.dimensionhoppertweaks.DimensionHopperTweaks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(DimensionHopperTweaks.MODID)
@Mod.EventBusSubscriber(modid = DimensionHopperTweaks.MODID)
public class DimensionHopperSounds {
    public static final SoundEvent SHORT_STATIC = makeSoundEvent("boss.static");
    public static final SoundEvent TEST_MUSIC = makeSoundEvent("boss.test_music");

    @SubscribeEvent
    public static void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(SHORT_STATIC);
    }

    private static SoundEvent makeSoundEvent(final String name) {
        ResourceLocation id = new ResourceLocation(DimensionHopperTweaks.MODID, name);
        return new SoundEvent(id).setRegistryName(name);
    }
}
