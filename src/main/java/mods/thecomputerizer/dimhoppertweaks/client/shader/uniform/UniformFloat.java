package mods.thecomputerizer.dimhoppertweaks.client.shader.uniform;

import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.OpenGlHelper;

import java.nio.FloatBuffer;
import java.util.function.BiConsumer;

public class UniformFloat extends Uniform<Float> {

    private final int size;
    private final FloatBuffer buffer;
    private final BiConsumer<FloatBuffer,Float> bufferFunc;

    public UniformFloat(String name, int size, BiConsumer<FloatBuffer,Float> bufferFunc, float ... defaults) {
        super(name);
        this.size = size;
        this.buffer = GLAllocation.createDirectFloatBuffer(size);
        this.buffer.put(defaults);
        this.bufferFunc = bufferFunc;
    }

    @Override
    public void upload(float partialTicks) {
        this.bufferFunc.accept(this.buffer,partialTicks);
        if(this.size==1) OpenGlHelper.glUniform1(getID(),this.buffer);
        else if(this.size==2) OpenGlHelper.glUniform2(getID(),this.buffer);
        else if(this.size==3) OpenGlHelper.glUniform3(getID(),this.buffer);
        else OpenGlHelper.glUniform4(getID(),this.buffer);
    }
}
