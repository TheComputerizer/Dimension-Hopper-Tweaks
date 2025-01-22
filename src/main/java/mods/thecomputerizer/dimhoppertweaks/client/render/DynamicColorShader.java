package mods.thecomputerizer.dimhoppertweaks.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.Shader;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.Objects;

import static net.minecraftforge.fml.relauncher.Side.CLIENT;

@SideOnly(CLIENT)
public class DynamicColorShader extends Shader {
    public DynamicColorShader(IResourceManager manager, String name, Framebuffer bufferIn, Framebuffer bufferOut) throws IOException {
        super(manager,name,bufferIn,bufferOut);
    }

    @Override
    public void render(float partialTicks) {
        if(Objects.nonNull(Minecraft.getMinecraft().player))
            this.getShaderManager().getShaderUniformOrDefault("Prominence").set(ClientEffects.getColorCorrection());
        super.render(partialTicks);
    }
}