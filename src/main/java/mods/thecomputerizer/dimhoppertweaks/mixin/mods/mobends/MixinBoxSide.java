package mods.thecomputerizer.dimhoppertweaks.mixin.mods.mobends;

import goblinbob.mobends.core.client.model.BoxSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = BoxSide.class, remap = false)
public interface MixinBoxSide {

    @Accessor int getFaceIndex();
}
