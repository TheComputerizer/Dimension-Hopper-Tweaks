package mods.thecomputerizer.dimhoppertweaks.client.shader.uniform;

import net.minecraft.client.renderer.OpenGlHelper;

import java.nio.FloatBuffer;
import java.util.function.Function;

public class UniformMatrix extends Uniform<Float> {

    private final Function<Float,FloatBuffer> bufferFunc;

    public UniformMatrix(String name, Function<Float,FloatBuffer> bufferFunc) {
        super(name);
        this.bufferFunc = bufferFunc;
    }

    @Override
    public void upload(float partialTicks) {
        OpenGlHelper.glUniformMatrix2(getID(),false,this.bufferFunc.apply(partialTicks));
    }
}
