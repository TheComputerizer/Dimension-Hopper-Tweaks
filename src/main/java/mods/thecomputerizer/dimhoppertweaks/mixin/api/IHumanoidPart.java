package mods.thecomputerizer.dimhoppertweaks.mixin.api;

import mods.thecomputerizer.dimhoppertweaks.client.model.ModelConstructsArmorWrapper;

public interface IHumanoidPart {

    void dimhoppertweaks$apply(ModelConstructsArmorWrapper wrapper);
    void dimhoppertweaks$deapply(ModelConstructsArmorWrapper wrapper);
}