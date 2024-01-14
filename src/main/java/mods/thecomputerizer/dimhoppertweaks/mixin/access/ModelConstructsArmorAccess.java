package mods.thecomputerizer.dimhoppertweaks.mixin.access;

import net.minecraft.entity.Entity;

import javax.annotation.Nullable;

public interface ModelConstructsArmorAccess {

    void dimhoppertweaks$assignRenders();
    void dimhoppertweaks$render(@Nullable Entity entity, float f, float f1, float f2, float f3, float f4, float f5);
}
