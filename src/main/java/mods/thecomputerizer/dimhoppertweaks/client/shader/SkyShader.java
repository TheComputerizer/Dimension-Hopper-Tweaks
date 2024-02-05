package mods.thecomputerizer.dimhoppertweaks.client.shader;

import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;

import javax.annotation.Nullable;
import java.util.Objects;

public class SkyShader extends Shader {


    public SkyShader() {
        super(DHTRef.res("shaders/boss/skytest.fsh"),DHTRef.res("shaders/boss/skytest.vsh"));
        addUniformFloat("millis",() -> System.nanoTime()/1000000f);
        addUniformFloat("timeScale",() -> 2.5E-7f);
        addUniformFloat("zoom",() -> 0.8f);
        addUniformFloat("offsetX",this::getXOffSet);
        addUniformFloat("offsetY",this::getYOffSet);
        addUniformFloat("offsetZ",() -> 0f);
    }

    @Override
    public boolean canRender(@Nullable WorldClient world) {
        return Objects.nonNull(world) && world.provider.getDimension()==2;
    }

    public float getXOffSet() {
        return (float)(Minecraft.getMinecraft().getRenderManager().viewerPosX/8000d);
    }

    public float getYOffSet() {
        return (float)(Minecraft.getMinecraft().getRenderManager().viewerPosY/8000d);
    }

    @Override
    public void render(float partialTicks) {
    }
}
