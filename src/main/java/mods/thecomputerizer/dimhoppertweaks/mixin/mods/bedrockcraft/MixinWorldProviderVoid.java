package mods.thecomputerizer.dimhoppertweaks.mixin.mods.bedrockcraft;

import bedrockcraft.voidworld.WorldProviderVoid;
import mods.thecomputerizer.dimhoppertweaks.client.DHTClient;
import mods.thecomputerizer.dimhoppertweaks.client.render.SkyShaderRenderer;
import morph.avaritia.client.render.shader.CosmicShaderHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = WorldProviderVoid.class, remap = false)
public abstract class MixinWorldProviderVoid {

    @Unique
    private WorldProviderVoid dimhoppertweaks$cast() {
        return (WorldProviderVoid)(Object)this;
    }

    @Inject(at = @At("TAIL"), method = "init", remap = true)
    private void dimhoppertweaks$init(CallbackInfo ci) {
        WorldProviderVoid instance = dimhoppertweaks$cast();
        instance.setSkyRenderer(new SkyShaderRenderer(v -> {
            CosmicShaderHelper.setLightLevel(1f);
            CosmicShaderHelper.cosmicOpacity = 1f;
            CosmicShaderHelper.useShader();
        },v -> CosmicShaderHelper.releaseShader()));
    }

    /**
     * @author The_Computerizer
     * @reason Disable fog when the final boss is present
     */
    @Overwrite(remap = true)
    public boolean doesXZShowFog(int x, int z) {
        return !DHTClient.REMOVE_FOG;
    }
}