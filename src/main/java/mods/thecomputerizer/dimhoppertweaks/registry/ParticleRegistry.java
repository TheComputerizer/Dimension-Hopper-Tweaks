package mods.thecomputerizer.dimhoppertweaks.registry;

import mods.thecomputerizer.dimhoppertweaks.client.particle.ParticleAscii;
import mods.thecomputerizer.dimhoppertweaks.core.Constants;
import mods.thecomputerizer.theimpossiblelibrary.util.TextUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public final class ParticleRegistry {

    private static final Class<?>[] PARTICLE_INIT_CLASSES = {String.class, int.class, boolean.class};
    public static final EnumParticleTypes RANDOM_ASCII = registerParticle("RANDOM_ASCII",true);

    private static TextureAtlasSprite FONT_ATLAS = null;
    private static float ATLAS_RATIO;

    private static EnumParticleTypes registerParticle(String name, boolean ignoreRange) {
        String camelName = TextUtil.makeCaseTypeFromSnake(name,TextUtil.TextCasing.CAMEL);
        int id = EnumParticleTypes.values().length;
        Constants.LOGGER.error("REGISTERING PARTICLE WITH NAME {}",camelName);
        EnumParticleTypes ret = EnumHelper.addEnum(EnumParticleTypes.class,name,PARTICLE_INIT_CLASSES,camelName,id,ignoreRange);
        if(Objects.nonNull(ret)) {
            EnumParticleTypes.PARTICLES.put(ret.getParticleID(), ret);
            EnumParticleTypes.BY_NAME.put(ret.getParticleName(), ret);
        } else Constants.LOGGER.error("Failed to register particle {}!",camelName);
        return ret;
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void stitchEvent(TextureStitchEvent.Pre ev) {
        String texPath = Minecraft.getMinecraft().fontRenderer.locationFontTexture.getPath();
        texPath = texPath.substring(0,texPath.lastIndexOf(".")).replace("textures/","");
        FONT_ATLAS = ev.getMap().registerSprite(new ResourceLocation(texPath));
    }

    @SideOnly(Side.CLIENT)
    public static void postInit() {
        ATLAS_RATIO = FONT_ATLAS.getMinU()/ FONT_ATLAS.getOriginX();
        Minecraft.getMinecraft().effectRenderer.registerParticle(RANDOM_ASCII.getParticleID(),new ParticleAscii.Factory());
    }

    @SideOnly(Side.CLIENT)
    public static TextureAtlasSprite getParticleAtlas() {
        return FONT_ATLAS;
    }

    @SideOnly(Side.CLIENT)
    public static float getParticleAtlasRatio() {
        return ATLAS_RATIO;
    }
}
