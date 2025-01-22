package mods.thecomputerizer.dimhoppertweaks.client.shader;

import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.BufferUtils;

import javax.annotation.Nullable;
import java.nio.FloatBuffer;
import java.util.Objects;

import static morph.avaritia.client.AvaritiaClientEventHandler.cosmicUVs;

public class CosmicShader extends Shader {

    private static final FloatBuffer LIGHT_BUFFER = createBuffer(new float[]{1f,1f,1f});

    private static FloatBuffer createBuffer(float[] defVals) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(defVals.length);
        for(float defVal : defVals) buffer.put(defVal);
        buffer.position(0);
        return buffer;
    }

    public CosmicShader() {
        super(DHTRef.res("shaders/boss/cosmic.fsh"),DHTRef.res("shaders/boss/cosmic.vsh"));
        LIGHT_BUFFER.put(new float[]{1f,1f,1f});
        addUniformInt("time",this::getTime);
        addUniformFloat("yaw",() -> 0f);
        addUniformFloat("pitch",() -> 0f);
        addUniformFloatBuffer("lightlevel",3,() -> LIGHT_BUFFER);
        addUniformFloat("lightmix",() -> 0.2f);
        addUniformMatrix("cosmicuvs",() -> cosmicUVs);
        addUniformFloat("externalScale",() -> 1f);
        addUniformFloat("opacity",() -> 1f);
    }

    @Override
    public boolean canRender(@Nullable WorldClient world) {
        return Objects.nonNull(world) && world.provider.getDimension()==2;
    }

    public int getTime() {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if(Objects.nonNull(player)) {
            return (int)(player.getEntityWorld().getWorldTime()%65536L);
        }
        return 0;
    }

    @Override
    void render(float partialTicks) {

    }
}