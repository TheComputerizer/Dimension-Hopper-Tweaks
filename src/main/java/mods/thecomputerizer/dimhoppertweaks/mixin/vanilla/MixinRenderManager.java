package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.client.render.BetterBlightFireRenderer;
import mods.thecomputerizer.dimhoppertweaks.mixin.access.EntityLivinBaseAccess;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderManager.class)
public class MixinRenderManager {

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/Render;doRender(Lnet/minecraft/entity/Entity;DDDFF)V"), method = "renderEntity")
    private <T extends Entity> void dimhoppertweaks$renderEntity(Render<T> instance, T entity, double x, double y,
                                                                 double z, float entityYaw, float partialTicks) {
        instance.doRender(entity, x, y, z, entityYaw, partialTicks);
        if(entity instanceof EntityLivingBase) {
            EntityLivingBase based = (EntityLivingBase)entity;
            if(((EntityLivinBaseAccess)based).dimhoppertweaks$isBlighted())
                BetterBlightFireRenderer.render((RenderManager)(Object)this,based,x,y,z);
        }
    }
}
