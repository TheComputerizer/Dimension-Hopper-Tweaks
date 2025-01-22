package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import mods.thecomputerizer.dimhoppertweaks.client.render.BetterBlightFireRenderer;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.IEntityLivingBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("SpellCheckingInspection")
@Mixin(RenderManager.class)
public abstract class MixinRenderManager {

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/Render;"+
            "doRender(Lnet/minecraft/entity/Entity;DDDFF)V"), method = "renderEntity")
    private <T extends Entity> void dimhoppertweaks$renderEntity(
            Render<T> instance, T entity, double x, double y, double z, float yaw, float partialTicks) {
        instance.doRender(entity,x,y,z,yaw,partialTicks);
        if(entity instanceof EntityLivingBase) {
            EntityLivingBase living = (EntityLivingBase)entity;
            if(((IEntityLivingBase)living).dimhoppertweaks$isBlighted())
                BetterBlightFireRenderer.render((RenderManager)(Object)this,living,x,y,z);
        }
    }
}