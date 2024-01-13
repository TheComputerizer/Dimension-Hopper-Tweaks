package mods.thecomputerizer.dimhoppertweaks.mixin.access;

import net.minecraft.entity.Entity;

public interface ArmorWrapperAccess {

    void dimhoppertweaks$preRenderSplit(Entity entity);
    void dimhoppertweaks$postRenderSplit();
}
