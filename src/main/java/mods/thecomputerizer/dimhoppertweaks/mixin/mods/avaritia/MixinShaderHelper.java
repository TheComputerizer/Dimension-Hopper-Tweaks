package mods.thecomputerizer.dimhoppertweaks.mixin.mods.avaritia;

import morph.avaritia.client.render.shader.ShaderHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = ShaderHelper.class, remap = false)
public abstract class MixinShaderHelper {

    @ModifyConstant(constant = @Constant(longValue = 2147483647L),
            method = "useShader(ILmorph/avaritia/client/render/shader/ShaderCallback;)V")
    private static long dimhoppertweaks$fixTime(long original) {
        return 65536L;
    }
}