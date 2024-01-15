package mods.thecomputerizer.dimhoppertweaks.mixin.api;

import net.minecraft.entity.Entity;

public interface IArmorWrapper {

    void dimhoppertweaks$preRenderSplit(Entity entity);
    void dimhoppertweaks$postRenderSplit();
}