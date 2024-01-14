package mods.thecomputerizer.dimhoppertweaks.mixin.access;

import mods.thecomputerizer.dimhoppertweaks.client.model.ModelConstructsArmorWrapper;

public interface HumanoidPartAccess {

    void dimhoppertweaks$apply(ModelConstructsArmorWrapper wrapper);
    void dimhoppertweaks$deapply(ModelConstructsArmorWrapper wrapper);
}
