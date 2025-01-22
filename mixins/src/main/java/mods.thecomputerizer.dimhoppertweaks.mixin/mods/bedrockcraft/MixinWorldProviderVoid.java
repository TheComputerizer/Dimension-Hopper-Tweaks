package mods.thecomputerizer.dimhoppertweaks.mixin.mods.bedrockcraft;

import bedrockcraft.voidworld.WorldProviderVoid;
import mods.thecomputerizer.dimhoppertweaks.client.render.SkyShaderRenderer;
import mods.thecomputerizer.dimhoppertweaks.client.shader.ShaderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static mods.thecomputerizer.dimhoppertweaks.client.DHTClient.REMOVE_FOG;

@Mixin(value = WorldProviderVoid.class, remap = false)
public abstract class MixinWorldProviderVoid {

    @Unique
    private WorldProviderVoid dimhoppertweaks$cast() {
        return (WorldProviderVoid)(Object)this;
    }

    @Inject(at = @At("TAIL"), method = "init", remap = true)
    private void dimhoppertweaks$init(CallbackInfo ci) {
        WorldProviderVoid instance = dimhoppertweaks$cast();
        instance.setSkyRenderer(new SkyShaderRenderer(pt -> ShaderManager.getInstance().cosmicShader.use(pt)
                ,pt -> ShaderManager.getInstance().cosmicShader.release()));
    }

    /**
     * @author The_Computerizer
     * @reason Disable fog when the final boss is present
     */
    @Overwrite(remap = true)
    public boolean doesXZShowFog(int x, int z) {
        return !REMOVE_FOG;
    }
}