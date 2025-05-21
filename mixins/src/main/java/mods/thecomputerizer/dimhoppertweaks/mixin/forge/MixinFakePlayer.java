package mods.thecomputerizer.dimhoppertweaks.mixin.forge;

import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import mods.thecomputerizer.dimhoppertweaks.mixin.api.IFakePlayer;
import net.minecraftforge.common.util.FakePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Collection;

@Mixin(value = FakePlayer.class, remap = false)
public abstract class MixinFakePlayer implements IFakePlayer {

    @Unique
    private FakePlayer dimhoppertweaks$cast() {
        return (FakePlayer)(Object)this;
    }

    @Override
    public void dimhoppertweaks$setStages(Collection<String> stages) {
        DelayedModAccess.setEntityStages(dimhoppertweaks$cast(), stages);
    }

    @Override
    public boolean dimhoppertweaks$hasStage(String stage) {
        return DelayedModAccess.hasGameStage(dimhoppertweaks$cast(),stage);
    }
}