package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.client.render.DynamicColorShader;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.util.List;

@Mixin(ShaderGroup.class)
public abstract class MixinShaderGroup {

    @Shadow @Final private IResourceManager resourceManager;
    @Shadow @Final private List<Shader> listShaders;

    @Inject(at = @At("HEAD"), method = "addShader", cancellable = true)
    private void dimhoppertweaks$addShader(
            String name, Framebuffer bufferIn, Framebuffer bufferOut, CallbackInfoReturnable<Shader> cir) throws IOException {
        if(name.matches("dimhoppertweaks:dynamic_color_overlay")) {
            DynamicColorShader shader = new DynamicColorShader(this.resourceManager,name,bufferIn,bufferOut);
            this.listShaders.add(this.listShaders.size(),shader);
            cir.setReturnValue(shader);
        }
    }
}