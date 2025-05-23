package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla_access;

import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityRenderer.class)
public interface EntityRendererAccess {

    @Accessor float getFarPlaneDistance();
}
