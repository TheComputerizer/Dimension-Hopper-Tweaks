package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityRenderer.class)
public interface MixinEntityRenderer {

    @Accessor float getFarPlaneDistance();
}
