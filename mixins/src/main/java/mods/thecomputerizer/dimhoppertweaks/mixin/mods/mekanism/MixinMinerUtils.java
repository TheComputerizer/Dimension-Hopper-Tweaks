package mods.thecomputerizer.dimhoppertweaks.mixin.mods.mekanism;

import mekanism.common.util.MinerUtils;
import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import mods.thecomputerizer.dimhoppertweaks.mixin.DelayedModAccess;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Objects;

@Mixin(value = MinerUtils.class, remap = false)
public class MixinMinerUtils {
    
    @Redirect(method="getDrops",at=@At(value="INVOKE",
            target="Ljava/lang/ref/WeakReference;get()Ljava/lang/Object;"))
    private static Object dimhoppertweaks$addFakePlayerStages(WeakReference<?> ref) {
        EntityPlayer player = (EntityPlayer)ref.get();
        if(Objects.nonNull(player)) {
            BlockPos pos = player.getPosition();
            Collection<String> stages = DelayedModAccess.getTileStages(player.getEntityWorld(),pos);
            DHTRef.LOGGER.info("Got stages at ({},{},{}) as {}",pos.getX(),pos.getY(),pos.getZ(),stages);
            DelayedModAccess.setGameStages(player,stages);
        }
        return player;
    }
}