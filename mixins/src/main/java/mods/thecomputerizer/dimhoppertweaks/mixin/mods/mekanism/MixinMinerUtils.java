package mods.thecomputerizer.dimhoppertweaks.mixin.mods.mekanism;

import mekanism.common.util.MinerUtils;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.FakePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.lang.ref.WeakReference;

@Mixin(value = MinerUtils.class, remap = false)
public class MixinMinerUtils {
    
    @Redirect(method="getDrops",at=@At(value="INVOKE",
            target="Ljava/lang/ref/WeakReference;get()Ljava/lang/Object;"))
    private static Object dimhoppertweaks$addFakePlayerStages(WeakReference<?> ref) {
        EntityPlayer player = (EntityPlayer)ref.get();
        if(player instanceof FakePlayer) DelayedModAccess.inheritTileStages((FakePlayer)player);
        return player;
    }
}