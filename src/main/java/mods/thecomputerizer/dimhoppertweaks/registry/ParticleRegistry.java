package mods.thecomputerizer.dimhoppertweaks.registry;

import mods.thecomputerizer.dimhoppertweaks.client.particle.ParticleAscii;
import mods.thecomputerizer.dimhoppertweaks.client.particle.ParticleBlightFire;
import mods.thecomputerizer.dimhoppertweaks.client.render.BetterBlightFireRenderer;
import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import mods.thecomputerizer.theimpossiblelibrary.util.TextUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;

import static net.minecraftforge.fml.relauncher.Side.CLIENT;

@Mod.EventBusSubscriber(modid = DHTRef.MODID)
public final class ParticleRegistry {

    public static final ResourceLocation PARTICLE_TEXTURES = new ResourceLocation("textures/particle/particles.png");
    private static final Class<?>[] PARTICLE_INIT_CLASSES = {String.class, int.class, boolean.class};
    public static final EnumParticleTypes RANDOM_ASCII = registerParticle("RANDOM_ASCII",true);
    public static final EnumParticleTypes BLIGHT_FIRE = registerParticle("BLIGHT_FIRE",false);

    private static TextureAtlasSprite FONT_ATLAS = null;
    private static TextureAtlasSprite BLIGHT_FIRE_ATLAS = null;
    private static TextureAtlasSprite GRAY_BLIGHT_FIRE_ATLAS = null;

    private static EnumParticleTypes registerParticle(String name, boolean ignoreRange) {
        String camelName = TextUtil.makeCaseTypeFromSnake(name,TextUtil.TextCasing.CAMEL);
        int id = EnumParticleTypes.values().length;
        DHTRef.LOGGER.info("Registrering particle with name {}",camelName);
        EnumParticleTypes ret = EnumHelper.addEnum(EnumParticleTypes.class,name,PARTICLE_INIT_CLASSES,camelName,id,ignoreRange);
        if(Objects.nonNull(ret)) {
            EnumParticleTypes.PARTICLES.put(ret.getParticleID(), ret);
            EnumParticleTypes.BY_NAME.put(ret.getParticleName(), ret);
        } else DHTRef.LOGGER.error("Failed to register particle {}!",camelName);
        return ret;
    }

    @SubscribeEvent
    @SideOnly(CLIENT)
    public static void stitchEvent(TextureStitchEvent.Pre ev) {
        String texPath = Minecraft.getMinecraft().fontRenderer.locationFontTexture.getPath();
        texPath = texPath.substring(0,texPath.lastIndexOf(".")).replace("textures/","");
        FONT_ATLAS = ev.getMap().registerSprite(new ResourceLocation(texPath));
        BLIGHT_FIRE_ATLAS = ev.getMap().registerSprite(BetterBlightFireRenderer.TEXTURE);
        GRAY_BLIGHT_FIRE_ATLAS = ev.getMap().registerSprite(BetterBlightFireRenderer.TEXTURE_GRAY);
    }

    @SideOnly(CLIENT)
    public static void postInit() {
        ParticleManager manager = Minecraft.getMinecraft().effectRenderer;
        manager.registerParticle(RANDOM_ASCII.getParticleID(),new ParticleAscii.Factory());
        manager.registerParticle(BLIGHT_FIRE.getParticleID(),new ParticleBlightFire.Factory());
    }

    @SideOnly(CLIENT)
    public static TextureAtlasSprite getFontAtlas() {
        return FONT_ATLAS;
    }

    @SideOnly(CLIENT)
    public static TextureAtlasSprite getFireAtlas() {
        return BLIGHT_FIRE_ATLAS;
    }

    @SideOnly(CLIENT)
    public static TextureAtlasSprite getGrayFireAtlas() {
        return GRAY_BLIGHT_FIRE_ATLAS;
    }
}